package com.my.projects.quizapp.presentation.history.detail

import androidx.lifecycle.*
import com.my.projects.quizapp.data.local.model.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.data.repository.QuizLocalRepository
import kotlinx.coroutines.Dispatchers
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


    init {
        Timber.d("Init")
    }

    private fun getQuizData(quizID: Long) {
        _quizID.value = quizID
    }


    //Getters
    fun getQuizDetail(): LiveData<QuizWithQuestionsAndAnswers> = _quiz
    fun getCuurentQuiz() = _quiz.value?.quizEntity

    override fun onCleared() {
        super.onCleared()
        Timber.d("OnCleared")
    }

}