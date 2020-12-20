package com.my.projects.quizapp.di

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.my.projects.quizapp.data.local.QuizDB
import com.my.projects.quizapp.data.repository.IQuizRepository
import com.my.projects.quizapp.data.repository.QuizRepositoryImpl
import com.my.projects.quizapp.presentation.history.controller.HistoryViewModelFactory
import com.my.projects.quizapp.presentation.quiz.controller.QuizViewModelFactory

class QuizInjector(app: Application) : AndroidViewModel(app) {
    private fun getQuizRepository(): IQuizRepository {
        return QuizRepositoryImpl(QuizDB.getInstance(getApplication()))
    }

    fun provideQuizViewModelFactory(): QuizViewModelFactory =
        QuizViewModelFactory(getApplication(), getQuizRepository())

    fun provideHistoryViewModelFactory(): HistoryViewModelFactory =
        HistoryViewModelFactory(getQuizRepository())

}