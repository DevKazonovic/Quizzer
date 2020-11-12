package com.my.projects.quizapp.data.model

import com.my.projects.quizapp.data.db.entity.Quiz
import java.io.Serializable
import java.util.*

data class QuizModel(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    val answers: List<Answer>
)

data class Answer(
    val answer: String,
    val isCorrect: Boolean
)

data class QuizSetting(
    val amount: Int,
    val category: Int?=null,
    val type: String?="",
    val difficulty: String?="",
): Serializable


fun QuizModel.asQuizEntity(date:Date,score:Int,userAnswer:Answer):Quiz{
    return Quiz(date,score,category,type,difficulty,question,userAnswer,answers)
}