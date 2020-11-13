package com.my.projects.quizapp.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.my.projects.quizapp.data.db.entity.Answer
import com.my.projects.quizapp.data.db.entity.Question
import com.my.projects.quizapp.data.db.entity.Quiz

@Dao
interface QuizDao{

    @Insert
    suspend fun insertQuiz(quiz: Quiz):Long

    @Insert
    suspend fun insertQuestion(question:Question):Long

    @Insert
    suspend fun insertAnswer(answer: Answer)

    @Insert
    suspend fun insertQuestions(questions:List<Question>):List<Long>

    @Query("SELECT * FROM QUIZ")
    fun findAll(): LiveData<Quiz>
}