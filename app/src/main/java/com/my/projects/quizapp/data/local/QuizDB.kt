package com.my.projects.quizapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.my.projects.quizapp.data.local.dao.QuizDao
import com.my.projects.quizapp.data.local.entity.Answer
import com.my.projects.quizapp.data.local.entity.Question
import com.my.projects.quizapp.data.local.entity.Quiz
import com.my.projects.quizapp.data.local.util.ObjectConverters

@Database(entities = [Quiz::class, Question::class, Answer::class], version = 11)
@TypeConverters(ObjectConverters::class)
abstract class QuizDB : RoomDatabase() {
    abstract val quizDao: QuizDao

    companion object {
        @Volatile
        private var INSTANCE: QuizDB? = null
        fun getInstance(context: Context): QuizDB {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        QuizDB::class.java,
                        "quiz_db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}