package com.my.projects.quizapp.presentation.quiz.controller

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.my.projects.quizapp.data.repository.IQuizRepository

class QuizViewModelFactory(private val repo: IQuizRepository, val app: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            return QuizViewModel(repo, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}