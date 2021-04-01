package com.devkazonovic.projects.quizzer.data

import com.devkazonovic.projects.quizzer.R
import com.devkazonovic.projects.quizzer.domain.model.Category

object CategoriesStore {
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

    fun findCategoryById(categoryID: Int): Category {
        return cats.find { it.id == categoryID }!!
    }
}