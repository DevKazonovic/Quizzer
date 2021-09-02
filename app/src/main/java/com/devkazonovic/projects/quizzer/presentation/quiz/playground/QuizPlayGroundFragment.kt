package com.devkazonovic.projects.quizzer.presentation.quiz.playground

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.devkazonovic.projects.quizzer.QuizApplication
import com.devkazonovic.projects.quizzer.R
import com.devkazonovic.projects.quizzer.data.CategoriesStore
import com.devkazonovic.projects.quizzer.databinding.FragmentQuizPlaygroundBinding
import com.devkazonovic.projects.quizzer.domain.manager.AppSettingManager
import com.devkazonovic.projects.quizzer.domain.model.Question
import com.devkazonovic.projects.quizzer.presentation.ViewModelProviderFactory
import com.devkazonovic.projects.quizzer.presentation.common.widgets.LogsRadioButtons.Companion.getAnswerRadio
import com.devkazonovic.projects.quizzer.presentation.common.widgets.LogsRadioButtons.Companion.layoutParams
import com.devkazonovic.projects.quizzer.presentation.quiz.QuizViewModel
import com.devkazonovic.projects.quizzer.util.ThemeUtil
import com.devkazonovic.projects.quizzer.util.extensions.hide
import com.devkazonovic.projects.quizzer.util.extensions.hideSystemUI
import com.devkazonovic.projects.quizzer.util.extensions.show
import com.devkazonovic.projects.quizzer.util.extensions.showSystemUI
import com.devkazonovic.projects.quizzer.util.wrappers.DataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class QuizPlayGroundFragment : Fragment() {

    private lateinit var binding: FragmentQuizPlaygroundBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory
    private val viewModel: QuizViewModel by navGraphViewModels(R.id.graph_quiz) {
        viewModelFactory
    }

    @Inject
    lateinit var appSettingManager: AppSettingManager

    val callback =  object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.onStop()
            findNavController().navigateUp()
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as QuizApplication).component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onStop() {
        callback.remove()
        super.onStop()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizPlaygroundBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUiVisibilityListener()

        binding.btnNext.setOnClickListener {
            viewModel.onMoveToNextQuiz()
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

        binding.viewClosePage.setOnClickListener {
            viewModel.onStop()
            findNavController().navigateUp()
        }

        binding.layoutErrors.setOnRefreshListener {
            viewModel.onReferesh()
            binding.layoutErrors.isRefreshing = false
        }

        observeDataChange()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        showSystemUI()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            requireActivity().window.statusBarColor = ThemeUtil.getThemeColorAttr(
                requireContext(),
                android.R.attr.statusBarColor
            )
        }
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
            }
        })

        viewModel.currentQuestion.observe(viewLifecycleOwner, {
            displayQuestion(it)
        })

        viewModel.currentQuestionPosition.observe(viewLifecycleOwner, {
            updateProgressBar(it)
        })

        viewModel.currentQuizSetting.observe(viewLifecycleOwner, { quizSetting ->
            quizSetting.category?.let { categoryID ->
                val category = CategoriesStore.findCategoryById(categoryID)
                binding.textViewCategoryName.text = category.name
                binding.imageViewCategoryIcon.setImageResource(category.icon)
            }
        })

        viewModel.countDown.observe(viewLifecycleOwner, { countDown ->
            binding.textViewQuestionCountDown.text = getString(
                R.string.quiz_countdown_placeholder,
                countDown
            )
        })

        viewModel.isQuizFinished.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let {
                if (it) findNavController().navigate(R.id.action_quiz_to_score)
            }
        })

    }

    private fun updateProgressBar(p: Int) {
        binding.progressBarQuizQuestion.progress = p
        binding.textViewQuestionNumber.text = getString(
            R.string.quiz_progress_placeholder,
            p,
            viewModel.getCurrentQuizzesListSize()
        )
    }

    private fun displayQuestion(question: Question) {
        var id = 0
        binding.radiogroupCardquestionAnswerchoices.clearCheck()
        binding.radiogroupCardquestionAnswerchoices.removeAllViews()
        binding.txtviewCardquestionQuestion.text = question.question
        question.answers.forEach {
            binding.radiogroupCardquestionAnswerchoices.addView(
                getAnswerRadio(requireContext(), it.answer, id),
                layoutParams
            )
            id++
        }

        binding.radiogroupCardquestionAnswerchoices.setOnCheckedChangeListener { _, checkedId ->
            viewModel.onQuestionAnswered(checkedId)
        }
    }

    private fun onSuccess() {
        hideSystemUI()
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        binding.progressBar.hide()
        binding.layoutErrors.hide()
        binding.layoutData.show()
        binding.progressBarQuizQuestion.max = viewModel.getCurrentQuizzesListSize()
    }

    private fun onError(message: Int) {
        showSystemUI()
        (activity as AppCompatActivity).supportActionBar?.show()
        binding.progressBar.hide()
        binding.layoutData.hide()
        binding.layoutErrors.show()
        binding.textViewError.text = getString(message)
    }

    private fun onLoading() {
        hideSystemUI()
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        binding.layoutData.hide()
        binding.layoutErrors.hide()
        binding.progressBar.show()
    }

    private fun hideSystemUI() {
        controlWindowInsets(true)
    }

    private fun showSystemUI() {
        controlWindowInsets(false)
    }

    private fun controlWindowInsets(hide: Boolean) {
        // WindowInsetsController can hide or show specified system bars.
        val insetsController = WindowInsetsControllerCompat(
            requireActivity().window,
            requireActivity().window.decorView
        )

        // The behavior of the immersive mode.
        val behavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // The type of system bars to hide or show.
        val type = WindowInsetsCompat.Type.systemBars()
        insetsController.systemBarsBehavior = behavior
        if (hide) {
            insetsController.hide(type)
        } else {
            insetsController.show(type)
        }
    }

    private fun setUiVisibilityListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            binding.root.setOnApplyWindowInsetsListener { _, insets ->
                if (insets.isVisible(4)) {
                    lifecycleScope.launch {
                        delay(2000)
                        hideSystemUI()
                    }
                }
                insets
            }
        }
    }



}