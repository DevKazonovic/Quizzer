package com.my.projects.quizapp.presentation.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.my.projects.quizapp.data.model.QuizSetting
import java.lang.IllegalArgumentException

class QuizViewModelFactory (private val setting: QuizSetting): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)){
            return QuizViewModel(setting) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}