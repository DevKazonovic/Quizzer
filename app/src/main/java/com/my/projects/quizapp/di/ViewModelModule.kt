package com.my.projects.quizapp.di

import androidx.lifecycle.ViewModel
import com.my.projects.quizapp.presentation.quiz.QuizViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(QuizViewModel::class)
    abstract fun bindsAuthViewModel(viewModel: QuizViewModel): ViewModel
}