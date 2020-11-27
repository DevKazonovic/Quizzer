package com.my.projects.quizapp.presentation.history.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.my.projects.quizapp.data.local.repository.IQuizRepository

class HistoryViewModelFactory(private val repo: IQuizRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}