package com.my.projects.quizapp.presentation.quiz.util

import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.model.CategoryModel

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
            CategoryModel(9, "General Knowledge", R.drawable.ic_gk),
            CategoryModel(10, "Books", R.drawable.ic_books),
            CategoryModel(23, "History", R.drawable.ic_history),
            CategoryModel(17, "Science & Nature", R.drawable.ic_science),
            CategoryModel(22, "Geography", R.drawable.ic_geography),
            CategoryModel(24, "Politics", R.drawable.ic_politics),
            CategoryModel(11, "Film", R.drawable.ic_film),
            CategoryModel(12, "Music", R.drawable.ic_music),
            CategoryModel(21, "Sport", R.drawable.ic_sport),
            CategoryModel(26, "Celebrities", R.drawable.ic_celebrity),
            CategoryModel(27, "Animals", R.drawable.ic_animals)
        )
    }
}