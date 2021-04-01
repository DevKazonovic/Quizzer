package com.devkazonovic.projects.quizzer.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Answer(
    val id: Int,
    val answer: String,
    val isCorrect: Boolean,
    val isUser: Boolean = false
) : Parcelable

