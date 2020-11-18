package com.my.projects.quizapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.my.projects.quizapp.data.db.dao.QuizDao
import com.my.projects.quizapp.data.db.entity.Answer
import com.my.projects.quizapp.data.db.entity.Question
import com.my.projects.quizapp.data.db.entity.Quiz
import com.my.projects.quizapp.util.converters.Converters

@Database(entities = [Quiz::class,Question::class,Answer::class], version = 5)
@TypeConverters(Converters::class)
abstract class QuizDB : RoomDatabase(){
    abstract val quizDao:QuizDao

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