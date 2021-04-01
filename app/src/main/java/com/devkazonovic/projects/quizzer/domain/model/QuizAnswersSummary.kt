package com.devkazonovic.projects.quizzer.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuizAnswersSummary(
    val answer: String,
    val isCorrect: Boolean,
    val isUser: Boolean = false
) : Parcelable