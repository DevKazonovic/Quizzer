package com.my.projects.quizapp.util.extensions

import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.my.projects.quizapp.R

fun Snackbar.setColor(isSeccessful: Boolean, context: Context): Snackbar {
    if (isSeccessful) {
        this.setBackgroundTint(ContextCompat.getColor(context, R.color.green))
    } else this.setBackgroundTint(ContextCompat.getColor(context, R.color.red))
    return this

}

fun Snackbar.setColorWithCallback(
    isSeccessful: Boolean,
    context: Context,
    callback: () -> Boolean
): Snackbar {
    if (isSeccessful) {
        this.setBackgroundTint(ContextCompat.getColor(context, R.color.green))
        this.addCallback(
            object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    callback()
                }
            }
        )
    } else this.setBackgroundTint(ContextCompat.getColor(context, R.color.red))

    return this
}
