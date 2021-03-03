package com.my.projects.quizapp.domain.manager

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferenceManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun getCountTimerValueInS(): Long =
        (sharedPreferences
            .getString(KEY_COUNT_DOWN_TIMER_PERIOD, "60")?.toLong()
            ?: 60) * 1000


    companion object {
        const val KEY_COUNT_DOWN_TIMER_PERIOD = "KEY_COUNT_DOWN_TIMER_PERIOD"
    }
}