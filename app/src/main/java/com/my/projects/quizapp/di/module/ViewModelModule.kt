package com.my.projects.quizapp.di.module

import androidx.lifecycle.ViewModel
import com.my.projects.quizapp.di.annotation.ViewModelKey
import com.my.projects.quizapp.presentation.history.detail.QuizDetailViewModel
import com.my.projects.quizapp.presentation.history.list.HistoryViewModel
import com.my.projects.quizapp.presentation.quiz.QuizViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(QuizViewModel::class)
    abstract fun bindsQuizViewModel(viewModel: QuizViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HistoryViewModel::class)
    abstract fun bindsHistoryViewModule(viewModel: HistoryViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(QuizDetailViewModel::class)
    abstract fun bindsQuizDetailViewModel(viewModel: QuizDetailViewModel): ViewModel
}