package com.devkazonovic.projects.quizzer.di.module

import androidx.lifecycle.ViewModelProvider
import com.devkazonovic.projects.quizzer.presentation.ViewModelProviderFactory
import dagger.Binds
import dagger.Module


@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelProviderFactory: ViewModelProviderFactory): ViewModelProvider.Factory
}