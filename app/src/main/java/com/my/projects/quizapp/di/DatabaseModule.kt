package com.my.projects.quizapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.my.projects.quizapp.data.local.QuizDB
import com.my.projects.quizapp.data.local.dao.QuizDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideQuizDB(context: Context): QuizDB{
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
    fun provideDao(db: QuizDB): QuizDao{
        return db.quizDao
    }
}