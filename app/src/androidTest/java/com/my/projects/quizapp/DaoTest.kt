package com.my.projects.quizapp

import android.content.Context
import android.nfc.Tag
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.my.projects.quizapp.data.db.QuizDB
import com.my.projects.quizapp.data.db.dao.QuizDao
import com.my.projects.quizapp.data.db.entity.Quiz
import com.my.projects.quizapp.data.model.Answer
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

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