package com.my.projects.quizapp.util.extensions

import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

fun DrawerLayout.openClose() {
    if (this.isDrawerOpen(GravityCompat.START)) {
        this.closeDrawer(GravityCompat.START)
    } else {
        this.openDrawer(GravityCompat.START)
    }
}