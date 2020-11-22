package com.my.projects.quizapp.presentation.controller

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.my.projects.quizapp.data.local.QuizDB
import com.my.projects.quizapp.data.local.repository.QuizRepository
import com.my.projects.quizapp.data.local.repository.QuizRepositoryImpl

class QuizInjector(app: Application) : AndroidViewModel(app) {
    private fun getQuizRepository(): QuizRepository {
        return QuizRepositoryImpl(QuizDB.getInstance(getApplication()))
    }

    fun provideQuizViewModelFactory(): QuizViewModelFactory =
        QuizViewModelFactory(getQuizRepository())
}