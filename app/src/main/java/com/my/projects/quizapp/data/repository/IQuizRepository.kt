package com.my.projects.quizapp.data.repository

import com.my.projects.quizapp.data.local.entity.Quiz
import com.my.projects.quizapp.data.local.entity.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.data.model.AnswerModel
import com.my.projects.quizapp.data.model.QuestionModel
import com.my.projects.quizapp.data.model.QuizSetting
import com.my.projects.quizapp.data.remote.QuizResponse

interface IQuizRepository {
    suspend fun saveQuiz(
        quiz: Quiz,
        questions: List<QuestionModel>,
        userAnswers: Map<Int, AnswerModel>
    )

    suspend fun updateQuiz(quiz: Quiz)

    suspend fun deleteQuiz(quiz: Quiz)

    suspend fun findAll(): List<QuizWithQuestionsAndAnswers>

    suspend fun deleteAll()

    suspend fun getQuiz(quizSetting: QuizSetting): QuizResponse
}