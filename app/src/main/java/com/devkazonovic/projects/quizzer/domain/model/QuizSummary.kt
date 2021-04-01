package com.devkazonovic.projects.quizzer.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuizSummary(
    var quizID: Long,
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    val answers: List<QuizAnswersSummary>
) : Parcelable