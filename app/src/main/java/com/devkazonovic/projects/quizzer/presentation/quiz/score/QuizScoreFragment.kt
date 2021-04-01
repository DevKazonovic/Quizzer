package com.devkazonovic.projects.quizzer.presentation.quiz.score

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.devkazonovic.projects.quizzer.QuizApplication
import com.devkazonovic.projects.quizzer.R
import com.devkazonovic.projects.quizzer.databinding.FragmentQuizScoreBinding
import com.devkazonovic.projects.quizzer.presentation.ViewModelProviderFactory
import com.devkazonovic.projects.quizzer.presentation.quiz.QuizViewModel
import com.devkazonovic.projects.quizzer.util.DomainUtil
import com.devkazonovic.projects.quizzer.util.ThemeUtil.Companion.getThemeColorAttr
import javax.inject.Inject


class QuizScoreFragment : Fragment() {

    private lateinit var binding: FragmentQuizScoreBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory
    private val viewModel: QuizViewModel by navGraphViewModels(R.id.graph_quiz) {
        viewModelFactory
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHasOptionsMenu(true)

        (requireActivity().application as QuizApplication).component.inject(this)

        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    saveQuiz()
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizScoreBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_close -> {
                    saveQuiz()
                    true
                }
                else -> false
            }
        }
        binding.cardViewSeeAnswersSummary.setOnClickListener {
            findNavController().navigate(R.id.action_quizScore_to_quizAnswersSummary)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeDataChange()
    }

    private fun observeDataChange() {
        viewModel.score.observe(viewLifecycleOwner, { score ->
            processScore(
                viewModel.getCurrentQuizzesListSize(),
                score
            )
        })
        viewModel.isQuizSaved.observe(viewLifecycleOwner, { isSaved ->
            isSaved.getContentIfNotHandled()?.let {
                if (it) {
                    findNavController().navigate(R.id.action_graph_quiz_pop)
                } else {
                    saveQuiz()
                }
            }
        })
    }

    private fun saveQuiz() {
        viewModel.saveQuiz()
    }

    private fun processScore(total: Int, corrects: Int) {
        val percentage = DomainUtil.getScorePercentage(
            numberOfQuestions = total,
            correctAnswers = corrects
        )
        if (percentage < 50) {
            binding.textViewScore.setTextColor(
                getThemeColorAttr(requireContext(), R.attr.colorRedThings)
            )
            binding.imageViewScoreState.setImageResource(R.drawable.ic_sad)
            binding.textViewScoreMessage.text = getString(R.string.score_status_lessthen50_label)
        } else {
            binding.textViewScore.setTextColor(
                getThemeColorAttr(requireContext(), R.attr.colorGreenThings)
            )
            binding.imageViewScoreState.setImageResource(R.drawable.img_win)
            binding.textViewScoreMessage.text = getString(R.string.score_status_morethen50_label)
        }
        binding.textViewScore.text = getString(R.string.score_percentage, percentage)
        binding.textViewQuestions.text = getString(R.string.score_numberofquestion, total)
        binding.textViewCorrectAnswers.text = getString(R.string.score_correct_answers, corrects)
        binding.textViewWrongAnswers.text =
            getString(R.string.score_incorrect_answers, total - corrects)
    }

}