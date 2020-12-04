package com.my.projects.quizapp.data.local.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.my.projects.quizapp.data.local.entity.Question
import com.my.projects.quizapp.data.local.entity.Quiz
import java.io.Serializable

data class QuizWithQuestionsAndAnswers(
    @Embedded val quiz: Quiz,
    @Relation(entity = Question::class, parentColumn = "id", entityColumn = "quizID")
    val questions: List<QuestionWithAnswers>,

) : Serializable


