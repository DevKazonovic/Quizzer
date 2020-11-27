package com.my.projects.quizapp.presentation.history.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.projects.quizapp.data.local.entity.Quiz
import com.my.projects.quizapp.data.local.entity.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.data.local.repository.IQuizRepository
import kotlinx.coroutines.launch

class HistoryViewModel(val quizRepository: IQuizRepository) : ViewModel() {

    private var _quizzes = MutableLiveData<List<QuizWithQuestionsAndAnswers>>()
    val quizzes: LiveData<List<QuizWithQuestionsAndAnswers>> get() = _quizzes

    fun onQuizUpdate(quiz: Quiz) {
        viewModelScope.launch {
            quizRepository.updateQuiz(quiz)
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