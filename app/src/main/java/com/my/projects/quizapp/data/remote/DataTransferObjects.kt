package com.my.projects.quizapp.data.remote

import com.my.projects.quizapp.data.model.Answer
import com.my.projects.quizapp.data.model.QuizModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuizResponse(@field:Json(name = "response_code") val code: Int, val results: List<Quiz>)

@JsonClass(generateAdapter = true)
data class Quiz(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
)

fun QuizResponse.asQuizModel(): List<QuizModel> {
    return results.map {
        QuizModel(
            category = it.category,
            type = it.type,
            difficulty = it.difficulty,
            question = it.question,
            answers = getAnswers(it.correct_answer, it.incorrect_answers)
        )
    }
}

fun getAnswers(correct_answer: String, incorrect_answers: List<String>): List<Answer> {
    val answers: MutableList<Answer> = mutableListOf()

    //Add Correct Answer
    answers.add(Answer(correct_answer, true))

    //Add Incorrect Answers
    answers.addAll(incorrect_answers.map {
        Answer(
            answer = it,
            isCorrect = false
        )
    })

    return answers
}