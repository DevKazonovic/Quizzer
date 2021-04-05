package com.devkazonovic.projects.quizzer

import android.app.Application
import com.devkazonovic.projects.quizzer.di.AppComponent
import com.devkazonovic.projects.quizzer.di.DaggerAppComponent
import com.devkazonovic.projects.quizzer.util.ThemeUtil
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import timber.log.Timber

class QuizApplication : Application() {

    val component: AppComponent by lazy {
        DaggerAppComponent.factory().create(this, applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(this)
        }
        updateAppTheme()
    }

    private fun updateAppTheme() {
        ThemeUtil.updateAppTheme(component.sharedPreferenceManager().getCurrentAppTheme())
    }
}