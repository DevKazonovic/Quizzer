package com.my.projects.quizapp.data.model

import java.io.Serializable

data class QuizModel(
    val questions: List<QuestionModel>
)

data class QuestionModel(
    val category: String,
    val type: String,
    val difficulty: String,
    val question:String,
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

