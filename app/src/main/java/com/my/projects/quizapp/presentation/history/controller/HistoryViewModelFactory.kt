package com.my.projects.quizapp.presentation.history.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.my.projects.quizapp.data.local.repository.QuizRepository

class HistoryViewModelFactory(private val repo: QuizRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}