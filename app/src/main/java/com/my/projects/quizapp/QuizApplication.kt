package com.my.projects.quizapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.facebook.stetho.Stetho
import com.my.projects.quizapp.di.AppComponent
import com.my.projects.quizapp.di.DaggerAppComponent
import com.my.projects.quizapp.util.UiUtil
import timber.log.Timber

class QuizApplication : Application() {

    lateinit var component: AppComponent
    override fun onCreate() {
        super.onCreate()
        updateThemeMode()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }

        component = DaggerAppComponent.factory().create(this, applicationContext)
    }

    private fun updateThemeMode() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val isDarkMode = sharedPreferences.getBoolean("KEY_DARK_MODE", false)
        if (isDarkMode) {
            UiUtil.setNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            UiUtil.setNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}