package com.my.projects.quizapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity
data class Quiz(
    val title: String,
    val score: Int,
    val date: Date,
    val category: Int
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

