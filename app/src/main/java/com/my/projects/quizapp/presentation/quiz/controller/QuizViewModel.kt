package com.my.projects.quizapp.presentation.quiz.controller

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.local.entity.Quiz
import com.my.projects.quizapp.data.local.repository.IQuizRepository
import com.my.projects.quizapp.data.model.AnswerModel
import com.my.projects.quizapp.data.model.QuestionModel
import com.my.projects.quizapp.data.model.QuizModel
import com.my.projects.quizapp.data.model.QuizSetting
import com.my.projects.quizapp.data.remote.QuizApi
import com.my.projects.quizapp.data.remote.QuizResponse
import com.my.projects.quizapp.data.remote.asQuestionModel
import com.my.projects.quizapp.util.wrappers.DataState
import com.my.projects.quizapp.util.wrappers.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.util.*

class QuizViewModel(private val quizRepo: IQuizRepository) : ViewModel() {

    private var _currentQuizSetting = MutableLiveData<QuizSetting>()

    private var _currentQuiz = MutableLiveData<QuizModel>()

    private var _currentQuestion = MutableLiveData<QuestionModel>()
    val currentQuestion: LiveData<QuestionModel> get() = _currentQuestion

    private var _currentQuestionPosition = MutableLiveData<Int>()
    val currentQuestionPosition: LiveData<Int> get() = _currentQuestionPosition

    private var _dataState = MutableLiveData<DataState>()
    val dataState: LiveData<DataState> get() = _dataState

    private var _score = MutableLiveData<Int>()
    val score: LiveData<Int> get() = _score

    private var _countDown = MutableLiveData<Long>()
    val countDown: LiveData<Long> get() = _countDown

    private lateinit var countDownTimer: CountDownTimer

    private var _navigateToScore = MutableLiveData<Event<Boolean>>()
    val navigateToScore: LiveData<Event<Boolean>> get() = _navigateToScore

    private var _userAnswers = mutableMapOf<Int, AnswerModel>()


    init {
        _score.postValue(0)
        _navigateToScore.value = Event(false)
    }


    //Network Request
    fun getQuiz(quizSetting: QuizSetting) {
        countDownTimer = setCountDownTimer(20000, 1000)
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
                        DataState.Error(R.string.all_error_no_result)
                    } else {
                        _userAnswers = mutableMapOf()
                        _currentQuizSetting.value = quizSetting
                        _currentQuiz.value = QuizModel(response.asQuestionModel())
                        startQuiz()
                    }
                } else {
                    _dataState.value = when (response.code) {
                        1 -> DataState.HttpErrors.NoResults(R.string.all_error_no_result)
                        2 -> DataState.HttpErrors.InvalidParameter(R.string.all_error_invalid_arg)
                        3 -> DataState.HttpErrors.TokenNotFound(R.string.all_error_no_token)
                        4 -> DataState.HttpErrors.TokenEmpty(R.string.all_error_empty_token)
                        else -> DataState.Error(R.string.all_unknown_error)
                    }
                }
            } catch (e: IOException) {
                _dataState.value = DataState.NetworkException(e.message!!)
            }
        }
    }

    //UI Data Controller
    fun onQuestionAnswered(answerPosition: Int) {
        val questions = getCurrentQuestionList()
        val currentQuestionPos = getCurrentQuestionPosition()

        if (questions != null && currentQuestionPos != null) {
            val question = questions[currentQuestionPos]
            if (answerPosition >= 0) {
                val userAnswer = question.answers[answerPosition]
                _userAnswers.put(
                    currentQuestionPos,
                    AnswerModel(userAnswer.answer, userAnswer.isCorrect, true)
                )
            }
            Timber.d(_userAnswers.toString())
        }
    }

    fun onMoveToNextQuiz() {
        val quizzes = getCurrentQuestionList()
        var quizCurrentPosition = getCurrentQuestionPosition()

        if (quizzes != null && quizCurrentPosition != null) {
            quizCurrentPosition++
            if (quizCurrentPosition < quizzes.size) {
                _currentQuestion.postValue(quizzes[quizCurrentPosition])
                _currentQuestionPosition.postValue(quizCurrentPosition)
                countDownTimer.start()
            } else {
                finishQuiz()
            }
        }

    }

    fun onReferesh() {
        _currentQuizSetting.value?.let { getQuiz(it) }
    }

    fun onGetQuizLogs(): MutableList<QuestionModel> {
        val newQuestions = mutableListOf<QuestionModel>()
        val questions = getCurrentQuestionList()
        if (!questions.isNullOrEmpty()) {
            for (i in questions.indices) {
                val userAnswer = _userAnswers[i]
                val answers = questions[i].answers
                val newAnswers = mutableListOf<AnswerModel>()
                for (j in answers.indices) {
                    if (userAnswer != null && answers[j].answer == userAnswer.answer) {
                        newAnswers.add(AnswerModel(userAnswer.answer, userAnswer.isCorrect, true))
                    } else {
                        newAnswers.add(answers[j])
                    }
                }
                newQuestions.add(
                    QuestionModel(
                        questions[i].category,
                        questions[i].type,
                        questions[i].difficulty,
                        questions[i].question,
                        newAnswers
                    )
                )
            }
        }

        return newQuestions
    }


    //View Model Logic
    private fun startQuiz() {
        _score.value = 0
        _currentQuestionPosition.value = 0
        _currentQuestion.value = _currentQuiz.value?.questions?.get(0)
        countDownTimer.start()
        _dataState.value = DataState.Success
    }

    private fun finishQuiz() {
        countDownTimer.cancel()
        _userAnswers.forEach { item ->
            if (item.value.isCorrect) {
                incrementScore()
            }
        }
        _navigateToScore.value = Event(true)

    }

    private fun incrementScore() {
        _score.value = _score.value?.plus(1)
    }

    private fun setCountDownTimer(millisInFuture: Long, countDownInterval: Long): CountDownTimer {
        return object : CountDownTimer(millisInFuture, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                _countDown.value = millisUntilFinished / 1000
            }

            override fun onFinish() {
                onMoveToNextQuiz()
            }
        }
    }


    //DataBase Query
    fun saveQuiz(quizName: String) {
        viewModelScope.launch {
            val score = _score.value
            val questions = getCurrentQuestionList()
            if (score != null && questions != null) {
                quizRepo.saveQuiz(
                    Quiz(quizName, score, Date()),
                    questions,
                    _userAnswers
                )
            }
        }
    }


    //Getters
    fun getCurrentQuizzesListSize(): Int = _currentQuiz.value?.questions?.size ?: 0
    private fun getCurrentQuestionList(): List<QuestionModel>? = _currentQuiz.value?.questions
    private fun getCurrentQuestionPosition(): Int? = _currentQuestionPosition.value


}