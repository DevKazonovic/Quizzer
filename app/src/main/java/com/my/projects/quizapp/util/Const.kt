package com.my.projects.quizapp.util

import com.my.projects.quizapp.R
import com.my.projects.quizapp.domain.model.Category

class Const {
    companion object {
        const val KEY_CATEGORY = "Quiz Category"
        const val KEY_QUIZ_SETTING = "KEY AMOUNT"
        const val KEY_QUIZ_ID = "QUIZ ID"

        val DIFFICULTIES =
            mapOf("Any Types" to "", "Easy" to "easy", "Medium" to "medium", "Hard" to "hard")

        val TYPES =
            mapOf("Any Types" to "", "Multiple Choice" to "multiple", "True/False" to "boolean")

        val cats = listOf(
            Category(9, "General Knowledge", R.drawable.ic_gk),
            Category(10, "Books", R.drawable.ic_books),
            Category(23, "History", R.drawable.ic_history),
            Category(17, "Science & Nature", R.drawable.ic_science),
            Category(22, "Geography", R.drawable.ic_geography),
            Category(24, "Politics", R.drawable.ic_politics),
            Category(11, "Film", R.drawable.ic_film),
            Category(12, "Music", R.drawable.ic_music),
            Category(21, "Sport", R.drawable.ic_sport),
            Category(26, "Celebrities", R.drawable.ic_celebrity),
            Category(27, "Animals", R.drawable.ic_animals)
        )
    }
}