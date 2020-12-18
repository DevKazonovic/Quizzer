package com.my.projects.quizapp.presentation.history.controller

import androidx.lifecycle.*
import com.my.projects.quizapp.data.local.entity.Quiz
import com.my.projects.quizapp.data.local.entity.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.data.model.SortBy
import com.my.projects.quizapp.data.repository.IQuizRepository
import com.my.projects.quizapp.util.wrappers.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class HistoryViewModel(
    private val quizRepository: IQuizRepository
) : ViewModel() {

    val quizzesMediatorLiveData = MediatorLiveData<List<QuizWithQuestionsAndAnswers>>()

    private var _quizzes = quizRepository.findAll()
    private val _quizzesSorted: LiveData<List<QuizWithQuestionsAndAnswers>>
    private val _quizzesFilterdByCategory: LiveData<List<QuizWithQuestionsAndAnswers>>
    private val _quizzesFilterdByDate: LiveData<List<QuizWithQuestionsAndAnswers>>
    private val _quizzesSearched: LiveData<List<QuizWithQuestionsAndAnswers>>
    private var _quizzesRefreshed = MutableLiveData<List<QuizWithQuestionsAndAnswers>>()


    private var _searchQuery = MutableLiveData<String>()
    private var _sortBy = MutableLiveData<SortBy>()
    private var _filterByCat = MutableLiveData<Int>()
    private var _filterByDate = MutableLiveData<Long>()


    private var _isQuizUpdated = MutableLiveData<Event<Boolean>>()
    private var _isQuizDeleted = MutableLiveData<Event<Boolean>>()


    init {
        Timber.d("Init")

        _quizzesSearched = Transformations.switchMap(_searchQuery){query ->
            var currentList = _quizzes.value
            val newList = MutableLiveData<List<QuizWithQuestionsAndAnswers>>()

            if (isQueryValid(query)) {
                viewModelScope.launch {
                    withContext(Dispatchers.Default){
                        currentList = currentList?.filter { item ->
                            item.quiz.title.contains(query.trim(),true)
                        }
                    }
                    newList.postValue(currentList)
                }
                newList
            }else{
                newList.postValue(_quizzes.value)
                newList
            }

        }

        _quizzesSorted = Transformations.switchMap(_sortBy) { sortBy ->
            sortCurrentHistory(sortBy)
        }

        _quizzesFilterdByCategory = Transformations.switchMap(_filterByCat) {
            filterHistory(getCurrentCatID(), getCurrentDate())
        }

        _quizzesFilterdByDate = Transformations.switchMap(_filterByDate) {
            filterHistory(getCurrentCatID(), getCurrentDate())
        }


        addQuizzesMediatorLiveDataSources()
    }

    private fun addQuizzesMediatorLiveDataSources() {

        quizzesMediatorLiveData.addSource(_quizzes) {
            updateCurrentHistory(it)
        }

        quizzesMediatorLiveData.addSource(_quizzesSorted) { list ->
            list?.let {
                Timber.d("_QuizzesSorted ${it.size}")
                quizzesMediatorLiveData.postValue(it)
            }
        }

        quizzesMediatorLiveData.addSource(_quizzesSearched) {
            updateCurrentHistory(it)
        }

        quizzesMediatorLiveData.addSource(_quizzesFilterdByCategory) {
            updateCurrentHistory(it)
        }

        quizzesMediatorLiveData.addSource(_quizzesFilterdByDate) {
            updateCurrentHistory(it)
        }

        quizzesMediatorLiveData.addSource(_quizzesRefreshed) {
            updateCurrentHistory(it)
        }
    }

    fun onRefresh() {
        _quizzesRefreshed.value = _quizzes.value
        _filterByDate.value = null
        _filterByCat.value = null
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


    fun onInstantSearch(query:String?){
        viewModelScope.launch {
            delay(500)
            _searchQuery.value = query
        }
    }

    fun onSubmitSearch(query:String?){
        _searchQuery.value = query
    }

    private fun isQueryValid(query: String?)
    = query != null && query.isNotEmpty()





    fun onSortBy(type: SortBy) {
        Timber.d("Sort By $type ")
        _sortBy.value = type
    }

    fun onFilterByCat(id: Int?) {
        _filterByCat.value = id
    }

    fun onFilterByDate(date: Long?) {
        _filterByDate.value = date
    }

    fun onDeleteAllQuizzes() {
        viewModelScope.launch {
            quizRepository.deleteAll()
        }
    }




    private fun updateCurrentHistory(
        newList: List<QuizWithQuestionsAndAnswers>?
    ) {
        newList?.let {
            Timber.d("_Quizzes ${it.size}")
            if (getCurrentSortBy() == SortBy.LATEST) {
                quizzesMediatorLiveData.postValue(it)
            } else {
                sortCurrentHistory(getCurrentSortBy())
            }
        }
    }

    private fun sortCurrentHistory(
        sortBy: SortBy
    ): MutableLiveData<List<QuizWithQuestionsAndAnswers>> {
        Timber.d("Sorting or Referesh $sortBy")
        val sortedList = MutableLiveData<List<QuizWithQuestionsAndAnswers>>()
        quizzesMediatorLiveData.value?.let { list ->
            sortedList.postValue(when (sortBy) {
                SortBy.TITLE -> list.sortedBy { item ->
                    item.quiz.title.toLowerCase(Locale.ROOT)
                }
                SortBy.OLDEST -> list.sortedBy { item ->
                    item.quiz.date
                }
                SortBy.LATEST -> list.sortedByDescending { item ->
                    item.quiz.date
                }
            })
        }
        return sortedList
    }


    private fun filterHistory(
        categoryID: Int?,
        saveDate: Date?
    ): LiveData<List<QuizWithQuestionsAndAnswers>> {
        Timber.d("filter= $categoryID , $saveDate")
        val quizzes: LiveData<List<QuizWithQuestionsAndAnswers>>
        quizzes =
            Transformations.switchMap(quizRepository.getFilteredQuizzes(categoryID, saveDate)) {
                val list = MutableLiveData<List<QuizWithQuestionsAndAnswers>>()
                list.postValue(it)
                list
            }

        return quizzes
    }


    val isQuizUpdated: LiveData<Event<Boolean>> get() = _isQuizUpdated
    val isQuizDeleted: LiveData<Event<Boolean>> get() = _isQuizDeleted

    val searchQuery: LiveData<String> get() = _searchQuery
    val filterByDate: LiveData<Long> get() = _filterByDate
    val filterByCat: LiveData<Int> get() = _filterByCat


    fun getCurrentSortBy(): SortBy = _sortBy.value ?: SortBy.LATEST
    fun getCurrentCatID(): Int? = _filterByCat.value
    fun getCurrentDate(): Date? = _filterByDate.value?.let {
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        formatter.parse(formatter.format(it))
    }

}