package com.devkazonovic.projects.quizzer.data.remote.response

import com.devkazonovic.projects.quizzer.data.remote.model.QuizJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuizResponse(
    @Json(name = "response_code") val code: Int,
    @Json(name = "results") val results: List<QuizJson>
)

