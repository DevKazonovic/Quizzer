package com.my.projects.quizapp.presentation.history.detail

import androidx.lifecycle.*
import com.my.projects.quizapp.data.local.model.QuizEntity
import com.my.projects.quizapp.data.local.model.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.data.repository.QuizLocalRepository
import com.my.projects.quizapp.util.wrappers.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class QuizDetailViewModel @Inject constructor(
    private val quizRepository: QuizLocalRepository,
) : ViewModel() {

    private var _quizID = MutableLiveData<Long>()
    val quizID: MutableLiveData<Long> get() = _quizID

    private val _quiz = _quizID.switchMap { quizID ->
        liveData {
            withContext(Dispatchers.IO) {
                emit(quizRepository.findQuizById(quizID))
            }
        }.distinctUntilChanged()
    }

    private var _isQuizUpdated = MutableLiveData<Event<Boolean>>()
    private var _isQuizDeleted = MutableLiveData<Event<Boolean>>()

    init {
        Timber.d("Init")
    }

    private fun getQuizData(quizID: Long) {
        _quizID.value = quizID
    }

    fun refresh() {
        val id = _quizID.value
        if (id != null) {
            getQuizData(id)
        }
    }

    fun onQuizUpdate(quizEntity: QuizEntity) {
        viewModelScope.launch {
            quizRepository.updateQuiz(quizEntity)
            _isQuizUpdated.value = Event(true)
        }
    }

    fun onQuizDelete(quizEntity: QuizEntity) {
        viewModelScope.launch {
            quizRepository.deleteQuiz(quizEntity)
            _isQuizDeleted.value = Event(true)
        }
    }


    //Getters
    fun getQuizDetail(): LiveData<QuizWithQuestionsAndAnswers> = _quiz
    fun getCuurentQuiz() = _quiz.value?.quizEntity
    val isQuizUpdated: LiveData<Event<Boolean>> get() = _isQuizUpdated
    val isQuizDeleted: LiveData<Event<Boolean>> get() = _isQuizDeleted

    override fun onCleared() {
        super.onCleared()
        Timber.d("OnCleared")
    }

}