package com.my.projects.quizapp.presentation.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.DataState
import com.my.projects.quizapp.data.model.QuestionModel
import com.my.projects.quizapp.data.model.QuizModel
import com.my.projects.quizapp.data.model.QuizSetting
import com.my.projects.quizapp.data.remote.QuizApi
import com.my.projects.quizapp.data.remote.QuizResponse
import com.my.projects.quizapp.data.remote.asQuestionModel
import com.my.projects.quizapp.util.wrappers.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException

class QuizViewModel : ViewModel() {

    private var _currentQuiz = MutableLiveData<QuizModel>()

    private var _currentQuestion = MutableLiveData<QuestionModel>()
    val currentQuestion: LiveData<QuestionModel> get() = _currentQuestion

    private var _currentQuestionPosition = MutableLiveData<Int>()
    val currentQuestionPosition: LiveData<Int> get() = _currentQuestionPosition

    private var _currentQuizSetting = MutableLiveData<QuizSetting>()

    private var _dataState = MutableLiveData<DataState>()
    val dataState: LiveData<DataState> get() = _dataState

    private var _score = MutableLiveData<Int>()
    val score: LiveData<Int> get() = _score

    private var _navigateToScore = MutableLiveData<Event<Boolean>>()
    val navigateToScore: LiveData<Event<Boolean>> get() = _navigateToScore


    init {
        _score.postValue(0)
        _navigateToScore.value = Event(false)
    }

    fun getQuiz(quizSetting: QuizSetting) {
        _dataState.value = DataState.Loading
        viewModelScope.launch {
            try {
                val response: QuizResponse
                withContext(Dispatchers.IO) {
                    response = QuizApi.quizAPI.getQuiz(
                        quizSetting.amount,
                        quizSetting.category,
                        quizSetting.difficulty,
                        quizSetting.type
                    )
                }
                if (response.code == 0) {
                    if (response.results.isEmpty()) {
                        DataState.Error(R.string.code1)
                    } else {
                        _currentQuiz.value = QuizModel(response.asQuestionModel())
                        initValues()
                        _dataState.value = DataState.Success
                        _currentQuizSetting.value = quizSetting
                    }
                } else {
                    _dataState.value = when (response.code) {
                        1 -> DataState.HttpErrors.NoResults(R.string.code1)
                        2 -> DataState.HttpErrors.InvalidParameter(R.string.code2)
                        3 -> DataState.HttpErrors.TokenNotFound(R.string.code3)
                        4 -> DataState.HttpErrors.TokenEmpty(R.string.code4)
                        else -> DataState.Error(R.string.unknown_error)
                    }
                }
            } catch (e: IOException) {
                _dataState.value = DataState.NetworkException(e.message!!)
            }
        }
    }

    private fun initValues() {
        _score.value = 0
        _currentQuestionPosition.value = 0
        _currentQuestion.value = _currentQuiz.value?.questions?.get(0)
    }

    fun onQuestionAnswered(answerPosition: Int) {

        val quizzes = getCurrentQuestionList()
        val quizCurrentPos = getCurrentQuizPosition()

        if (quizzes != null && quizCurrentPos != null) {
            val quiz = quizzes[quizCurrentPos]
            if (answerPosition >= 0) {
                val answer = quiz.answers[answerPosition]
                if (answer.isCorrect) {
                    incrementScore()
                }
            }
        }
    }

    fun moveToNextQuiz() {
        val quizzes = getCurrentQuestionList()
        var quizCurrentPosition = getCurrentQuizPosition()

        if (quizzes != null && quizCurrentPosition != null) {
            quizCurrentPosition++
            if (quizCurrentPosition < quizzes.size) {
                _currentQuestion.postValue(quizzes[quizCurrentPosition])
                _currentQuestionPosition.postValue(quizCurrentPosition)
            } else {
                _navigateToScore.value = Event(true)
            }
        }
    }

    private fun incrementScore() {
        _score.value = _score.value?.plus(1)
        Timber.d("Score: ${_score.value}")
    }

    fun refresh() {
        _currentQuizSetting.value?.let { getQuiz(it) }
    }

    fun getCurrentQuizzesListSize(): Int = _currentQuiz.value?.questions?.size ?: 0
    private fun getCurrentQuestionList(): List<QuestionModel>? = _currentQuiz.value?.questions
    private fun getCurrentQuizPosition(): Int? = _currentQuestionPosition.value


}