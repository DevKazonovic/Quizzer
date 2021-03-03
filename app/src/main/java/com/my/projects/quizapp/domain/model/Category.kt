package com.my.projects.quizapp.domain.model

import java.io.Serializable

data class Category(
    val id: Int,
    val name: String,
    val icon: Int
) : Serializable