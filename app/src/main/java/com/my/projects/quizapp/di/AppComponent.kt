package com.my.projects.quizapp.di

import android.app.Application
import android.content.Context
import com.my.projects.quizapp.di.module.DatabaseModule
import com.my.projects.quizapp.di.module.NetworkModule
import com.my.projects.quizapp.di.module.ViewModelFactoryModule
import com.my.projects.quizapp.di.module.ViewModelModule
import com.my.projects.quizapp.presentation.history.detail.QuizDetailFragment
import com.my.projects.quizapp.presentation.history.list.HistoryFragment
import com.my.projects.quizapp.presentation.quiz.quizplay.QuizFragment
import com.my.projects.quizapp.presentation.quiz.score.ScoreFragment
import com.my.projects.quizapp.presentation.quiz.setting.QuizSettingFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        DatabaseModule::class,
        ViewModelFactoryModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {


    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application,
            @BindsInstance context: Context
        ): AppComponent
    }

    fun inject(fragment: ScoreFragment)
    fun inject(fragment: QuizFragment)
    fun inject(fragment: QuizSettingFragment)
    fun inject(fragment: HistoryFragment)
    fun inject(fragment: QuizDetailFragment)
}