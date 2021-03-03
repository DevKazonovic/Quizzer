package com.my.projects.quizapp.data.repository

import androidx.lifecycle.LiveData
import com.my.projects.quizapp.data.local.QuizDB
import com.my.projects.quizapp.data.local.model.QuizEntity
import com.my.projects.quizapp.data.local.model.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.domain.model.Answer
import com.my.projects.quizapp.domain.model.Question
import com.my.projects.quizapp.domain.model.asAnswerEntity
import com.my.projects.quizapp.domain.model.asQuestionEntity
import java.util.*
import javax.inject.Inject


class QuizLocalRepository @Inject constructor(
    private val database: QuizDB
) {

    suspend fun saveQuiz(
        quizEntity: QuizEntity,
        questions: List<Question>,
        userAnswers: Map<Int, Answer>
    ) {
        val quizId = database.quizDao.insertQuiz(quizEntity)
        saveQuizQuestion(quizId, questions, userAnswers)
    }

    private suspend fun saveQuizQuestion(
        quizId: Long,
        questions: List<Question>,
        userAnswers: Map<Int, Answer>
    ) {
        if (!questions.isNullOrEmpty()) {
            for (i in questions.indices) {
                val questionID =
                    database.quizDao.insertQuestion(questions[i].asQuestionEntity(quizId))
                saveQuestionAnswers(questionID, questions[i].answers, userAnswers[i])
            }
        }
    }

    private suspend fun saveQuestionAnswers(
        questionId: Long,
        answers: List<Answer>,
        userAnswer: Answer?
    ) {
        for (j in answers.indices) {
            if (userAnswer != null && answers[j].id == userAnswer.id)
                database.quizDao.insertAnswer(userAnswer.asAnswerEntity(questionId))
            else
                database.quizDao.insertAnswer(answers[j].asAnswerEntity(questionId))
        }

    }


    suspend fun updateQuiz(
        quizEntity: QuizEntity
    ) {
        database.quizDao.updateQuiz(quizEntity)
    }

    suspend fun deleteQuiz(
        quizEntity: QuizEntity
    ) {
        database.quizDao.deleteQuiz(quizEntity)
    }

    suspend fun findQuizById(quizID: Long): QuizWithQuestionsAndAnswers {
        return database.quizDao.findQuizById(quizID)
    }


    fun findAll(): LiveData<List<QuizWithQuestionsAndAnswers>> {
        return database.quizDao.findAll()
    }

    fun getQuizzesByDate(
        saveDate: Date
    ): LiveData<List<QuizWithQuestionsAndAnswers>> {
        return database.quizDao.getQuizzesByDate(saveDate)
    }

    fun getQuizzesByCategory(
        categoryID: Int
    ): LiveData<List<QuizWithQuestionsAndAnswers>> {
        return database.quizDao.getQuizzesByCategory(categoryID)
    }

    fun getFilteredQuizzes(
        categoryID: Int?,
        saveDate: Date?
    ): LiveData<List<QuizWithQuestionsAndAnswers>> {

        return if (categoryID != null && saveDate == null) {
            database.quizDao.getQuizzesByCategory(categoryID)
        } else if (saveDate != null && categoryID == null) {
            database.quizDao.getQuizzesByDate(saveDate)
        } else if (saveDate != null && categoryID != null) {
            database.quizDao.getQuizzesByDateAndCategory(categoryID, saveDate)
        } else database.quizDao.findAll()

    }

    suspend fun deleteAll() = database.quizDao.deleteAll()


}