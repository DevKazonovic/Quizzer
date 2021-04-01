package com.devkazonovic.projects.quizzer.data.local.util

import androidx.room.TypeConverter
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

object ObjectConverters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    @JvmStatic
    fun fromTimestamp(value: String?): LocalDate? {
        return formatter.parse(value, LocalDate::from)
    }

    @TypeConverter
    @JvmStatic
    fun dateToTimestamp(date: LocalDate?): String? {
        return date?.format(formatter)
    }
}