package com.devkazonovic.projects.quizzer.di

import android.app.Application
import android.content.Context
import com.devkazonovic.projects.quizzer.di.module.*
import com.devkazonovic.projects.quizzer.domain.manager.AppSettingManager
import com.devkazonovic.projects.quizzer.presentation.history.detail.QuizDetailFragment
import com.devkazonovic.projects.quizzer.presentation.history.list.HistoryFragment
import com.devkazonovic.projects.quizzer.presentation.quiz.answers.QuizSummaryFragment
import com.devkazonovic.projects.quizzer.presentation.quiz.playground.QuizPlayGroundFragment
import com.devkazonovic.projects.quizzer.presentation.quiz.score.QuizScoreFragment
import com.devkazonovic.projects.quizzer.presentation.quiz.setting.QuizSettingFragment
import com.devkazonovic.projects.quizzer.presentation.setting.SettingFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        DatabaseModule::class,
        ViewModelFactoryModule::class,
        ViewModelModule::class,
        PreferenceModule::class
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

    fun sharedPreferenceManager(): AppSettingManager


    fun inject(fragment: QuizScoreFragment)
    fun inject(playGroundFragment: QuizPlayGroundFragment)
    fun inject(fragment: QuizSettingFragment)
    fun inject(fragment: HistoryFragment)
    fun inject(fragment: QuizDetailFragment)
    fun inject(fragment: QuizSummaryFragment)
    fun inject(settingFragment: SettingFragment)

}