package com.my.projects.quizapp

import android.app.Application
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import com.my.projects.quizapp.di.AppComponent
import com.my.projects.quizapp.di.DaggerAppComponent
import com.my.projects.quizapp.util.ThemeUtil
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