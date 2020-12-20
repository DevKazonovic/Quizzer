package com.my.projects.quizapp.presentation.quiz

import com.my.projects.quizapp.presentation.quiz.util.Const
import java.util.*

class DataBaseUtil {
    companion object {
        fun generateRandomTitle(categoryID: Int, score: Int): String {
            val uuid = UUID.randomUUID()
            val category = Const.cats.find { category -> category.id == categoryID }
            return "${category?.name ?: "no-cat"}-quiz-${category?.id ?: 0}-${
                uuid.toString().subSequence(0, 6)
            }-$score"
        }
    }
}