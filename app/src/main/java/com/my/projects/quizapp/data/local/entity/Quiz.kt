package com.my.projects.quizapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Quiz(
    val title: String,
    val score: Int,
    val date: Date
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

