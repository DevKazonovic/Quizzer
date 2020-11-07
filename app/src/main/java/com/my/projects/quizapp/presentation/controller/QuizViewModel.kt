package com.my.projects.quizapp.presentation.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.DataState
import com.my.projects.quizapp.data.model.QuizModel
import com.my.projects.quizapp.data.model.QuizSetting
import com.my.projects.quizapp.data.remote.QuizApi
import com.my.projects.quizapp.data.remote.QuizResponse
import com.my.projects.quizapp.data.remote.asQuizModel
import com.my.projects.quizapp.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException

class QuizViewModel() : ViewModel() {

    /*constructor(setting:QuizSetting):this(){
        this.setting=setting
    }*/

    //var setting=QuizSetting(10)

    private var _quizList = MutableLiveData<List<QuizModel>>()

    private var _currentQuizPosition = MutableLiveData<Int>()
    val currentQuizPosition: LiveData<Int>
        get() = _currentQuizPosition

    private var _currentQuiz = MutableLiveData<QuizModel>()
    val currentQuiz: LiveData<QuizModel>
        get() = _currentQuiz

    private var _dataState = MutableLiveData<DataState>()
    val dataState: LiveData<DataState>
        get() = _dataState

    private var _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private var _navigateToScore = MutableLiveData<Event<Boolean>>()
    val navigateToScore: LiveData<Event<Boolean>>
        get() = _navigateToScore




    init {
        _score.postValue(0)
        _navigateToScore.value = Event(false)

    }

    fun getQuizzes(quizSetting: QuizSetting) {
        _dataState.value=DataState.Loading
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
                    Timber.d(response.toString())
                }
                if(response.code==0){
                    if(response.results.isEmpty()){
                        DataState.Error(R.string.code1)
                    }else{
                        _quizList.value = response.asQuizModel()
                        initValues()
                        _dataState.value=DataState.Success
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
                _dataState.value=DataState.NetworkException(e.message!!)
            }
        }
    }

    private fun initValues() {
        _score.value = 0
        _currentQuizPosition.value = 0
        _currentQuiz.value = _quizList.value?.get(0)
    }

    fun onQuizAnswered(answerPosition: Int) {

        val quizzes = getCurrentQuizzesList()
        val quizCurrentPos = getCurrentQuizPosition()

        if (quizzes != null && quizCurrentPos != null) {
            val quiz = quizzes[quizCurrentPos]
            if(answerPosition>=0){
                val answer = quiz.answers[answerPosition]
                if (answer.isCorrect) {
                    incrementScore()
                }
            }
        }
    }

    fun moveToNextQuiz() {
        val quizzes = getCurrentQuizzesList()
        var quizCurrentPosition = getCurrentQuizPosition()

        if (quizzes != null && quizCurrentPosition != null) {
            quizCurrentPosition++
            if (quizCurrentPosition < quizzes.size) {
                _currentQuiz.postValue(quizzes[quizCurrentPosition])
                _currentQuizPosition.postValue(quizCurrentPosition)
            } else {
                _navigateToScore.value = Event(true)
            }
        }
    }

    private fun incrementScore() {
        _score.value = _score.value?.plus(1)
        Timber.d("Score: ${_score.value}")
    }

    fun getCurrentQuizzesListSize(): Int = _quizList.value?.size ?: 0
    private fun getCurrentQuizzesList(): List<QuizModel>? = _quizList.value
    private fun getCurrentQuizPosition(): Int? = _currentQuizPosition.value


}