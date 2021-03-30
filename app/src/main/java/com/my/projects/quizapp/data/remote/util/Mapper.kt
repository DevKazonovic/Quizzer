package com.my.projects.quizapp.data.remote.util

import com.my.projects.quizapp.data.remote.response.QuizResponse
import com.my.projects.quizapp.domain.model.Answer
import com.my.projects.quizapp.domain.model.Question
import com.my.projects.quizapp.util.converters.Converters

fun QuizResponse.toQuestionModel(): List<Question> {
    return results.map {
        Question(
            category = it.category,
            type = it.type,
            difficulty = it.difficulty,
            question = Converters.htmlToString(it.question),
            answers = getAnswers(it.correct_answer, it.incorrect_answers)
        )
    }
}

fun getAnswers(correct_answer: String, incorrect_answers: List<String>): List<Answer> {
    val answers: MutableList<Answer> = mutableListOf()
    //Add Correct Answer
    answers.add(Answer(0, Converters.htmlToString(correct_answer), true))

    //Add Incorrect Answers
    var i = 0
    answers.addAll(incorrect_answers.map {
        i++
        Answer(
            i,
            answer = Converters.htmlToString(it),
            isCorrect = false
        )

    })
    return answers
}