package com.my.projects.quizapp.domain.model

import com.my.projects.quizapp.data.local.model.relations.QuestionWithAnswers
import org.threeten.bp.LocalDate


data class HistoryQuiz(
    val id: Long,
    val title: String,
    val score: Int,
    val date: LocalDate,
    val category: Int,
    val questions: List<QuestionWithAnswers>
)
