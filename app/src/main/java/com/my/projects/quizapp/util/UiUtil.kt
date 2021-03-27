package com.my.projects.quizapp.util

import android.text.Editable
import androidx.appcompat.app.AppCompatDelegate


class UiUtil {
    companion object {

        fun setNightMode(@AppCompatDelegate.NightMode nightMode: Int) {
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }

        fun getEditbaleInstance(): Editable.Factory = Editable.Factory.getInstance()

        fun getEditableInstance(text: String): Editable =
            Editable.Factory.getInstance().newEditable(text)

    }
}