package com.my.projects.quizapp.presentation.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.CategoriesStore
import com.my.projects.quizapp.data.local.model.QuizEntity
import com.my.projects.quizapp.data.remote.response.QuizResponse
import com.my.projects.quizapp.data.remote.response.asQuestionModel
import com.my.projects.quizapp.data.repository.QuizLocalRepository
import com.my.projects.quizapp.data.repository.QuizRemoteRepository
import com.my.projects.quizapp.domain.manager.CountDownTimerManager
import com.my.projects.quizapp.domain.model.Answer
import com.my.projects.quizapp.domain.model.Question
import com.my.projects.quizapp.domain.model.Quiz
import com.my.projects.quizapp.domain.model.QuizSetting
import com.my.projects.quizapp.util.wrappers.DataState
import com.my.projects.quizapp.util.wrappers.Event
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class QuizViewModel @Inject constructor(
    private val quizRepository: QuizLocalRepository,
    private val quizRemoteRepository: QuizRemoteRepository,
    private val countDownTimerManager: CountDownTimerManager
) : ViewModel() {

    private var _dataState = MutableLiveData<DataState>()
    private var _currentQuizSetting = MutableLiveData<QuizSetting>()
    private var _currentQuiz = MutableLiveData<Quiz>()
    private var _currentQuestion = MutableLiveData<Question>()
    private var _currentQuestionPosition = MutableLiveData<Int>()
    private var _userAnswers = mutableMapOf<Int, Answer>()
    private var _score = MutableLiveData<Int>()
    private var _countDown = MutableLiveData<Long>()
    private var _isQuizFinished = MutableLiveData<Event<Boolean>>()
    private var _isQuizSaved = MutableLiveData<Event<Boolean>>()

    init {
        _score.postValue(0)
        _isQuizFinished.value = Event(false)

        Timber.d("Init Done")
    }

    //Network Request
    fun getQuiz(quizSetting: QuizSetting) {
        clearAndReset()
        _dataState.value = DataState.Loading
        _currentQuizSetting.value = quizSetting
        countDownTimerManager.setCountDownTimer(quizSetting.countDownInSeconds!!, object :
            CountDownTimerManager.OnCountDownTimerChangeListener {
            override fun onTick(millisUntilFinished: Long) {
                _countDown.value = millisUntilFinished
            }

            override fun onFinish() {
                onMoveToNextQuiz()
            }
        })
        viewModelScope.launch {
            try {
                handleResonse(quizRemoteRepository.getQuiz(quizSetting))
            } catch (e: IOException) {
                _dataState.value = DataState.NetworkException(R.string.error_network)
            } catch (e: HttpException) {
                _dataState.value = DataState.NetworkException(R.string.error_network)
            }
        }
    }

    //DataBase Query
    fun saveQuiz() {
        viewModelScope.launch {
            val score = _score.value
            val questions = getCurrentQuestionList()
            val category = _currentQuizSetting.value?.category
            if (score != null && questions != null && category != null) {
                //Generet a randum title is empty
                val title = "Quiz In ${CategoriesStore.getCategorie(category).name}"

                quizRepository.saveQuiz(
                    QuizEntity(title, score, LocalDate.now(), category),
                    questions,
                    _userAnswers
                )

                _isQuizSaved.value = Event(true)
            }
        }
    }


    //UI Data Controllers
    fun onQuestionAnswered(answerPosition: Int) {
        val questions = getCurrentQuestionList()
        val currentQuestionPos = getCurrentQuestionPosition()

        if (questions != null && currentQuestionPos != null) {
            val question = questions[currentQuestionPos]
            if (answerPosition >= 0 && answerPosition < question.answers.size) {
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
                _currentQuestionPosition.value = quizCurrentPosition
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

    fun onCurrentQuizSummary(): MutableList<Question> {
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


    //ViewModel Util
    private fun handleResonse(response: QuizResponse) {
        if (response.code == 0 && response.results.isNotEmpty()) {
            _currentQuiz.value = Quiz(response.asQuestionModel())
            startQuiz()
        } else {

            if (response.results.isEmpty()) {
                _dataState.value = DataState.Error(R.string.error_no_result)
            } else {
                _dataState.value = when (response.code) {
                    1 -> DataState.HttpErrors.NoResults(R.string.error_no_result)
                    2 -> DataState.HttpErrors.InvalidParameter(R.string.error_invalid_arg)
                    else -> DataState.Error(R.string.error_unknown)
                }
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
        _isQuizFinished.value = Event(true)

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

    val dataState: LiveData<DataState> get() = _dataState
    val currentQuestion: LiveData<Question> get() = _currentQuestion
    val currentQuestionPosition: LiveData<Int> get() = _currentQuestionPosition
    val currentQuizSetting: LiveData<QuizSetting> get() = _currentQuizSetting
    val score: LiveData<Int> get() = _score
    val countDown: LiveData<Long> get() = _countDown
    val isQuizFinished: LiveData<Event<Boolean>> get() = _isQuizFinished
    val isQuizSaved: LiveData<Event<Boolean>> get() = _isQuizSaved


}