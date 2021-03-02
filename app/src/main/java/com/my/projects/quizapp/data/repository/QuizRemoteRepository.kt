package com.my.projects.quizapp.data.repository

import com.my.projects.quizapp.data.model.QuizSetting
import com.my.projects.quizapp.data.remote.QuizResponse
import com.my.projects.quizapp.data.remote.QuizService
import javax.inject.Inject

class QuizRemoteRepository @Inject constructor(private val quizService: QuizService){
    suspend fun getQuiz(quizSetting: QuizSetting): QuizResponse {
        return quizService.getQuiz(
            quizSetting.amount,
            quizSetting.category,
            quizSetting.difficulty,
            quizSetting.type
        )
    }
}