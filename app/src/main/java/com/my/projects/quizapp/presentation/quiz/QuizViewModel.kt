package com.my.projects.quizapp.presentation.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.local.model.QuizEntity
import com.my.projects.quizapp.data.remote.response.QuizResponse
import com.my.projects.quizapp.data.remote.response.asQuestionModel
import com.my.projects.quizapp.data.repository.QuizLocalRepository
import com.my.projects.quizapp.data.repository.QuizRemoteRepository
import com.my.projects.quizapp.domain.manager.CountDownTimerManager
import com.my.projects.quizapp.domain.model.Answer
import com.my.projects.quizapp.domain.model.Question
import com.my.projects.quizapp.domain.model.QuizSetting
import com.my.projects.quizapp.util.DBUtil.Companion.generateRandomTitle
import com.my.projects.quizapp.util.wrappers.DataState
import com.my.projects.quizapp.util.wrappers.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.util.*
import javax.inject.Inject

class QuizViewModel @Inject constructor(
    private val quizRepository: QuizLocalRepository,
    private val quizRemoteRepository: QuizRemoteRepository,
    private val countDownTimerManager: CountDownTimerManager
) : ViewModel() {

    private var _dataState = MutableLiveData<DataState>()
    private var _currentQuizSetting = MutableLiveData<QuizSetting>()
    private var _currentQuiz = MutableLiveData<com.my.projects.quizapp.domain.model.Quiz>()
    private var _currentQuestion = MutableLiveData<Question>()
    private var _currentQuestionPosition = MutableLiveData<Int>()
    private var _userAnswers = mutableMapOf<Int, Answer>()
    private var _score = MutableLiveData<Int>()
    private var _countDown = MutableLiveData<Long>()
    private var _navigateToScore = MutableLiveData<Event<Boolean>>()
    private var _snackBarSaved = MutableLiveData<Event<Boolean>>()


    init {
        _score.postValue(0)
        _navigateToScore.value = Event(false)
        countDownTimerManager.setCountDownTimer(object :
            CountDownTimerManager.OnCountDownTimerChangeListener {
            override fun onTick(millisUntilFinished: Long) {
                _countDown.value = millisUntilFinished
            }

            override fun onFinish() {
                onMoveToNextQuiz()
            }
        })
        Timber.d("Init Done")
    }


    //Network Request
    fun getQuiz(quizSetting: QuizSetting) {
        clearAndReset()
        _dataState.value = DataState.Loading
        _currentQuizSetting.value = quizSetting
        viewModelScope.launch {
            try {
                val response: QuizResponse

                withContext(Dispatchers.IO) { response = quizRemoteRepository.getQuiz(quizSetting) }

                if (response.code == 0 && response.results.isNotEmpty()) {
                    _currentQuiz.value =
                        com.my.projects.quizapp.domain.model.Quiz(response.asQuestionModel())
                    startQuiz()
                } else {
                    handleDataState(response)
                }

            } catch (e: IOException) {
                _dataState.value = DataState.NetworkException(R.string.all_network_error)
            }
        }
    }

    //DataBase Query
    fun saveQuiz(quizName: String) {
        viewModelScope.launch {
            val score = _score.value
            val questions = getCurrentQuestionList()
            val category = _currentQuizSetting.value?.category
            if (score != null && questions != null && category != null) {
                //Generet a randum title is empty
                val title =
                    if (quizName.isEmpty()) generateRandomTitle(category, score) else quizName

                quizRepository.saveQuiz(
                    QuizEntity(title, score, Date(), category),
                    questions,
                    _userAnswers
                )

                _snackBarSaved.value = Event(true)
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
                    Answer(userAnswer.id, userAnswer.answer, userAnswer.isCorrect, true)
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
                countDownTimerManager.start()
            } else {
                finishQuiz()
            }
        }

    }

    fun onReferesh() {
        viewModelScope.launch {
            _dataState.value = DataState.Loading
            delay(1000)
            getQuiz(_currentQuizSetting.value!!)
        }
    }

    fun onGetQuizLogs(): MutableList<Question> {
        val newQuestions = mutableListOf<Question>()
        val questions = getCurrentQuestionList()
        if (!questions.isNullOrEmpty()) {
            for (i in questions.indices) {
                val userAnswer = _userAnswers[i]
                val answers = questions[i].answers
                val newAnswers = mutableListOf<Answer>()
                for (j in answers.indices) {
                    if (userAnswer != null && answers[j].id == userAnswer.id) {
                        newAnswers.add(
                            Answer(
                                userAnswer.id,
                                userAnswer.answer,
                                userAnswer.isCorrect,
                                true
                            )
                        )
                    } else {
                        newAnswers.add(answers[j])
                    }
                }
                newQuestions.add(
                    Question(
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

    fun onStop() {
        countDownTimerManager.stop()
    }


    //View Model Logic
    private fun handleDataState(response: QuizResponse) {
        if (response.results.isEmpty()) {
            DataState.Error(R.string.all_error_no_result)
        } else {
            _dataState.value = when (response.code) {
                1 -> DataState.HttpErrors.NoResults(R.string.all_error_no_result)
                2 -> DataState.HttpErrors.InvalidParameter(R.string.all_error_invalid_arg)
                3 -> DataState.HttpErrors.TokenNotFound(R.string.all_error_no_token)
                4 -> DataState.HttpErrors.TokenEmpty(R.string.all_error_empty_token)
                else -> DataState.Error(R.string.all_unknown_error)
            }
        }
    }

    private fun startQuiz() {
        _score.value = 0
        _currentQuestionPosition.value = 0
        _currentQuestion.value = _currentQuiz.value?.questions?.get(0)
        countDownTimerManager.start()
        _dataState.value = DataState.Success
    }

    private fun finishQuiz() {
        countDownTimerManager.stop()
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

    private fun clearAndReset() {
        if (countDownTimerManager.isStarted) {
            countDownTimerManager.reset()
        }
        _userAnswers = mutableMapOf() //reset userAnswers
    }

    //Callback
    override fun onCleared() {
        super.onCleared()
        Timber.d("OnCleared")
    }


    //Getters
    fun getCurrentQuizzesListSize(): Int = _currentQuiz.value?.questions?.size ?: 0
    private fun getCurrentQuestionList(): List<Question>? = _currentQuiz.value?.questions
    private fun getCurrentQuestionPosition(): Int? = _currentQuestionPosition.value

    val currentQuestion: LiveData<Question> get() = _currentQuestion
    val currentQuestionPosition: LiveData<Int> get() = _currentQuestionPosition
    val dataState: LiveData<DataState> get() = _dataState
    val score: LiveData<Int> get() = _score
    val countDown: LiveData<Long> get() = _countDown
    val navigateToScore: LiveData<Event<Boolean>> get() = _navigateToScore
    val snackBarSaved: LiveData<Event<Boolean>> get() = _snackBarSaved


}