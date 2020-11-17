package com.my.projects.quizapp.data.db.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.my.projects.quizapp.data.db.entity.Question
import com.my.projects.quizapp.data.db.entity.Quiz

data class QuizWithQuestionsAndAnswers(
    @Embedded val quiz: Quiz,
    @Relation(entity=Question::class, parentColumn = "id", entityColumn = "quizID")
    val questions:List<QuestionWithAnswers>


)