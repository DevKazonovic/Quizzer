package com.my.projects.quizapp.util

import com.my.projects.quizapp.domain.enums.Difficulty
import com.my.projects.quizapp.domain.enums.Type

class QuizUtil {
    companion object {

        val DIFFICULTIES =
            mapOf(
                Difficulty.ANY to "",
                Difficulty.EASY to "easy",
                Difficulty.MEDIUM to "medium",
                Difficulty.HARD to "hard"
            )

        val TYPES =
            mapOf(
                Type.ANY to "",
                Type.MULTIPLECHOICE to "multiple",
                Type.TRUEFALSE to "boolean"
            )


        val COUNTDOWNPERIODS = listOf(
            10,20,30,40,50,60
        )
    }
}