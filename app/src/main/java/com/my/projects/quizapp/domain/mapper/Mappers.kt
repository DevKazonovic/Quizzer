package com.my.projects.quizapp.domain.mapper

import com.my.projects.quizapp.data.local.model.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.domain.model.HistoryQuiz

fun QuizWithQuestionsAndAnswers.toHistoryQuiz(): HistoryQuiz {
    return HistoryQuiz(
        this.quizEntity.id,
        this.quizEntity.title,
        this.quizEntity.score,
        this.quizEntity.date,
        this.quizEntity.category,
        this.questions
    )
}