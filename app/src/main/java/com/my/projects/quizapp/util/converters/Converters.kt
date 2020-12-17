package com.my.projects.quizapp.util.converters

import android.annotation.SuppressLint
import androidx.core.text.HtmlCompat
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class Converters {



    @SuppressLint("SimpleDateFormat")
    companion object {
        fun htmlToString(html: String): String {
            return HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        }

        fun dateToString(time: Long): String {
            val formatter = SimpleDateFormat("dd/MM/yyyy, HH:mm")
            return formatter.format(Date(time))
        }

        fun noTimeDateToString(date:Long):String{
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            return formatter.format(Date(date))
        }
    }
}