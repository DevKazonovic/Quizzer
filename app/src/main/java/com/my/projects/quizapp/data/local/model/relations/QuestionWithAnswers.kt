package com.my.projects.quizapp.data.local.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.my.projects.quizapp.data.local.model.AnswerEntity
import com.my.projects.quizapp.data.local.model.QuestionEntity
import java.io.Serializable

data class QuestionWithAnswers(
    @Embedded
    val questionEntity: QuestionEntity,
    @Relation(parentColumn = "id", entityColumn = "questionID")
    val answerEntities: List<AnswerEntity>
) : Serializable