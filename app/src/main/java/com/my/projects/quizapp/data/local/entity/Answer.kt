package com.my.projects.quizapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Answer(
    var questionID: Long,
    val answer: String,
    val isCorrect: Boolean,
    val isUser: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var answerID: Long = 0
}