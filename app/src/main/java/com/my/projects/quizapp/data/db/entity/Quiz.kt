package com.my.projects.quizapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.my.projects.quizapp.data.model.Answer
import java.util.*


@Entity
data class Quiz(
    val date:Date,
    val score: Int,
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    val userAnswer:String,
    val answers: List<Answer>
){
    @PrimaryKey(autoGenerate = true) var id:Long=0
}