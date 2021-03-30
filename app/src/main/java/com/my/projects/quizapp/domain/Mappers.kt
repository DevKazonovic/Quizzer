package com.my.projects.quizapp.domain

import com.my.projects.quizapp.data.local.model.AnswerEntity
import com.my.projects.quizapp.data.local.model.relations.QuestionWithAnswers
import com.my.projects.quizapp.data.local.model.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.domain.model.HistoryQuiz
import com.my.projects.quizapp.domain.model.QuizAnswersSummary
import com.my.projects.quizapp.domain.model.QuizSummary

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

fun QuestionWithAnswers.toQuizSummary(): QuizSummary {
    return QuizSummary(
        quizID = questionEntity.quizID,
        category = questionEntity.category,
        type = questionEntity.type,
        difficulty = questionEntity.difficulty,
        question = questionEntity.question,
        answers = answerEntities.map { it.toAnswer() }
    )
}

fun AnswerEntity.toAnswer(): QuizAnswersSummary {
    return QuizAnswersSummary(
        answer = answer,
        isCorrect = isCorrect,
        isUser = isUser
    )
}