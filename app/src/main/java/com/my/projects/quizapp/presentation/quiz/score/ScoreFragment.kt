package com.my.projects.quizapp.presentation.quiz.score

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.my.projects.quizapp.MainActivity
import com.my.projects.quizapp.QuizApplication
import com.my.projects.quizapp.R
import com.my.projects.quizapp.databinding.ActivityMainBinding
import com.my.projects.quizapp.databinding.FragmentScoreBinding
import com.my.projects.quizapp.databinding.SaveQuizLayoutBinding
import com.my.projects.quizapp.domain.model.Question
import com.my.projects.quizapp.presentation.ViewModelProviderFactory
import com.my.projects.quizapp.presentation.quiz.QuizViewModel
import com.my.projects.quizapp.util.extensions.setColor
import timber.log.Timber
import javax.inject.Inject


class ScoreFragment : Fragment() {

    private lateinit var scoreBinding: FragmentScoreBinding
    private lateinit var mainActivityBinding: ActivityMainBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory
    private val viewModel: QuizViewModel by navGraphViewModels(R.id.graph_quiz) {
        viewModelFactory
    }

    private lateinit var adapter: QuestionsAdapter
    private lateinit var list: MutableList<Question>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHasOptionsMenu(true)

        (requireActivity().application as QuizApplication).component.inject(this)

        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Timber.d("Custom Nav")
                    findNavController().navigate(R.id.action_score_to_categories)
                }
            })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        scoreBinding = FragmentScoreBinding.inflate(inflater)
        setHasOptionsMenu(true)
        mainActivityBinding = (activity as MainActivity).mainBinding
        return scoreBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initSummaryRecyclerView()

        observeData()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                showSaveDialog()
                return true
            }

        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_score_save, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    private fun observeData() {
        viewModel.score.observe(viewLifecycleOwner, { score ->
            scoreBinding.textViewQuestions.text = viewModel.getCurrentQuizzesListSize().toString()
            scoreBinding.textViewCorrectAnswers.text = score.toString()
            scoreBinding.textViewWrongAnswers.text =
                (viewModel.getCurrentQuizzesListSize() - score).toString()

        })
        viewModel.snackBarSaved.observe(viewLifecycleOwner, { isSaved ->
            isSaved.getContentIfNotHandled()?.let {
                if (it) {
                    showSnackBar(getString(R.string.snackbar_success_save), it)
                } else {
                    showSnackBar(getString(R.string.snackbar_failure_save), it)
                }
            }
        })
    }

    private fun initSummaryRecyclerView() {
        list = viewModel.onCurrentQuizSummary()
        scoreBinding.recyclerViewUserAnswers.layoutManager =
            LinearLayoutManager(requireContext())
        adapter = QuestionsAdapter(list)
        scoreBinding.recyclerViewUserAnswers.adapter = adapter
    }

    private fun showSaveDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle("Quiz Name")
        }
        val layout = SaveQuizLayoutBinding.inflate(layoutInflater)
        builder.setView(layout.root)
        builder
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Save") { dialog, which ->
                val nameEt = layout.textInputLayoutQuizName
                val name = nameEt.editText?.text.toString()
                viewModel.saveQuiz(name)
            }
            .show()
    }

    private fun showSnackBar(text: String, isSeccessful: Boolean) {
        val activity = activity as MainActivity
        return Snackbar.make(activity.mainBinding.root, text, Snackbar.LENGTH_LONG)
            .setColor(isSeccessful, requireContext())
            .show()
    }

}