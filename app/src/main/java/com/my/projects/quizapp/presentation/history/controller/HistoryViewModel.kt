package com.my.projects.quizapp.presentation.history.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.projects.quizapp.data.local.entity.Quiz
import com.my.projects.quizapp.data.local.entity.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.data.local.repository.IQuizRepository
import com.my.projects.quizapp.util.wrappers.Event
import kotlinx.coroutines.launch

class HistoryViewModel(val quizRepository: IQuizRepository) : ViewModel() {

    private var _quizzes = MutableLiveData<List<QuizWithQuestionsAndAnswers>>()
    val quizzes: LiveData<List<QuizWithQuestionsAndAnswers>> get() = _quizzes

    private var _isQuizUpdated = MutableLiveData<Event<Boolean>>()
    val isQuizUpdated: LiveData<Event<Boolean>> get() = _isQuizUpdated

    private var _isQuizDeleted = MutableLiveData<Event<Boolean>>()
    val isQuizDeleted : LiveData<Event<Boolean>> get() = _isQuizDeleted

    fun onQuizUpdate(quiz: Quiz) {
        viewModelScope.launch {
            quizRepository.updateQuiz(quiz)
            _isQuizUpdated.value = Event(true)
        }
    }

    fun onQuizDelete(quiz: Quiz) {
        viewModelScope.launch {
            quizRepository.deleteQuiz(quiz)
        }
    }


    fun getStoredUserQuizzes() {
        viewModelScope.launch {
            _quizzes.postValue(quizRepository.findAll())
        }
    }

    fun deleteAllQuizzes() {
        viewModelScope.launch {
            quizRepository.deleteAll()
            getStoredUserQuizzes()
        }
    }
}