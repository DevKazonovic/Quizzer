package com.devkazonovic.projects.quizzer.util.extensions

import android.content.Context
import com.google.android.material.snackbar.Snackbar
import com.devkazonovic.projects.quizzer.R
import com.devkazonovic.projects.quizzer.util.ThemeUtil.Companion.getThemeColorAttr

fun Snackbar.setColor(isSeccessful: Boolean, context: Context): Snackbar {
    if (isSeccessful) {
        this.setBackgroundTint(getThemeColorAttr(context, R.attr.colorGreenThings))
    } else this.setBackgroundTint(getThemeColorAttr(context, R.attr.colorRedThings))
    return this

}

fun Snackbar.setColorWithCallback(
    isSeccessful: Boolean,
    context: Context,
    callback: () -> Boolean
): Snackbar {
    if (isSeccessful) {
        this.setBackgroundTint(getThemeColorAttr(context, R.attr.colorRedThings))
        this.addCallback(
            object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    callback()
                }
            }
        )
    } else this.setBackgroundTint(getThemeColorAttr(context, R.attr.colorRedThings))

    return this
}
