package com.my.projects.quizapp.data.local

import com.my.projects.quizapp.data.local.model.AnswerEntity
import com.my.projects.quizapp.data.local.model.QuestionEntity
import com.my.projects.quizapp.domain.model.Answer
import com.my.projects.quizapp.domain.model.Question

fun Answer.asAnswerEntity(questionID: Long): AnswerEntity {
    return AnswerEntity(questionID, answer, isCorrect, isUser)
}

fun Question.asQuestionEntity(quizID: Long): QuestionEntity {
    return QuestionEntity(quizID, category, type, difficulty, question)
}