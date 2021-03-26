package com.my.projects.quizapp.domain.manager

import android.os.CountDownTimer
import timber.log.Timber
import javax.inject.Inject

class CountDownTimerManager @Inject constructor(private val sharedPreferenceManager: SharedPreferenceManager) {
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var listner: OnCountDownTimerChangeListener

    var isSet: Boolean = false
    var isStarted: Boolean = false

    fun setCountDownTimer(listner: OnCountDownTimerChangeListener) {
        this.listner = listner
        val millisInFutur = sharedPreferenceManager.getCountDownTimeInS()
        countDownTimer = object : CountDownTimer(millisInFutur, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                listner.onTick(millisUntilFinished / 1000)
            }

            override fun onFinish() {
                listner.onFinish()
            }
        }
        isSet = true
        isStarted = false
        Timber.d("CountDownTimer Is Set")
    }

    fun stop() {
        Timber.d("stop()")
        countDownTimer.cancel()
        isSet = false

    }

    fun start() {
        Timber.d("start()")
        countDownTimer.start()
        isStarted = true

    }

    fun reset() {
        Timber.d("reset()")
        stop()
        setCountDownTimer(listner)
    }

    interface OnCountDownTimerChangeListener {
        fun onTick(millisUntilFinished: Long)
        fun onFinish()
    }

}