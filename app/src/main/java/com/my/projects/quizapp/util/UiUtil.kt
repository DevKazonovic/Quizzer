package com.my.projects.quizapp.util

import android.text.Editable


class UiUtil {
    companion object {
        fun getEditableInstance(text: String?): Editable =
            Editable.Factory.getInstance().newEditable(text)
    }
}