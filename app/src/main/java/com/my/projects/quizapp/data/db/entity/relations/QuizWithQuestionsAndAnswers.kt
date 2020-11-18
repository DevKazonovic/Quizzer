package com.my.projects.quizapp.data.db.entity.relations

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.my.projects.quizapp.data.db.entity.Question
import com.my.projects.quizapp.data.db.entity.Quiz
import com.my.projects.quizapp.presentation.ui.adapter.QuizParent

data class QuizWithQuestionsAndAnswers(
    @Embedded val quiz: Quiz,
    @Relation(entity=Question::class, parentColumn = "id", entityColumn = "quizID")
    val questions:List<QuestionWithAnswers>,

)


fun QuizWithQuestionsAndAnswers.asQuiz(isExpanded:Boolean):QuizParent{
    return QuizParent(
        this.quiz,
        this.questions,
        isExpanded
    )
}