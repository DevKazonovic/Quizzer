package com.devkazonovic.projects.quizzer.presentation.history.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.devkazonovic.projects.quizzer.QuizApplication
import com.devkazonovic.projects.quizzer.R
import com.devkazonovic.projects.quizzer.data.CategoriesStore
import com.devkazonovic.projects.quizzer.databinding.FragmentQuizDetailBinding
import com.devkazonovic.projects.quizzer.domain.model.HistoryQuiz
import com.devkazonovic.projects.quizzer.domain.toQuizSummary
import com.devkazonovic.projects.quizzer.presentation.ViewModelProviderFactory
import com.devkazonovic.projects.quizzer.util.BundleUtil.KEY_HISTORY_QUIZ_ID
import com.devkazonovic.projects.quizzer.util.DomainUtil
import com.devkazonovic.projects.quizzer.util.ThemeUtil.Companion.getThemeColorAttr
import javax.inject.Inject

class QuizDetailFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory
    private val viewModel by viewModels<QuizDetailViewModel> {
        viewModelFactory
    }

    private lateinit var binding: FragmentQuizDetailBinding
    private lateinit var adapter: HistoryDetailAdapter
    private var argQuizID: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            argQuizID = it.getLong(KEY_HISTORY_QUIZ_ID)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as QuizApplication).component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewClosePage.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.quizID.value = argQuizID
        observeData()
    }

    private fun observeData() {
        viewModel.getQuizDetail().observe(viewLifecycleOwner, { quiz ->
            quiz?.let {
                displayQuizDetails(it)
            }
        })
    }

    private fun displayQuizDetails(history: HistoryQuiz) {
        CategoriesStore.findCategoryById(history.category).let { category ->
            binding.textViewCategoryName.text = category.name
            binding.imageViewCategoryIcon.setImageResource(category.icon)
        }
        binding.textViewDate.text = history.date.toString()
        processScore(history.questions.size, history.score)
        binding.recyclerViewQuestions.layoutManager = LinearLayoutManager(requireContext())
        adapter = HistoryDetailAdapter(history.questions.map { it.toQuizSummary() })
        binding.recyclerViewQuestions.adapter = adapter

    }

    private fun processScore(total: Int, corrects: Int) {
        val percentage = DomainUtil.getScorePercentage(
            numberOfQuestions = total,
            correctAnswers = corrects
        )

        if (percentage < 50)
            binding.textViewScore.setTextColor(
                getThemeColorAttr(
                    requireContext(),
                    R.attr.colorRedThings
                )
            )
        else binding.textViewScore.setTextColor(
            getThemeColorAttr(
                requireContext(),
                R.attr.colorGreenThings
            )
        )

        binding.textViewScore.text = getString(R.string.score_percentage, percentage)
        binding.textViewQuestions.text = getString(R.string.score_numberofquestion, total)
        binding.textViewCorrectAnswers.text = getString(R.string.score_correct_answers, corrects)
        binding.textViewWrongAnswers.text =
            getString(R.string.score_incorrect_answers, total - corrects)
    }


}