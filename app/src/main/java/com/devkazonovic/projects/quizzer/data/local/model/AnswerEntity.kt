package com.devkazonovic.projects.quizzer.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class AnswerEntity(
    var questionID: Long,
    val answer: String,
    val isCorrect: Boolean,
    val isUser: Boolean
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var answerID: Long = 0
}