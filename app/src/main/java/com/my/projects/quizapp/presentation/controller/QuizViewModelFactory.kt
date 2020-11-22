package com.my.projects.quizapp.presentation.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.my.projects.quizapp.data.local.repository.QuizRepository

class QuizViewModelFactory(private val repo: QuizRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            return QuizViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}