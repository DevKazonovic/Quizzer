package com.my.projects.quizapp.data.local.dao

import androidx.room.*
import com.my.projects.quizapp.data.local.entity.Answer
import com.my.projects.quizapp.data.local.entity.Question
import com.my.projects.quizapp.data.local.entity.Quiz
import com.my.projects.quizapp.data.local.entity.relations.QuizWithQuestionsAndAnswers

@Dao
interface QuizDao {

    @Insert
    suspend fun insertQuiz(quiz: Quiz): Long

    @Update
    suspend fun updateQuiz(quiz: Quiz)

    @Delete
    suspend fun deleteQuiz(quiz: Quiz)

    @Insert
    suspend fun insertQuestion(question: Question): Long

    @Insert
    suspend fun insertAnswer(answer: Answer)

    @Transaction
    @Query("SELECT * FROM QUIZ")
    suspend fun findAll(): List<QuizWithQuestionsAndAnswers>

    @Transaction
    @Query("DELETE FROM QUIZ")
    suspend fun deleteAll()
}