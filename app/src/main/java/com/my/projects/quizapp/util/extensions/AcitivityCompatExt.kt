package com.my.projects.quizapp.util.extensions

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.hideKeyboard() {
    val inputManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = this.currentFocus
    inputManager.hideSoftInputFromWindow(view?.windowToken, 0)
}