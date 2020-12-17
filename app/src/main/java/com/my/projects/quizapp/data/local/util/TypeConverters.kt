package com.my.projects.quizapp.data.local.util

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class ObjectConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        val formatter= SimpleDateFormat("dd/MM/yyyy")
        return if(date==null) 0 else formatter.parse(formatter.format(date))?.time
    }
}