package com.my.projects.quizapp.presentation.history.detail

import androidx.lifecycle.*
import com.my.projects.quizapp.data.repository.QuizLocalRepository
import com.my.projects.quizapp.domain.model.HistoryQuiz
import com.my.projects.quizapp.domain.toHistoryQuiz
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class QuizDetailViewModel @Inject constructor(
    private val quizRepository: QuizLocalRepository,
) : ViewModel() {

    private var _quizID = MutableLiveData<Long>()
    val quizID: MutableLiveData<Long> get() = _quizID
    private val _quiz = _quizID.switchMap { quizID ->
        liveData {
            withContext(Dispatchers.IO) {
                emit(quizRepository.findQuizById(quizID))
            }
        }.distinctUntilChanged()
    }

    init {
        Timber.d("Init")
    }

    fun getQuizDetail(): LiveData<HistoryQuiz> = _quiz.map { it.toHistoryQuiz() }

}