package com.devkazonovic.projects.quizzer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.devkazonovic.projects.quizzer.data.local.dao.QuizDao
import com.devkazonovic.projects.quizzer.data.local.model.AnswerEntity
import com.devkazonovic.projects.quizzer.data.local.model.QuestionEntity
import com.devkazonovic.projects.quizzer.data.local.model.QuizEntity
import com.devkazonovic.projects.quizzer.data.local.util.ObjectConverters

@Database(
    entities = [
        QuizEntity::class,
        QuestionEntity::class,
        AnswerEntity::class
    ],
    version = 2
)
@TypeConverters(ObjectConverters::class)
abstract class QuizDB : RoomDatabase() {
    abstract val quizDao: QuizDao
}