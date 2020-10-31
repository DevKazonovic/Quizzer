package com.my.projects.quizapp.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuizResponse(@field:Json(name = "response_code") val code:Int, val results:List<Quiz>)

@JsonClass(generateAdapter = true)
data class Quiz(val category:String,
           val type:String,
           val difficulty:String,
           val question:String,
           val correct_answer:String,
           val incorrect_answers: List<String>
)
