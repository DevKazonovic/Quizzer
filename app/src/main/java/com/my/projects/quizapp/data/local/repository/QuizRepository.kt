package com.my.projects.quizapp.data.local.repository

import com.my.projects.quizapp.data.local.entity.Quiz
import com.my.projects.quizapp.data.local.entity.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.data.model.AnswerModel
import com.my.projects.quizapp.data.model.QuestionModel

interface QuizRepository {
    suspend fun saveQuiz(
        quiz: Quiz,
        questions: List<QuestionModel>,
        _userAnswers: Map<Int, AnswerModel>
    )

    suspend fun findAll(): List<QuizWithQuestionsAndAnswers>
    suspend fun deleteAll()
}