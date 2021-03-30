package com.my.projects.quizapp.domain.model

data class Question(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    val answers: List<Answer>
)

