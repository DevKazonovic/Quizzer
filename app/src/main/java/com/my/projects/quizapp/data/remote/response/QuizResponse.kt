package com.my.projects.quizapp.data.remote.response

import com.my.projects.quizapp.data.remote.model.QuizJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuizResponse(
    @field:Json(name = "response_code") val code: Int,
    val results: List<QuizJson>
)

