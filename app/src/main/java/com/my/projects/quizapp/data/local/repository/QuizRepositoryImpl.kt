package com.my.projects.quizapp.data.local.repository

import com.my.projects.quizapp.data.local.QuizDB
import com.my.projects.quizapp.data.local.entity.Quiz
import com.my.projects.quizapp.data.local.entity.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.data.model.AnswerModel
import com.my.projects.quizapp.data.model.QuestionModel
import com.my.projects.quizapp.data.model.asAnswerEntity
import com.my.projects.quizapp.data.model.asQuestionEntity

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
}

class QuizRepositoryImpl(
    private val database: QuizDB
) : IQuizRepository {

    override suspend fun saveQuiz(
        quiz: Quiz,
        questions: List<QuestionModel>,
        userAnswers: Map<Int, AnswerModel>
    ) {
        val quizId = database.quizDao.insertQuiz(quiz)
        val questionsIDs = saveQuizQuestion(quizId, questions)
        for (i in 0..questionsIDs.size) {
            saveQuestionAnswers(questionsIDs[i], questions[i].answers, userAnswers[i] ?: error(""))
        }
    }

    override suspend fun updateQuiz(quiz: Quiz) {
        database.quizDao.updateQuiz(quiz)
    }

    override suspend fun deleteQuiz(quiz: Quiz) {
        database.quizDao.deleteQuiz(quiz)
    }

    override suspend fun findAll(): List<QuizWithQuestionsAndAnswers> = database.quizDao.findAll()

    override suspend fun deleteAll() = database.quizDao.deleteAll()

    private suspend fun saveQuizQuestion(
        quizId: Long,
        questions: List<QuestionModel>
    ): MutableList<Long> {
        val questionsIds = mutableListOf<Long>()
        if (!questions.isNullOrEmpty()) {
            for (i in questions.indices) {
                val questionID =
                    database.quizDao.insertQuestion(questions[i].asQuestionEntity(quizId))
                questionsIds.add(questionID)
            }
        }

        return questionsIds
    }

    private suspend fun saveQuestionAnswers(
        questionId: Long,
        answers: List<AnswerModel>,
        userAnswer: AnswerModel
    ) {
        for (j in answers.indices) {
            database.quizDao.insertAnswer(
                answers[j].asAnswerEntity(
                    questionId
                )
            )
        }
        database.quizDao.insertAnswer(
            userAnswer.asAnswerEntity(questionId)
        )
    }

}