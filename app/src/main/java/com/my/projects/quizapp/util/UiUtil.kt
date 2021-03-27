package com.my.projects.quizapp.util

import android.content.Context
import android.text.Editable
import android.util.TypedValue
import androidx.appcompat.app.AppCompatDelegate
import com.my.projects.quizapp.R


class UiUtil {
    companion object {

        fun setNightMode(@AppCompatDelegate.NightMode nightMode: Int) {
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }

        fun getEditbaleInstance(): Editable.Factory = Editable.Factory.getInstance()

        fun getEditableInstance(text: String): Editable = Editable.Factory.getInstance().newEditable(text)

        fun getThemeColorAttr(context:Context, attr:Int) : Int{
            val typedValue = TypedValue()
            val theme = context.theme
            theme.resolveAttribute(attr,typedValue,true)
            return typedValue.data
        }

    }
}