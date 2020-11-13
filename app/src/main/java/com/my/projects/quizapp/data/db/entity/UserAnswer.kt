package com.my.projects.quizapp.data.db.entity

import androidx.room.PrimaryKey

data class UserAnswer(
    var questionID:Long,
    val answer: String,
    val isCorrect: Boolean
){
    @PrimaryKey(autoGenerate = true) var answerID:Long=0
}