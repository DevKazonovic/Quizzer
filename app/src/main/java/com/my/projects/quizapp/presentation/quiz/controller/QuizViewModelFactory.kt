package com.my.projects.quizapp.presentation.quiz.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.my.projects.quizapp.data.local.repository.IQuizRepository

class QuizViewModelFactory(private val repo: IQuizRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            return QuizViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}