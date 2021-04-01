package com.devkazonovic.projects.quizzer.domain.model

import com.devkazonovic.projects.quizzer.data.local.model.relations.QuestionWithAnswers
import org.threeten.bp.LocalDate


data class HistoryQuiz(
    val id: Long,
    val title: String,
    val score: Int,
    val date: LocalDate,
    val category: Int,
    val questions: List<QuestionWithAnswers>
)
