package com.devkazonovic.projects.quizzer.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class QuestionEntity(
    var quizID: Long,
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}