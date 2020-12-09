package com.my.projects.quizapp.util

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.my.projects.quizapp.util.Const.Companion.cats
import java.util.*

class Util {
    companion object {

        fun setNightMode(@AppCompatDelegate.NightMode nightMode: Int) {
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }

        fun makeToast(context: Context, text: String) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }

        fun generateRandomTitle(categoryID:Int,score:Int): String {
            val uuid = UUID.randomUUID()
            val category = cats.find { category -> category.id==categoryID }?.name ?: "no-cat"
            return "Quiz-${category}-${uuid.toString().subSequence(0,6)}-$score"
        }

    }
}