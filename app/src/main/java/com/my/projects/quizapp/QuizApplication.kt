package com.my.projects.quizapp

import android.app.Application
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import com.my.projects.quizapp.di.AppComponent
import com.my.projects.quizapp.di.DaggerAppComponent
import com.my.projects.quizapp.util.ThemeUtil
import timber.log.Timber

class QuizApplication : Application() {

    lateinit var component: AppComponent
    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.factory().create(this, applicationContext)
        AndroidThreeTen.init(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(this)
        }

        updateThemeMode()
    }

    private fun updateThemeMode() {
        ThemeUtil.updateAppTheme(component.sharedPreferenceManager().getCurrentAppTheme())
    }
}