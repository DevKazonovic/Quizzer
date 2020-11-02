package com.my.projects.quizapp

import android.app.Application
import timber.log.Timber

class QuizApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}