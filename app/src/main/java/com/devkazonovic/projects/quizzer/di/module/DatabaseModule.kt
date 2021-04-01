package com.devkazonovic.projects.quizzer.di.module

import android.content.Context
import androidx.room.Room
import com.devkazonovic.projects.quizzer.data.local.QuizDB
import com.devkazonovic.projects.quizzer.data.local.dao.QuizDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideQuizDB(context: Context): QuizDB {
        return Room.databaseBuilder(
            context.applicationContext,
            QuizDB::class.java,
            "quiz_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideDao(db: QuizDB): QuizDao {
        return db.quizDao
    }
}