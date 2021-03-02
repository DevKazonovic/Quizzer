package com.my.projects.quizapp.di

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import com.my.projects.quizapp.data.remote.QuizService
import com.my.projects.quizapp.presentation.quiz.quizplay.QuizFragment
import com.my.projects.quizapp.presentation.quiz.score.ScoreFragment
import com.my.projects.quizapp.presentation.quiz.setting.QuizSettingFragment
import com.my.projects.quizapp.presentation.setting.SettingFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component (modules = [
    NetworkModule::class,
    DatabaseModule::class,
ViewModelFactoryModule::class,
ViewModelModule::class
])
interface AppComponent {


    @Component.Factory
    interface Factory{
        fun create(@BindsInstance application: Application, @BindsInstance context: Context): AppComponent
    }

    fun inject(fragment: ScoreFragment)
    fun inject(fragment: QuizFragment)
    fun inject(fragment: QuizSettingFragment)

}