package com.my.projects.quizapp.presentation.history.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.projects.quizapp.data.local.entity.Quiz
import com.my.projects.quizapp.data.local.entity.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.data.repository.QuizLocalRepository
import com.my.projects.quizapp.util.wrappers.Event
import kotlinx.coroutines.launch
import timber.log.Timber

class QuizDetailViewModel(
    private val quizRepository: QuizLocalRepository,
    private val quizID: Long
) : ViewModel() {

    private val _quiz = MutableLiveData<QuizWithQuestionsAndAnswers>()

    private var _isQuizUpdated = MutableLiveData<Event<Boolean>>()
    private var _isQuizDeleted = MutableLiveData<Event<Boolean>>()

    init {
        Timber.d("Init")
        getQuizData(quizID)
    }

    private fun getQuizData(quizID: Long) {
        viewModelScope.launch {
            _quiz.postValue(quizRepository.findQuizById(quizID))
        }
    }

    fun refresh() {
        getQuizData(quizID)
    }

    fun onQuizUpdate(quiz: Quiz) {
        viewModelScope.launch {
            quizRepository.updateQuiz(quiz)
            _isQuizUpdated.value = Event(true)
        }
    }

    fun onQuizDelete(quiz: Quiz) {
        viewModelScope.launch {
            quizRepository.deleteQuiz(quiz)
            _isQuizDeleted.value = Event(true)
        }
    }


    //Getters
    fun getQuizDetail(): LiveData<QuizWithQuestionsAndAnswers> = _quiz
    fun getCuurentQuiz() = _quiz.value?.quiz
    val isQuizUpdated: LiveData<Event<Boolean>> get() = _isQuizUpdated
    val isQuizDeleted: LiveData<Event<Boolean>> get() = _isQuizDeleted

    override fun onCleared() {
        Timber.d("onCleared")
        super.onCleared()
    }
}