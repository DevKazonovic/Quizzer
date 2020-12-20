package com.my.projects.quizapp.presentation.history.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.my.projects.quizapp.data.repository.IQuizRepository

class QuizDetailViewModelFactory(
    private val repo: IQuizRepository,
    private val quizID: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizDetailViewModel::class.java)) {
            return QuizDetailViewModel(repo, quizID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}