package com.my.projects.quizapp.data.local.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.my.projects.quizapp.data.local.model.QuestionEntity
import com.my.projects.quizapp.data.local.model.QuizEntity
import java.io.Serializable

data class QuizWithQuestionsAndAnswers(
    @Embedded val quizEntity: QuizEntity,
    @Relation(entity = QuestionEntity::class, parentColumn = "id", entityColumn = "quizID")
    val questions: List<QuestionWithAnswers>,

    ) : Serializable


