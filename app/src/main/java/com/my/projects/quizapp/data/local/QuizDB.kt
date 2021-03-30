package com.my.projects.quizapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.my.projects.quizapp.data.local.dao.QuizDao
import com.my.projects.quizapp.data.local.model.AnswerEntity
import com.my.projects.quizapp.data.local.model.QuestionEntity
import com.my.projects.quizapp.data.local.model.QuizEntity
import com.my.projects.quizapp.data.local.util.ObjectConverters

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