package com.my.projects.quizapp.data.remote.service

import com.my.projects.quizapp.data.remote.response.QuizResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface QuizService {
    @GET("api.php")
    suspend fun getQuiz(
        @Query("amount") amount: Int,
        @Query("category") category: Int? = null,
        @Query("difficulty") difficulty: String? = null,
        @Query("type") type: String? = null
    ): QuizResponse
}

