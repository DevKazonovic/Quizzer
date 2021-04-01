package com.devkazonovic.projects.quizzer.data.repository

import com.devkazonovic.projects.quizzer.data.remote.response.QuizResponse
import com.devkazonovic.projects.quizzer.data.remote.service.QuizService
import com.devkazonovic.projects.quizzer.domain.model.QuizSetting
import javax.inject.Inject

class QuizRemoteRepository @Inject constructor(private val quizService: QuizService) {
    suspend fun getQuiz(quizSetting: QuizSetting): QuizResponse {
        return quizService.getQuiz(
            quizSetting.numberOfQuestions,
            quizSetting.category,
            quizSetting.difficulty,
            quizSetting.type
        )
    }
}