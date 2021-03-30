package com.my.projects.quizapp.data.repository

import androidx.lifecycle.LiveData
import com.my.projects.quizapp.data.local.QuizDB
import com.my.projects.quizapp.data.local.asAnswerEntity
import com.my.projects.quizapp.data.local.asQuestionEntity
import com.my.projects.quizapp.data.local.model.QuizEntity
import com.my.projects.quizapp.data.local.model.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.domain.model.Answer
import com.my.projects.quizapp.domain.model.Question
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

    suspend fun deleteQuiz(quizID: Long) {
        database.quizDao.deleteQuiz(quizID)
    }

    suspend fun findQuizById(quizID: Long): QuizWithQuestionsAndAnswers {
        return database.quizDao.findQuizById(quizID)
    }

    fun findAll(): LiveData<List<QuizWithQuestionsAndAnswers>> {
        return database.quizDao.findAll()
    }

    suspend fun deleteAll() = database.quizDao.deleteAll()


}