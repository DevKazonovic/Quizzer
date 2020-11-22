package com.my.projects.quizapp.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.my.projects.quizapp.R
import com.my.projects.quizapp.model.QuestionModel
import com.my.projects.quizapp.model.QuizSetting
import com.my.projects.quizapp.databinding.FragmentQuizBinding
import com.my.projects.quizapp.presentation.controller.QuizInjector
import com.my.projects.quizapp.presentation.controller.QuizViewModel
import com.my.projects.quizapp.presentation.ui.widgets.LogsRadioButtons.Companion.getAnswerRadio
import com.my.projects.quizapp.presentation.ui.widgets.LogsRadioButtons.Companion.layoutParams
import com.my.projects.quizapp.util.Const.Companion.KEY_QUIZ_SETTING
import com.my.projects.quizapp.util.extensions.hide
import com.my.projects.quizapp.util.extensions.show
import com.my.projects.quizapp.util.wrappers.DataState
import timber.log.Timber


class QuizFragment : Fragment() {

    private lateinit var quizBinding: FragmentQuizBinding
    private lateinit var quizViewModel: QuizViewModel
    private lateinit var setting: QuizSetting


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            setting = it.getSerializable(KEY_QUIZ_SETTING) as QuizSetting
            Timber.d(setting.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        quizBinding = FragmentQuizBinding.inflate(inflater)

        quizBinding.btnNext.setOnClickListener {
            quizViewModel.onQuestionAnswered(quizBinding.radioGroupAnswer.checkedRadioButtonId)
            quizViewModel.moveToNextQuiz()
        }

        quizBinding.swipeRefreshLayout.setOnRefreshListener {
            quizViewModel.refresh()
            quizBinding.swipeRefreshLayout.isRefreshing = false
        }

        return quizBinding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //val quizViewModelFactory=QuizViewModelFactory(setting)
        quizViewModel = ViewModelProvider(
            requireActivity(),
            QuizInjector(requireActivity().application).provideQuizViewModelFactory()
        ).get(QuizViewModel::class.java)

        observeDataChange()
    }

    private fun observeDataChange() {
        quizViewModel.currentQuestion.observe(viewLifecycleOwner, {
            hideErrorLayout()
            hideProgressBar()
            displayQuestion(it)
        })

        quizViewModel.currentQuestionPosition.observe(viewLifecycleOwner, {
            updateProgress(it)
        })

        quizViewModel.dataState.observe(viewLifecycleOwner, { state ->
            when (state) {
                DataState.Loading -> showProgressBar()
                DataState.Success -> setQuizProgress()
                is DataState.Error -> handleError(state.error)
                is DataState.NetworkException -> handleError(state.error)
                is DataState.HttpErrors.NoResults -> handleError(state.exception)
                is DataState.HttpErrors.InvalidParameter -> handleError(state.exception)
                is DataState.HttpErrors.TokenNotFound -> handleError(state.exception)
                is DataState.HttpErrors.TokenEmpty -> handleError(state.exception)
            }
        })

        quizViewModel.navigateToScore.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let {
                if (it) navigateToScorePage()
            }
        })
    }

    private fun setQuizProgress() {
        quizBinding.quizProgress.max = quizViewModel.getCurrentQuizzesListSize()
    }

    private fun updateProgress(p: Int) {
        quizBinding.quizProgress.progress = p
        quizBinding.quizNumber.text = getString(
            R.string.quiz_progressplaceholder,
            p,
            quizViewModel.getCurrentQuizzesListSize()
        )
    }

    private fun displayQuestion(question: QuestionModel) {
        var id = 0
        quizBinding.radioGroupAnswer.clearCheck()
        quizBinding.radioGroupAnswer.removeAllViews()
        quizBinding.txtQuestion.text = question.question

        question.answers.forEach {
            quizBinding.radioGroupAnswer.addView(
                getAnswerRadio(
                    requireContext(),
                    it.answer,
                    id
                ), layoutParams
            )
            id++
        }
    }

    private fun navigateToScorePage() {
        findNavController().navigate(R.id.action_quiz_to_score)
    }

    private fun handleError(message: Int) {
        showErrorLayout()
        quizBinding.errorMessageText.text = getString(message)
    }

    private fun handleError(message: String) {
        showErrorLayout()
        quizBinding.errorMessageText.text = message
    }

    private fun showErrorLayout() {
        hideProgressBar()
        quizBinding.emptyViewLinear.show()
    }

    private fun hideErrorLayout() {
        quizBinding.emptyViewLinear.hide()
    }

    private fun showProgressBar() {
        quizBinding.progressBar.visibility = VISIBLE
    }

    private fun hideProgressBar() {
        quizBinding.progressBar.visibility = GONE
    }
}