package com.my.projects.quizapp.data.db.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.my.projects.quizapp.data.db.entity.Answer
import com.my.projects.quizapp.data.db.entity.Question

data class QuestionWithAnswers (
    @Embedded
    val question: Question,
    @Relation(parentColumn = "id", entityColumn = "questionID")
    val answers:List<Answer>
)