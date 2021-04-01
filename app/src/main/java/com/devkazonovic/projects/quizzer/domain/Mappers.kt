package com.devkazonovic.projects.quizzer.domain

import com.devkazonovic.projects.quizzer.data.local.model.AnswerEntity
import com.devkazonovic.projects.quizzer.data.local.model.relations.QuestionWithAnswers
import com.devkazonovic.projects.quizzer.data.local.model.relations.QuizWithQuestionsAndAnswers
import com.devkazonovic.projects.quizzer.domain.model.HistoryQuiz
import com.devkazonovic.projects.quizzer.domain.model.QuizAnswersSummary
import com.devkazonovic.projects.quizzer.domain.model.QuizSummary

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