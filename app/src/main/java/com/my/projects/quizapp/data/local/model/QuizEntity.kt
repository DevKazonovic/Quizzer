package com.my.projects.quizapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDate
import java.io.Serializable

@Entity(tableName = "Quiz")
data class QuizEntity(
    val title: String,
    val score: Int,
    val date: LocalDate,
    val category: Int
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

