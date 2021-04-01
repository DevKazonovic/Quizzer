package com.devkazonovic.projects.quizzer.data.local

import com.devkazonovic.projects.quizzer.data.local.model.AnswerEntity
import com.devkazonovic.projects.quizzer.data.local.model.QuestionEntity
import com.devkazonovic.projects.quizzer.domain.model.Answer
import com.devkazonovic.projects.quizzer.domain.model.Question

fun Answer.asAnswerEntity(questionID: Long): AnswerEntity {
    return AnswerEntity(questionID, answer, isCorrect, isUser)
}

fun Question.asQuestionEntity(quizID: Long): QuestionEntity {
    return QuestionEntity(quizID, category, type, difficulty, question)
}