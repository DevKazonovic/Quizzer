package com.my.projects.quizapp.presentation.history.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.my.projects.quizapp.QuizApplication
import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.CategoriesStore
import com.my.projects.quizapp.data.local.model.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.databinding.FragmentQuizDetailBinding
import com.my.projects.quizapp.presentation.ViewModelProviderFactory
import com.my.projects.quizapp.util.BundleUtil.KEY_HISTORY_QUIZ_ID
import com.my.projects.quizapp.util.DomainUtil
import com.my.projects.quizapp.util.ThemeUtil.Companion.getThemeColorAttr
import timber.log.Timber
import javax.inject.Inject

class QuizDetailFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory
    private val viewModel by viewModels<QuizDetailViewModel> {
        viewModelFactory
    }

    private lateinit var binding: FragmentQuizDetailBinding
    private lateinit var adapter: QuestionsWithAnswersAdapter
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
            Timber.d("$quiz")
            displayQuizDetails(quiz)
        })
    }

    private fun displayQuizDetails(data: QuizWithQuestionsAndAnswers?) {
        data?.let { history ->
            CategoriesStore.getCategorie(history.quizEntity.category).let { category ->
                binding.textViewCategoryName.text = category.name
                binding.imageViewCategoryIcon.setImageResource(category.icon)
            }
            binding.textViewDate.text = history.quizEntity.date.toString()
            processScore(history.questions.size, history.quizEntity.score)

            binding.recyclerViewQuestions.layoutManager = LinearLayoutManager(requireContext())
            adapter = QuestionsWithAnswersAdapter(history.questions)
            binding.recyclerViewQuestions.adapter = adapter
        }
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