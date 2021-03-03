package com.my.projects.quizapp.domain.model

import java.io.Serializable

data class QuizSetting(
    val amount: Int,
    val category: Int? = -1,
    val type: String? = "",
    val difficulty: String? = "",
) : Serializable