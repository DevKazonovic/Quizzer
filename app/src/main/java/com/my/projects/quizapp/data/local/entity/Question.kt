package com.my.projects.quizapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Question(
    var quizID: Long,
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}