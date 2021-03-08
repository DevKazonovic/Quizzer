package com.my.projects.quizapp.presentation.quiz.quizplay

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.my.projects.quizapp.QuizApplication
import com.my.projects.quizapp.R
import com.my.projects.quizapp.databinding.FragmentQuizBinding
import com.my.projects.quizapp.domain.model.Question
import com.my.projects.quizapp.domain.model.QuizSetting
import com.my.projects.quizapp.presentation.ViewModelProviderFactory
import com.my.projects.quizapp.presentation.common.widgets.LogsRadioButtons.Companion.getAnswerRadio
import com.my.projects.quizapp.presentation.common.widgets.LogsRadioButtons.Companion.layoutParams
import com.my.projects.quizapp.presentation.quiz.QuizViewModel
import com.my.projects.quizapp.util.Const.Companion.KEY_QUIZ_SETTING
import com.my.projects.quizapp.util.extensions.hide
import com.my.projects.quizapp.util.extensions.show
import com.my.projects.quizapp.util.wrappers.DataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


class QuizFragment : Fragment() {

    private lateinit var quizBinding: FragmentQuizBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory
    private val viewModel: QuizViewModel by navGraphViewModels(R.id.graph_quiz) {
        viewModelFactory
    }
    private lateinit var setting: QuizSetting

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            setting = it.getSerializable(KEY_QUIZ_SETTING) as QuizSetting
            Timber.d(setting.toString())
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as QuizApplication).component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        quizBinding = FragmentQuizBinding.inflate(inflater)

        setHasOptionsMenu(true)
        setUiVisibilityListener()

        quizBinding.btnNext.setOnClickListener {
            viewModel.onMoveToNextQuiz()
        }
        quizBinding.btnStop.setOnClickListener {
            viewModel.onStop()
            findNavController().navigateUp()
        }

        return quizBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeDataChange()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> {
                viewModel.onReferesh()
                return true
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_refresh, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        activity?.window?.decorView?.systemUiVisibility = 0
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }

    private fun observeDataChange() {

        viewModel.dataState.observe(viewLifecycleOwner, { state ->
            when (state) {
                is DataState.Loading -> onLoading()
                is DataState.Success -> onSuccess()
                is DataState.Error -> onError(state.error)
                is DataState.NetworkException -> onError(state.error)
                is DataState.HttpErrors.NoResults -> onError(state.exception)
                is DataState.HttpErrors.InvalidParameter -> onError(state.exception)
                is DataState.HttpErrors.TokenNotFound -> onError(state.exception)
                is DataState.HttpErrors.TokenEmpty -> onError(state.exception)
            }
        })

        viewModel.currentQuestion.observe(viewLifecycleOwner, {
            displayQuestion(it)
        })

        viewModel.currentQuestionPosition.observe(viewLifecycleOwner, {
            updateProgressBar(it)
        })

        viewModel.countDown.observe(viewLifecycleOwner, { counDown ->
            quizBinding.lblCountDown.text = counDown.toString()
        })

        viewModel.navigateToScore.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let {
                if (it) navigateToScorePage()
            }
        })

    }

    private fun setupQuizProgressBar() {
        quizBinding.quizProgress.max = viewModel.getCurrentQuizzesListSize()
    }

    private fun updateProgressBar(p: Int) {
        quizBinding.quizProgress.progress = p
        quizBinding.quizNumber.text = getString(
            R.string.quiz_progressplaceholder,
            p,
            viewModel.getCurrentQuizzesListSize()
        )
    }

    private fun displayQuestion(question: Question) {
        var id = 0
        quizBinding.radioGroupAnswer.clearCheck()
        quizBinding.radioGroupAnswer.removeAllViews()
        quizBinding.txtQuestion.text = question.question
        question.answers.forEach {
            quizBinding.radioGroupAnswer.addView(
                getAnswerRadio(requireContext(), it.answer, id),
                layoutParams
            )
            id++
        }

        quizBinding.radioGroupAnswer.setOnCheckedChangeListener { _, checkedId ->
            viewModel.onQuestionAnswered(checkedId)
        }
    }

    private fun navigateToScorePage() {
        findNavController().navigate(R.id.action_quiz_to_score)
    }

    private fun onSuccess() {
        hideSystemUI()
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        quizBinding.progressBar.hide()
        quizBinding.emptyViewLinear.hide()
        setupQuizProgressBar()
    }

    private fun onError(message: Int) {
        showSystemUI()
        (activity as AppCompatActivity).supportActionBar?.show()
        quizBinding.progressBar.hide()
        quizBinding.emptyViewLinear.show()
        quizBinding.errorMessageText.text = getString(message)
    }

    private fun onLoading() {
        hideSystemUI()
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        quizBinding.emptyViewLinear.hide()
        quizBinding.progressBar.show()
    }

    private fun hideSystemUI() {
        val flags =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        activity?.window?.decorView?.systemUiVisibility = flags
    }

    private fun showSystemUI() {
        (activity as AppCompatActivity).window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    private fun setUiVisibilityListener() {
        quizBinding.root.setOnSystemUiVisibilityChangeListener { visibility ->
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                lifecycleScope.launch {
                    delay(2000)
                    hideSystemUI()
                }
            }
        }
    }


}