package com.my.projects.quizapp.presentation.history.controller

import androidx.lifecycle.*
import com.my.projects.quizapp.data.local.entity.Quiz
import com.my.projects.quizapp.data.local.entity.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.data.model.SortBy
import com.my.projects.quizapp.data.repository.IQuizRepository
import com.my.projects.quizapp.util.wrappers.Event
import kotlinx.coroutines.launch
import timber.log.Timber

class HistoryViewModel(
    private val quizRepository: IQuizRepository
) : ViewModel() {

    val quizzesMediatorLiveData = MediatorLiveData<List<QuizWithQuestionsAndAnswers>>()

    private var _quizzes = quizRepository.findAll()

    private val _quizzesSorted: LiveData<List<QuizWithQuestionsAndAnswers>>

    private var _sortBy = MutableLiveData<SortBy>()

    private var _isQuizUpdated = MutableLiveData<Event<Boolean>>()
    private var _isQuizDeleted = MutableLiveData<Event<Boolean>>()


    init {
        Timber.d("Init")

        _quizzesSorted = Transformations.switchMap(_sortBy) { sortBy ->
            updateQuizzes(sortBy)
        }

        quizzesMediatorLiveData.addSource(_quizzes) { list ->
            list?.let {
                Timber.d("_Quizzes ${it.size}")
                if (getCurrentSortBy() == SortBy.LATEST) {
                    quizzesMediatorLiveData.postValue(it)
                } else {
                    quizzesMediatorLiveData.postValue(sortBy(it, getCurrentSortBy()))
                }
            }
        }

        quizzesMediatorLiveData.addSource(_quizzesSorted) { list ->
            list?.let {
                quizzesMediatorLiveData.postValue(it)
                Timber.d("_QuizzesSorted ${it.size}")
            }
        }

    }


    fun onRefresh() {
        _sortBy.value = SortBy.LATEST
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

    fun onSortBy(type: SortBy) {
        Timber.d("Sort By $type ")
        _sortBy.value = type
    }

    fun onDeleteAllQuizzes() {
        viewModelScope.launch {
            quizRepository.deleteAll()
        }
    }


    private fun updateQuizzes(sortBy: SortBy)
    : MutableLiveData<List<QuizWithQuestionsAndAnswers>> {
        Timber.d("_Sorting SwitchMap $sortBy")
        val sortedList = MutableLiveData<List<QuizWithQuestionsAndAnswers>>()
        _quizzes.value?.let { list ->
            if (sortBy.ordinal == SortBy.LATEST.ordinal) {
                sortedList.postValue(list)
            } else {
                sortedList.postValue(sortBy(list, sortBy))
            }
        }
        return sortedList
    }

    private fun sortBy(
        quizzes: List<QuizWithQuestionsAndAnswers>,
        type: SortBy
    ): List<QuizWithQuestionsAndAnswers> {
        return when (type) {
            SortBy.TITLE -> quizzes.sortedBy { item -> item.quiz.title }
            SortBy.OLDEST -> quizzes.sortedBy { item -> item.quiz.date }
            SortBy.LATEST -> quizzes.sortedByDescending { item -> item.quiz.date }
        }
    }

    val isQuizUpdated: LiveData<Event<Boolean>> get() = _isQuizUpdated
    val isQuizDeleted: LiveData<Event<Boolean>> get() = _isQuizDeleted
    fun getCurrentSortBy(): SortBy = _sortBy.value ?: SortBy.LATEST


}