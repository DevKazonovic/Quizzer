package com.my.projects.quizapp.util.converters

import androidx.core.text.HtmlCompat
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }


    companion object {
        fun htmlToString(html: String): String {
            return HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        }

        fun dateToString(time: Long): String {
            val formatter = SimpleDateFormat("dd/MM/yyyy, HH:mm")
            return formatter.format(Date(time))
        }
    }
}