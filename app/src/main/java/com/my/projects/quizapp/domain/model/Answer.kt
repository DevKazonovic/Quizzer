package com.my.projects.quizapp.domain.model

import com.my.projects.quizapp.data.local.model.AnswerEntity

data class Answer(
    val id: Int,
    val answer: String,
    val isCorrect: Boolean,
    val isUser: Boolean = false
)

fun Answer.asAnswerEntity(questionID: Long): AnswerEntity {
    return AnswerEntity(questionID, answer, isCorrect, isUser)
}