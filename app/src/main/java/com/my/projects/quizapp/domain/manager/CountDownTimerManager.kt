package com.my.projects.quizapp.domain.manager

import android.os.CountDownTimer
import timber.log.Timber
import javax.inject.Inject

class CountDownTimerManager @Inject constructor() {
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var listner: OnCountDownTimerChangeListener
    private var _periodInS: Int = -1

    fun setCountDownPeriod(periodInS: Int) {
        _periodInS = periodInS
        isSet = true
        isStarted = false
    }

    var isSet: Boolean = false
    var isStarted: Boolean = false

    fun setCountDownTimer(periodInS: Int, listner: OnCountDownTimerChangeListener) {
        this.listner = listner
        _periodInS = periodInS
        countDownTimer = object : CountDownTimer(periodInS.times(1000).toLong(), 1000) {
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
        setCountDownTimer(_periodInS, listner)
    }

    interface OnCountDownTimerChangeListener {
        fun onTick(millisUntilFinished: Long)
        fun onFinish()
    }

}