package com.my.projects.quizapp.data.repository

import com.my.projects.quizapp.data.remote.response.QuizResponse
import com.my.projects.quizapp.data.remote.service.QuizService
import com.my.projects.quizapp.domain.model.QuizSetting
import javax.inject.Inject

class QuizRemoteRepository @Inject constructor(private val quizService: QuizService) {
    suspend fun getQuiz(quizSetting: QuizSetting): QuizResponse {
        return quizService.getQuiz(
            quizSetting.amount,
            quizSetting.category,
            quizSetting.difficulty,
            quizSetting.type
        )
    }
}