package com.my.projects.quizapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.my.projects.quizapp.data.local.model.AnswerEntity
import com.my.projects.quizapp.data.local.model.QuestionEntity
import com.my.projects.quizapp.data.local.model.QuizEntity
import com.my.projects.quizapp.data.local.model.relations.QuizWithQuestionsAndAnswers
import java.util.*

@Dao
interface QuizDao {

    @Insert
    suspend fun insertQuiz(quizEntity: QuizEntity): Long

    @Update
    suspend fun updateQuiz(quizEntity: QuizEntity)

    @Delete
    suspend fun deleteQuiz(quizEntity: QuizEntity)

    @Insert
    suspend fun insertQuestion(questionEntity: QuestionEntity): Long

    @Insert
    suspend fun insertAnswer(answerEntity: AnswerEntity)

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