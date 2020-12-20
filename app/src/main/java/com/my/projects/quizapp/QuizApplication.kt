package com.my.projects.quizapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.my.projects.quizapp.util.Util
import timber.log.Timber

class QuizApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        updateThemeMode()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

    }

    private fun updateThemeMode() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val isDarkMode = sharedPreferences.getBoolean("KEY_DARK_MODE", false)
        if (isDarkMode) {
            Util.setNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            Util.setNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}