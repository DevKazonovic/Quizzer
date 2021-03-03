package com.my.projects.quizapp.data.remote.model

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class QuizJson(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
)



