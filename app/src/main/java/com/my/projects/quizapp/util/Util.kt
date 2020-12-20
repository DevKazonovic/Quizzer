package com.my.projects.quizapp.util

import android.text.Editable
import androidx.appcompat.app.AppCompatDelegate

class Util {
    companion object {

        fun setNightMode(@AppCompatDelegate.NightMode nightMode: Int) {
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }

        fun getEditbaleInstance() = Editable.Factory.getInstance()

    }
}