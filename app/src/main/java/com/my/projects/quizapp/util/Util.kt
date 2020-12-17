package com.my.projects.quizapp.util

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import timber.log.Timber
import java.util.*

class Util {
    companion object {

        fun setNightMode(@AppCompatDelegate.NightMode nightMode: Int) {
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }

        fun makeToast(context: Context, text: String) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }


        fun compareDates(date1: Date, date2: Date): Boolean {
            val cal1 = Calendar.getInstance()
            val cal2 = Calendar.getInstance()

            cal1.time = date1
            cal2.time = date2

            val yearDiff: Int = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)
            val monthDiff: Int = cal1.get(Calendar.MONTH) - cal2.get(Calendar.MONTH)
            val dayDiff: Int = cal1.get(Calendar.DAY_OF_MONTH) - cal2.get(Calendar.DAY_OF_MONTH)
            Timber.d("$yearDiff, $monthDiff, $dayDiff")

            return yearDiff == 0 && monthDiff == 0 && dayDiff == 0

        }

    }
}