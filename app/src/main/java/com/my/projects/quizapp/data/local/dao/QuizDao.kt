package com.my.projects.quizapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.my.projects.quizapp.data.local.model.AnswerEntity
import com.my.projects.quizapp.data.local.model.QuestionEntity
import com.my.projects.quizapp.data.local.model.QuizEntity
import com.my.projects.quizapp.data.local.model.relations.QuizWithQuestionsAndAnswers
import org.threeten.bp.LocalDate

@Dao
interface QuizDao {

    @Insert
    suspend fun insertQuiz(quizEntity: QuizEntity): Long

    @Update
    suspend fun updateQuiz(quizEntity: QuizEntity)

    @Delete
    suspend fun deleteQuiz(quizEntity: QuizEntity)

    @Query("DELETE FROM Quiz WHERE id=:quizID")
    suspend fun deleteQuiz(quizID: Long)

    @Insert
    suspend fun insertQuestion(questionEntity: QuestionEntity): Long

    @Insert
    suspend fun insertAnswer(answerEntity: AnswerEntity)

    @Transaction
    @Query("SELECT * FROM Quiz WHERE id = :quizID")
    suspend fun findQuizById(quizID: Long): QuizWithQuestionsAndAnswers

    @Transaction
    @Query("SELECT * FROM Quiz ORDER BY date DESC")
    fun findAll(): LiveData<List<QuizWithQuestionsAndAnswers>>

    @Transaction
    @Query("SELECT * FROM Quiz WHERE date = :saveDate")
    fun getQuizzesByDate(saveDate: LocalDate): LiveData<List<QuizWithQuestionsAndAnswers>>

    @Transaction
    @Query("SELECT * FROM Quiz WHERE category = :categoryID")
    fun getQuizzesByCategory(categoryID: Int): LiveData<List<QuizWithQuestionsAndAnswers>>

    @Transaction
    @Query("SELECT * FROM Quiz WHERE category IS :categoryID  AND date IS :saveDate")
    fun getQuizzesByDateAndCategory(
        categoryID: Int?,
        saveDate: LocalDate?
    ): LiveData<List<QuizWithQuestionsAndAnswers>>

    @Transaction
    @Query("DELETE FROM Quiz")
    suspend fun deleteAll()
}