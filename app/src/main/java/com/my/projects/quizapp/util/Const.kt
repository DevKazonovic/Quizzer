package com.my.projects.quizapp.util

import com.my.projects.quizapp.R
import com.my.projects.quizapp.model.CategoryModel

class Const {
    companion object {
        const val KEY_CATEGORY = "Quiz Category"
        const val KEY_QUIZ_SETTING = "KEY AMOUNT"
        const val KEY_QUIZ = "QUIZ"
        val DIFFICULTIES =
            mapOf("Any Types" to "", "Easy" to "easy", "Medium" to "medium", "Hard" to "hard")
        val TYPES =
            mapOf("Any Types" to "", "Multiple Choice" to "multiple", "True/False" to "boolean")
        val cats = listOf(
            CategoryModel(9, "General Knowledge", R.drawable.ic_gk),
            CategoryModel(23, "History", R.drawable.ic_history),
            CategoryModel(24, "Politics", R.drawable.ic_politics),
            CategoryModel(21, "Sport", R.drawable.ic_sport),
            CategoryModel(26, "Celebrities", R.drawable.ic_celebrity),
            CategoryModel(27, "Animals", R.drawable.ic_animals)
        )


    }
}