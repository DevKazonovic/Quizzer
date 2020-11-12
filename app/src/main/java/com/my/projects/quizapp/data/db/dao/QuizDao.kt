package com.my.projects.quizapp.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.my.projects.quizapp.data.db.entity.Quiz

@Dao
interface QuizDao{

    @Insert
    fun insertQuiz(quizDao: QuizDao)

    @Query("SELECT * FROM QUIZ")
    fun finAll(): LiveData<Quiz>
}