package com.my.projects.quizapp.util.extensions

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController

fun Fragment.setToolbar(toolbar: Toolbar) {
    val navController = findNavController()
    val appBarConfiguration = AppBarConfiguration(navController.graph)
    toolbar.setupWithNavController(navController, appBarConfiguration)
}