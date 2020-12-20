package com.my.projects.quizapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.my.projects.quizapp.data.local.entity.Answer
import com.my.projects.quizapp.data.local.entity.Question
import com.my.projects.quizapp.data.local.entity.Quiz
import com.my.projects.quizapp.data.local.entity.relations.QuizWithQuestionsAndAnswers
import java.util.*

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
    @Query("SELECT * FROM QUIZ WHERE id = :quizID")
    suspend fun findQuizById(quizID: Long): QuizWithQuestionsAndAnswers

    @Transaction
    @Query("SELECT * FROM QUIZ ORDER BY date DESC")
    fun findAll(): LiveData<List<QuizWithQuestionsAndAnswers>>

    @Transaction
    @Query("SELECT * FROM QUIZ WHERE date = :saveDate")
    fun getQuizzesByDate(saveDate: Date): LiveData<List<QuizWithQuestionsAndAnswers>>

    @Transaction
    @Query("SELECT * FROM QUIZ WHERE category = :categoryID")
    fun getQuizzesByCategory(categoryID: Int): LiveData<List<QuizWithQuestionsAndAnswers>>

    @Transaction
    @Query("SELECT * FROM QUIZ WHERE category IS :categoryID  AND date IS :saveDate")
    fun getQuizzesByDateAndCategory(
        categoryID: Int?,
        saveDate: Date?
    ): LiveData<List<QuizWithQuestionsAndAnswers>>

    @Transaction
    @Query("DELETE FROM QUIZ")
    suspend fun deleteAll()
}