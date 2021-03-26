package com.my.projects.quizapp.domain.manager

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferenceManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun getCountDownTimeInS(): Long =
        (sharedPreferences.getString(KEY_COUNT_DOWN_TIMER_PERIOD, "60")
            ?.toLong() ?: 60) * 1000

    fun getCountDownTimer(): Short =
        (sharedPreferences.getString(KEY_COUNT_DOWN_TIMER_PERIOD, "60")
            ?.toShort() ?: 60)

    fun getNumberOfQuestions() : Int =
        sharedPreferences.getString(KEY_NUMBER_OF_QUESTIONS,"10")?.toInt() ?: 10

    companion object {
        const val KEY_COUNT_DOWN_TIMER_PERIOD = "KEY_COUNT_DOWN_TIMER_PERIOD"
        const val KEY_NUMBER_OF_QUESTIONS = "KEY_NUMBER_OF_QUESTIONS"
    }
}