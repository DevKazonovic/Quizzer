package com.devkazonovic.projects.quizzer.domain.model

sealed class HistoryItem {
    abstract val id: Long

    data class Item(val item: HistoryQuiz) : HistoryItem() {
        override val id: Long get() = item.id
    }

    data class Header(val header: String) : HistoryItem() {
        override val id: Long get() = Long.MIN_VALUE
    }


}
