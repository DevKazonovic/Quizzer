package com.devkazonovic.projects.quizzer.presentation.history.list

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devkazonovic.projects.quizzer.data.local.model.relations.QuizWithQuestionsAndAnswers
import com.devkazonovic.projects.quizzer.data.repository.QuizLocalRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    private val quizRepository: QuizLocalRepository,
) : ViewModel() {

    val quizzesMediatorLiveData = MediatorLiveData<List<QuizWithQuestionsAndAnswers>>()

    private var _quizzes = quizRepository.findAll()
    private var _quizzesRefreshed = MutableLiveData<List<QuizWithQuestionsAndAnswers>>()

    init {
        addQuizzesMediatorLiveDataSources()
    }

    fun onRefresh() {
        _quizzesRefreshed.value = _quizzes.value
    }

    fun onDeleteAllQuizzes() {
        viewModelScope.launch {
            quizRepository.deleteAll()
        }
        onRefresh()
    }

    fun onQuizDelete(quizID: Long) {
        viewModelScope.launch {
            quizRepository.deleteQuiz(quizID)
        }
    }

    private fun addQuizzesMediatorLiveDataSources() {

        quizzesMediatorLiveData.addSource(_quizzes) {
            updateCurrentHistory(it)
        }
        quizzesMediatorLiveData.addSource(_quizzesRefreshed) {
            updateCurrentHistory(it)
        }
    }

    private fun updateCurrentHistory(newList: List<QuizWithQuestionsAndAnswers>?) {
        newList?.let {
            quizzesMediatorLiveData.postValue(it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.d("OnCleared")
    }


}