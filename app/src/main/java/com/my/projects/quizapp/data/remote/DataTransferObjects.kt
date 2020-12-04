package com.my.projects.quizapp.data.remote

import com.my.projects.quizapp.data.model.AnswerModel
import com.my.projects.quizapp.data.model.QuestionModel
import com.my.projects.quizapp.util.converters.Converters
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuizResponse(
    @field:Json(name = "response_code") val code: Int,
    val results: List<QuizDto>
)

@JsonClass(generateAdapter = true)
data class QuizDto(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
)

fun QuizResponse.asQuestionModel(): List<QuestionModel> {
    return results.map {
        QuestionModel(
            category = it.category,
            type = it.type,
            difficulty = it.difficulty,
            question = Converters.htmlToString(it.question),
            answers = getAnswers(it.correct_answer, it.incorrect_answers)
        )
    }
}

fun getAnswers(correct_answer: String, incorrect_answers: List<String>): List<AnswerModel> {
    val answers: MutableList<AnswerModel> = mutableListOf()
    //Add Correct Answer
    answers.add(AnswerModel(0, Converters.htmlToString(correct_answer), true))

    //Add Incorrect Answers
    var i = 0
    answers.addAll(incorrect_answers.map {
        i++
        AnswerModel(
            i,
            answer = Converters.htmlToString(it),
            isCorrect = false
        )

    })
    return answers
}