package com.my.projects.quizapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "Quiz")
data class QuizEntity(
    val title: String,
    val score: Int,
    val date: Date,
    val category: Int
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

