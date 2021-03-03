package com.my.projects.quizapp.presentation.quiz.quizplay

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
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

    var visible = true

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory

    val viewModel: QuizViewModel by navGraphViewModels(R.id.graph_quiz) {
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
        hideSystemUI()

        quizBinding.root.setOnSystemUiVisibilityChangeListener { visibility ->
            // Note that system bars will only be "visible" if none of the
            // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                Timber.d("The system bars are visible")
                lifecycleScope.launch {
                    delay(1000)
                    hideSystemUI()
                }
            }
        }

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

    private fun observeDataChange() {

        viewModel.currentQuestion.observe(viewLifecycleOwner, {
            hideErrorLayout()
            hideProgressBar()
            displayQuestion(it)
        })

        viewModel.currentQuestionPosition.observe(viewLifecycleOwner, {
            updateProgress(it)
        })

        viewModel.dataState.observe(viewLifecycleOwner, { state ->
            when (state) {
                DataState.Loading -> showProgressBar()
                DataState.Success -> {
                    hideErrorLayout()
                    hideProgressBar()
                    setQuizProgress()
                }
                is DataState.Error -> handleError(state.error)
                is DataState.NetworkException -> handleError(state.error)
                is DataState.HttpErrors.NoResults -> handleError(state.exception)
                is DataState.HttpErrors.InvalidParameter -> handleError(state.exception)
                is DataState.HttpErrors.TokenNotFound -> handleError(state.exception)
                is DataState.HttpErrors.TokenEmpty -> handleError(state.exception)
            }
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

    private fun setQuizProgress() {
        quizBinding.quizProgress.max = viewModel.getCurrentQuizzesListSize()
    }

    private fun updateProgress(p: Int) {
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

    private fun hideSystemUI() {
        quizBinding.root.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

}