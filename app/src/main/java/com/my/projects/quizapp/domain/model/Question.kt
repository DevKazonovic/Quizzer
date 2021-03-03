package com.my.projects.quizapp.domain.model

import com.my.projects.quizapp.data.local.model.QuestionEntity

data class Question(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    val answers: List<Answer>
)

fun Question.asQuestionEntity(quizID: Long): QuestionEntity {
    return QuestionEntity(quizID, category, type, difficulty, question)
}