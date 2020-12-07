package com.my.projects.quizapp.presentation.history.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.projects.quizapp.data.local.entity.Quiz
import com.my.projects.quizapp.data.local.entity.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.data.local.repository.IQuizRepository
import com.my.projects.quizapp.data.model.SortBy
import com.my.projects.quizapp.util.wrappers.Event
import kotlinx.coroutines.launch

class HistoryViewModel(private val quizRepository: IQuizRepository) : ViewModel() {

    private var _quizzes = MutableLiveData<List<QuizWithQuestionsAndAnswers>>()
    private var _quizzesCopy = MutableLiveData<List<QuizWithQuestionsAndAnswers>>()

    private var _sortBy = MutableLiveData<SortBy>()

    private var _isQuizUpdated = MutableLiveData<Event<Boolean>>()
    private var _isQuizDeleted = MutableLiveData<Event<Boolean>>()

    init {
        _sortBy.value = SortBy.OLDEST
        getStoredUserQuizzes()
    }

    private fun getStoredUserQuizzes() {
        viewModelScope.launch {
            _quizzes.postValue(quizRepository.findAll())
        }
    }

    fun onDeleteAllQuizzes() {
        viewModelScope.launch {
            quizRepository.deleteAll()
            getStoredUserQuizzes()
        }
    }

    fun onRefresh(){
        getStoredUserQuizzes()
    }

    fun onQuizUpdate(quiz: Quiz) {
        viewModelScope.launch {
            quizRepository.updateQuiz(quiz)
            _isQuizUpdated.value = Event(true)
        }
    }

    fun onQuizDelete(quiz: Quiz) {
        viewModelScope.launch {
            quizRepository.deleteQuiz(quiz)
            _isQuizDeleted.value = Event(true)
        }
    }

    fun onSortBy(type:SortBy){
        _quizzesCopy = lazy {_quizzes}.value
        _sortBy.value = type
        var quizzes = _quizzesCopy.value
        quizzes = when(type){
            SortBy.TITLE -> {
                quizzes?.sortedBy { item -> item.quiz.title }
            }
            SortBy.OLDEST -> {
                quizzes?.sortedBy { item -> item.quiz.date }
            }
            SortBy.LATEST -> {
                quizzes?.sortedByDescending { item -> item.quiz.date }
            }
        }
        _quizzes.postValue(quizzes)
    }

    val isQuizUpdated: LiveData<Event<Boolean>> get() = _isQuizUpdated
    val isQuizDeleted: LiveData<Event<Boolean>> get() = _isQuizDeleted
    val quizzes: LiveData<List<QuizWithQuestionsAndAnswers>> get() = _quizzes
    val sortBy: LiveData<SortBy> get() = _sortBy

    fun getCurrentSortBy(): SortBy? = _sortBy.value


}