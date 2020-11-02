package com.my.projects.quizapp.data.model

import android.text.Html

data class QuizModel(
    val category:String,
    val type:String,
    val difficulty:String,
    val question:String,
    val answers: List<Answer>
){
    fun question():String= if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(question, Html.FROM_HTML_MODE_LEGACY, null, null).toString()
    } else {
        Html.fromHtml(question, null, null).toString()
    }

}


data class Answer(
    val answer: String,
    val isCorrect:Boolean
)


data class QuizSetting(
    val amount: Int,
    val category:Int?,
    val type:String?,
    val difficulty:String?,
)