package com.my.projects.quizapp.util

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate

class Util {
    companion object {
        fun setNightMode(@AppCompatDelegate.NightMode nightMode: Int) {
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }

        fun makeToast(context: Context, text: String) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }
}