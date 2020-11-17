package com.my.projects.quizapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.my.projects.quizapp.data.db.QuizDB
import com.my.projects.quizapp.data.db.dao.QuizDao
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DaoTest {
    private lateinit var dao: QuizDao
    private lateinit var db: QuizDB
    val TAG="TEST"

    @Before
    fun createDb() {
        println("Before")
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, QuizDB::class.java).build()
        println( db.toString())

        dao = db.quizDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeNoteAndReadInList() {

    }

}