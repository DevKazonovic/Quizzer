package com.devkazonovic.projects.quizzer.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuizSetting(
    val category: Int? = -1,
    val numberOfQuestions: Int,
    val type: String? = "",
    val difficulty: String? = "",
    val countDownInSeconds: Int? = 60
) : Parcelable