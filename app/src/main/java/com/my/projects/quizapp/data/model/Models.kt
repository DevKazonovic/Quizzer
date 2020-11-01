package com.my.projects.quizapp.data.model

data class QuizModel(
    val category:String,
    val type:String,
    val difficulty:String,
    val question:String,
    val answers: List<Answer>
)


data class Answer(
    val answer: String,
    val isCorrect:Boolean
)


data class QuizSetting(
    val amount: Int,
    val category:Int?,
    val type:String?,
    val difficulty:String?,
)