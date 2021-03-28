package com.my.projects.quizapp.presentation.quiz.score

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavDestination
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.my.projects.quizapp.QuizApplication
import com.my.projects.quizapp.R
import com.my.projects.quizapp.databinding.FragmentQuizScoreBinding
import com.my.projects.quizapp.databinding.SaveQuizLayoutBinding
import com.my.projects.quizapp.domain.model.Question
import com.my.projects.quizapp.presentation.ViewModelProviderFactory
import com.my.projects.quizapp.presentation.quiz.QuizViewModel
import com.my.projects.quizapp.util.UiUtil
import com.my.projects.quizapp.util.extensions.setColor
import timber.log.Timber
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
                    saveQuizToHistory()
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
                    saveQuizToHistory()
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
        observeData()
    }

    private fun observeData() {
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
                    saveQuizToHistory()
                }
            }
        })
    }
    private fun saveQuizToHistory(){
        viewModel.saveQuiz()
    }

    private fun showSnackBar(text: String, isSeccessful: Boolean) {
        return Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG)
            .setColor(isSeccessful, requireContext())
            .show()
    }
    private fun processScore(total:Int, corrects:Int){
        binding.textViewQuestions.text = "$total Questions"
        binding.textViewCorrectAnswers.text = "$corrects Correct Answers"
        binding.textViewWrongAnswers.text = "${(total - corrects)} InCorrect Answers"
        val pers = ((corrects.toDouble()/total)*100).toInt()
        binding.textViewScore.text = "$pers% Score"
        if(pers<50) {
            binding.textViewScore.setTextColor(UiUtil.getThemeColorAttr(requireContext(),R.attr.colorRedThings))
            binding.imageViewScoreState.setImageResource(R.drawable.ic_sad)
            binding.textViewScoreMessage.text = "Good Luck next Time"
        }
        else {
            binding.textViewScore.setTextColor(UiUtil.getThemeColorAttr(requireContext(),R.attr.colorGreenThings))
            binding.imageViewScoreState.setImageResource(R.drawable.img_win)
            binding.textViewScoreMessage.text = "Congrats!"
        }

    }

}