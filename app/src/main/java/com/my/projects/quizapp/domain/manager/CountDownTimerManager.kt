package com.my.projects.quizapp.domain.manager

import android.os.CountDownTimer
import javax.inject.Inject

class CountDownTimerManager @Inject constructor() {

    private lateinit var countDownTimer: CountDownTimer
    private lateinit var _listner: OnCountDownTimerChangeListener

    private var _periodInS: Int = -1
    private var _isSet: Boolean = false
    private var _isStarted: Boolean = false

    fun setCountDownTimer(periodInS: Int, listner: OnCountDownTimerChangeListener) {
        _listner = listner
        _periodInS = periodInS
        countDownTimer = object : CountDownTimer(periodInS.times(1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                listner.onTick(millisUntilFinished / 1000)
            }

            override fun onFinish() {
                listner.onFinish()
            }
        }
        _isSet = true
        _isStarted = false
    }

    fun stop() {
        countDownTimer.cancel()
        _isSet = false
    }

    fun start() {
        countDownTimer.start()
        _isStarted = true
    }

    fun reset() {
        if (_isStarted) {
            stop()
            setCountDownTimer(_periodInS, _listner)
        }
    }


}

interface OnCountDownTimerChangeListener {
    fun onTick(millisUntilFinished: Long)
    fun onFinish()
}