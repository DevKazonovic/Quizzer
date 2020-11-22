package com.my.projects.quizapp.data.local.repository

import com.my.projects.quizapp.data.local.QuizDB
import com.my.projects.quizapp.data.local.entity.Quiz
import com.my.projects.quizapp.data.local.entity.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.model.AnswerModel
import com.my.projects.quizapp.model.QuestionModel
import com.my.projects.quizapp.model.asAnswerEntity
import com.my.projects.quizapp.model.asQuestionEntity

class QuizRepositoryImpl(
    private val database: QuizDB
) : QuizRepository {

    override suspend fun saveQuiz(
        quiz: Quiz,
        questions: List<QuestionModel>,
        _userAnswers: Map<Int, AnswerModel>
    ) {
        val quizId = database.quizDao.insertQuiz(quiz)
        if (!questions.isNullOrEmpty()) {
            for (i in questions.indices) {
                val questionID =
                    database.quizDao.insertQuestion(questions[i].asQuestionEntity(quizId))
                val answers = questions[i].answers
                for (j in answers.indices) {
                    if (answers[j].answer == _userAnswers[i]?.answer) {
                        database.quizDao.insertAnswer(
                            answers[j].asAnswerEntity(
                                questionID, true
                            )
                        )
                    } else {
                        database.quizDao.insertAnswer(
                            answers[j].asAnswerEntity(
                                questionID, false
                            )
                        )
                    }
                }
            }
        }
    }

    override suspend fun findAll(): List<QuizWithQuestionsAndAnswers> = database.quizDao.findAll()
    override suspend fun deleteAll() = database.quizDao.deleteAll()

}