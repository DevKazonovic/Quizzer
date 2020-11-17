package com.my.projects.quizapp.presentation.controller

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.DataState
import com.my.projects.quizapp.data.db.QuizDB
import com.my.projects.quizapp.data.db.entity.Quiz
import com.my.projects.quizapp.data.db.entity.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.data.model.*
import com.my.projects.quizapp.data.remote.QuizApi
import com.my.projects.quizapp.data.remote.QuizResponse
import com.my.projects.quizapp.data.remote.asQuestionModel
import com.my.projects.quizapp.util.wrappers.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.util.*
import kotlin.collections.set

class QuizViewModel : ViewModel() {

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

    private var _navigateToScore = MutableLiveData<Event<Boolean>>()
    val navigateToScore: LiveData<Event<Boolean>> get() = _navigateToScore

    private var _userAnswers = mutableMapOf<Int, AnswerModel>()

    private var _quizzes = MutableLiveData<List<QuizWithQuestionsAndAnswers>>()
    val quizzes: LiveData<List<QuizWithQuestionsAndAnswers>> get() = _quizzes

    init {
        _score.postValue(0)
        _navigateToScore.value = Event(false)
    }



    private fun initValues() {
        _score.value = 0
        _currentQuestionPosition.value = 0
        _currentQuestion.value = _currentQuiz.value?.questions?.get(0)
    }

    //Network Request
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


    //UI Data Controller
    fun onQuestionAnswered(answerPosition: Int) {
        val questions = getCurrentQuestionList()
        val currentQuestionPos = getCurrentQuestionPosition()

        if (questions != null && currentQuestionPos != null) {
            val question = questions[currentQuestionPos]
            if (answerPosition >= 0) {
                val userAnswer = question.answers[answerPosition]
                _userAnswers[currentQuestionPos] =
                    AnswerModel(userAnswer.answer, userAnswer.isCorrect, true)
                if (userAnswer.isCorrect) {
                    incrementScore()
                }
            }
            Timber.d(_userAnswers.toString())
        }
    }
    fun moveToNextQuiz() {
        val quizzes = getCurrentQuestionList()
        var quizCurrentPosition = getCurrentQuestionPosition()

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
    fun getLogs(): MutableList<QuestionModel> {
        val newQuestions = mutableListOf<QuestionModel>()
        val questions = getCurrentQuestionList()
        if (!questions.isNullOrEmpty()) {
            for (i in questions.indices) {
                val userAnswer = _userAnswers[i]
                val answers = questions[i].answers
                val newAnswers = mutableListOf<AnswerModel>()
                for (j in answers.indices) {
                    if (userAnswer != null && answers[j].answer==userAnswer.answer) {
                        newAnswers.add(AnswerModel(userAnswer.answer, userAnswer.isCorrect, true))
                    }else{
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
    fun refresh() {
        _currentQuizSetting.value?.let { getQuiz(it) }
    }

    //DataBase Query
    fun saveQuiz(context: Context, quizName:String) {
        val dao = QuizDB.getInstance(context).quizDao
        viewModelScope.launch {
            val score = _score.value
            if (score != null) {
                val quizId = dao.insertQuiz(Quiz(quizName,score, Date()))
                val questions = getCurrentQuestionList()
                if (!questions.isNullOrEmpty()) {
                    for (i in questions.indices) {
                        val questionID = dao.insertQuestion(questions[i].asQuestionEntity(quizId))
                        val answers = questions[i].answers
                        for (j in answers.indices) {
                            if(answers[j].answer==_userAnswers[i]?.answer){
                                dao.insertAnswer(
                                    answers[j].asAnswerEntity(
                                        questionID, true
                                    )
                                )
                            }else{
                                dao.insertAnswer(
                                    answers[j].asAnswerEntity(
                                        questionID, false
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    fun getStoredUserQuizzes(context: Context) {
        val dao = QuizDB.getInstance(context).quizDao
        viewModelScope.launch {
            _quizzes.postValue(dao.findAll())
        }
    }
    fun deleteAllQuizzes(context: Context){
        val dao = QuizDB.getInstance(context).quizDao
        viewModelScope.launch {
            dao.deleteAll()
            getStoredUserQuizzes(context)
        }
    }


    //Getters
    fun getCurrentQuizzesListSize(): Int = _currentQuiz.value?.questions?.size ?: 0
    private fun getCurrentQuestionList(): List<QuestionModel>? = _currentQuiz.value?.questions
    private fun getCurrentQuestionPosition(): Int? = _currentQuestionPosition.value


}