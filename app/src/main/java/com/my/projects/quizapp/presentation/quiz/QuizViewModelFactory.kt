package com.my.projects.quizapp.presentation.quiz

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.my.projects.quizapp.data.repository.QuizLocalRepository
import com.my.projects.quizapp.data.repository.QuizRemoteRepository

class QuizViewModelFactory(
    val app: Application,
    private val quizRepository: QuizLocalRepository,
    private val quizRemoteRepository: QuizRemoteRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            return QuizViewModel(app,quizRepository,quizRemoteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}