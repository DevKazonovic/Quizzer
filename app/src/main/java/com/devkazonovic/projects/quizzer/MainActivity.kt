package com.devkazonovic.projects.quizzer

import android.app.Activity
import android.app.ActivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.devkazonovic.projects.quizzer.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            val task = ActivityManager.TaskDescription().apply {
                label
                R.drawable.ic_logo
                R.attr.colorToolbarBackground
            }

            (this as Activity).setTaskDescription(task)
        }
    }
}