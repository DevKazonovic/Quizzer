package com.my.projects.quizapp.util

class Const {
    companion object {
        const val KEY_CATEGORY="Quiz Category"
        const val KEY_QUIZ_SETTING = "KEY_AMOUNT"
        val DIFFICULTIES =
            mapOf("Any Types" to "", "Easy" to "easy", "Medium" to "medium", "Hard" to "hard")
        val TYPES =
            mapOf("Any Types" to "", "Multiple Choice" to "multiple", "True/False" to "boolean")
    }
}