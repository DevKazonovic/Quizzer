package com.my.projects.quizapp.presentation.quiz

import androidx.lifecycle.*
import com.my.projects.quizapp.data.model.QuizModel
import com.my.projects.quizapp.data.model.QuizSetting
import com.my.projects.quizapp.data.remote.QuizApi
import com.my.projects.quizapp.data.remote.QuizResponse
import com.my.projects.quizapp.data.remote.asQuizModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class QuizViewModel : ViewModel(){

    private var _quizList = MutableLiveData<List<QuizModel>>()
    val quizList: LiveData<List<QuizModel>>
        get() = _quizList

    private var _currentQuiz = MutableLiveData<QuizModel>()
    val currentQuiz: LiveData<QuizModel>
        get() = _currentQuiz

    private var _currentQuizPosition = MutableLiveData<Int>()


    private var _score = MutableLiveData<Int>()
    val score : LiveData<Int>
        get() = _score


    init {
        _score.postValue(0)
    }

    fun getQuizzes(quizSetting: QuizSetting){
        viewModelScope.launch {
            val response: QuizResponse
            withContext(Dispatchers.IO){
                response =  QuizApi.quizAPI.getQuiz(quizSetting.amount,
                quizSetting.category,
                quizSetting.difficulty,
                quizSetting.type)
                Timber.d(response.toString())
            }
            _quizList.value=response.asQuizModel()
            _currentQuizPosition.value=0
            _currentQuiz.value=_quizList.value?.get(0)
        }
    }

    fun onQuizAnswered(quizPosition:Int, answerPosition:Int){
        val quizzes=_quizList.value
        if(quizzes!= null){
            val quiz=quizzes[quizPosition]
            val answer = quiz.answers[answerPosition]
            if(answer.isCorrect){
                incrementScore()
            }
        }
    }

    fun moveToNextQuiz(){
        val quizzes=_quizList.value
        var currentPosition=_currentQuizPosition.value

        if(quizzes!= null && currentPosition!=null) {
            currentPosition++
            if(currentPosition < quizzes.size){
                _currentQuiz.postValue(quizzes[currentPosition])
                _currentQuizPosition.postValue(currentPosition)
            }
        }
    }

    private fun incrementScore(){
        _score.postValue(_score.value?.plus(1))
    }

}