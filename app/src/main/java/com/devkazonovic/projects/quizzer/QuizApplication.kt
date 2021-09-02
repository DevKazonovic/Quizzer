package com.devkazonovic.projects.quizzer

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.devkazonovic.projects.quizzer.di.AppComponent
import com.devkazonovic.projects.quizzer.di.DaggerAppComponent
import com.devkazonovic.projects.quizzer.domain.enums.ThemeType
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import timber.log.Timber

const val KEY_SETTING_THEME = "KEY SETTING THEME"

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
        val settingSharedPreference =
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        when (ThemeType.valueOf(
            settingSharedPreference.getString(
                KEY_SETTING_THEME,
                ThemeType.THEME_DEFAULT.name
            ) ?: ThemeType.THEME_DEFAULT.name
        )
        ) {
            ThemeType.THEME_DEFAULT -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            ThemeType.THEME_DARK -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            ThemeType.THEME_LIGHT -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}