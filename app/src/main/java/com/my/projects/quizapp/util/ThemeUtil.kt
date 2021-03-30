package com.my.projects.quizapp.util

import android.content.Context
import android.util.TypedValue
import androidx.appcompat.app.AppCompatDelegate

class ThemeUtil {
    companion object {
        fun updateAppTheme(isDarkMode: Boolean) {
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        fun getThemeColorAttr(context: Context, attr: Int): Int {
            val typedValue = TypedValue()
            val theme = context.theme
            theme.resolveAttribute(attr, typedValue, true)
            return typedValue.data
        }
    }
}