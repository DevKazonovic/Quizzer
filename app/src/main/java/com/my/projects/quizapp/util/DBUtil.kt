package com.my.projects.quizapp.util

import com.my.projects.quizapp.data.CategoriesStore.cats
import java.util.*

class DBUtil {
    companion object {
        fun generateRandomTitle(categoryID: Int, score: Int): String {
            val uuid = UUID.randomUUID()
            val category = cats.find { category -> category.id == categoryID }
            return "${category?.name ?: "no-cat"}-quiz-${category?.id ?: 0}-${
                uuid.toString().subSequence(0, 6)
            }-$score"
        }
    }
}