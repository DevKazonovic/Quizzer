package com.my.projects.quizapp.presentation.quiz

import androidx.lifecycle.*
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

class QuizViewModel : ViewModel() {

    private var _quizList = MutableLiveData<List<QuizModel>>()


    private var _currentQuiz = MutableLiveData<QuizModel>()
    val currentQuiz: LiveData<QuizModel>
        get() = _currentQuiz

    private var _currentQuizPosition = MutableLiveData<Int>()


    private var _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private var _navigateToScore = MutableLiveData<Event<Boolean>>()
    val navigateToScore: LiveData<Event<Boolean>>
        get() = _navigateToScore




    init {
        _score.postValue(0)
        _navigateToScore.value=Event(false)
    }

    fun getQuizzes(quizSetting: QuizSetting) {
        viewModelScope.launch {
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
            _quizList.value = response.asQuizModel()
            _currentQuizPosition.value = 0
            _currentQuiz.value = _quizList.value?.get(0)
        }
    }

    fun onQuizAnswered(answerPosition: Int) {

        val quizzes = getCurrentQuizzesList()
        val quizCurrentPos = getCurrentQuizPosition()

        if (quizzes != null && quizCurrentPos != null) {
            val quiz = quizzes[quizCurrentPos]
            val answer = quiz.answers[answerPosition]
            if (answer.isCorrect) {
                incrementScore()
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
            }else{
                _navigateToScore.value=Event(true)
            }
        }
    }

    private fun incrementScore() {
        _score.value = _score.value?.plus(1)
        Timber.d("Score: ${_score.value}")
    }

    fun getCurrentQuizzesList(): List<QuizModel>? = _quizList.value
    private fun getCurrentQuizPosition(): Int? = _currentQuizPosition.value

}