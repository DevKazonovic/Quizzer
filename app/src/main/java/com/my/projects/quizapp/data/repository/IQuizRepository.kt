package com.my.projects.quizapp.data.repository

import androidx.lifecycle.LiveData
import com.my.projects.quizapp.data.local.entity.Quiz
import com.my.projects.quizapp.data.local.entity.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.data.model.AnswerModel
import com.my.projects.quizapp.data.model.QuestionModel
import com.my.projects.quizapp.data.model.QuizSetting
import com.my.projects.quizapp.data.remote.QuizResponse
import java.util.*

interface IQuizRepository {
    suspend fun saveQuiz(
        quiz: Quiz,
        questions: List<QuestionModel>,
        userAnswers: Map<Int, AnswerModel>
    )

    suspend fun updateQuiz(quiz: Quiz)

    suspend fun deleteQuiz(quiz: Quiz)

    fun findAll(): LiveData<List<QuizWithQuestionsAndAnswers>>

    fun getQuizzesByDate(saveDate: Date): LiveData<List<QuizWithQuestionsAndAnswers>>
    fun getQuizzesByCategory(categoryID: Int): LiveData<List<QuizWithQuestionsAndAnswers>>
    fun getFilteredQuizzes(
        categoryID: Int?,
        saveDate: Date?
    ): LiveData<List<QuizWithQuestionsAndAnswers>>


    suspend fun deleteAll()

    suspend fun getQuiz(quizSetting: QuizSetting): QuizResponse
    suspend fun findQuizById(quizID: Long): QuizWithQuestionsAndAnswers
}