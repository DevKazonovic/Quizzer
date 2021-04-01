package com.devkazonovic.projects.quizzer.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class QuizJson(
    @Json(name = "category") val category: String,
    @Json(name = "type") val type: String,
    @Json(name = "difficulty") val difficulty: String,
    @Json(name = "question") val question: String,
    @Json(name = "correct_answer") val correct_answer: String,
    @Json(name = "incorrect_answers") val incorrect_answers: List<String>
)



