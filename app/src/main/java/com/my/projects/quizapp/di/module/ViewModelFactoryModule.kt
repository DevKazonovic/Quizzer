package com.my.projects.quizapp.di.module

import androidx.lifecycle.ViewModelProvider
import com.my.projects.quizapp.presentation.ViewModelProviderFactory
import dagger.Binds
import dagger.Module


@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelProviderFactory: ViewModelProviderFactory): ViewModelProvider.Factory
}