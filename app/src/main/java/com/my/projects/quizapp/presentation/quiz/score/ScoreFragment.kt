package com.my.projects.quizapp.presentation.quiz.score

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.my.projects.quizapp.QuizApplication
import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.model.QuestionModel
import com.my.projects.quizapp.databinding.FragmentScoreBinding
import com.my.projects.quizapp.databinding.SaveQuizLayoutBinding
import com.my.projects.quizapp.presentation.quiz.QuizViewModel
import com.my.projects.quizapp.viewmodels.ViewModelProviderFactory
import timber.log.Timber
import javax.inject.Inject


class ScoreFragment : Fragment() {

    private lateinit var scoreBinding: FragmentScoreBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory

    val viewModel: QuizViewModel by navGraphViewModels(R.id.graph_quiz) {
        viewModelFactory
    }

    private lateinit var adapter: QuestionsAdapter
    private lateinit var list: MutableList<QuestionModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        scoreBinding = FragmentScoreBinding.inflate(inflater)

        return scoreBinding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        initSummaryRecyclerView()

        observeData()

        scoreBinding.btnSave.setOnClickListener {
            createSavingDialog()
        }

    }

    private fun observeData() {
        viewModel.score.observe(viewLifecycleOwner, { score ->
            scoreBinding.txtTotalQuestions.text =
                viewModel.getCurrentQuizzesListSize().toString()
            scoreBinding.txtCorrectAnswers.text = score.toString()
            scoreBinding.txtWrongAnswers.text =
                (viewModel.getCurrentQuizzesListSize() - score).toString()

        })

        viewModel.snackBarSaved.observe(viewLifecycleOwner, { isSaved ->
            isSaved.getContentIfNotHandled()?.let {
                if (it) {
                    showSanckBar("Quiz Saved successfully", it)
                } else {
                    showSanckBar("UnSuccessfully Save!", it)
                }
            }
        })
    }

    private fun initSummaryRecyclerView() {
        //get DataList
        list = viewModel.onGetQuizLogs()

        //Setup RecyclerView
        scoreBinding.recyclerQuestions.layoutManager = LinearLayoutManager(requireContext())
        adapter = QuestionsAdapter(list)
        scoreBinding.recyclerQuestions.adapter = adapter

    }

    private fun createSavingDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle("Quiz Name")
        }
        val layout = SaveQuizLayoutBinding.inflate(layoutInflater)
        builder.setView(layout.root)
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }.setPositiveButton("Save") { dialog, which ->
            // Respond to positive button press
            val nameEt = layout.nameInLayout
            val name = nameEt.editText?.text.toString()
            viewModel.saveQuiz(name)
        }.show()
    }

    private fun showSanckBar(text: String, isSeccessful: Boolean) {
        return Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG).let {
            if (isSeccessful) it.setBackgroundTint(Color.GREEN)
            else it.setBackgroundTint(Color.RED)
        }.show()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHasOptionsMenu(true)

         ( requireActivity().application as QuizApplication).component.inject(this)

        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Timber.d("Custom Nav")
                    findNavController().navigate(R.id.action_score_to_categories)
                }
            })
    }

}