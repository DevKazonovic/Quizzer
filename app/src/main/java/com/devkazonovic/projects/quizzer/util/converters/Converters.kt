package com.devkazonovic.projects.quizzer.util.converters

import android.annotation.SuppressLint
import androidx.core.text.HtmlCompat
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

class Converters {


    @SuppressLint("SimpleDateFormat")
    companion object {
        private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

        fun htmlToString(html: String): String {
            return HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        }

        fun dateToString(time: Long): String {
            val formatter = SimpleDateFormat("dd/MM/yyyy, HH:mm")
            return formatter.format(Date(time))
        }

        fun noTimeDateToString(date: Long): String {
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            return formatter.format(Date(date))
        }

        fun stringToLocalDate(value: String): LocalDate = formatter.parse(value, LocalDate::from)

        fun scoreIntToPers(score: Int, questions: Int): Int {
            return ((score.toDouble() / questions) * 100).toInt()
        }
    }
}