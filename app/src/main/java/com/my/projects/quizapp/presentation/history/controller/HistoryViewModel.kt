package com.my.projects.quizapp.presentation.history.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.projects.quizapp.data.local.entity.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.data.local.repository.QuizRepository
import kotlinx.coroutines.launch

class HistoryViewModel(val quizRepository: QuizRepository) : ViewModel() {

    private var _quizzes = MutableLiveData<List<QuizWithQuestionsAndAnswers>>()
    val quizzes: LiveData<List<QuizWithQuestionsAndAnswers>> get() = _quizzes


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