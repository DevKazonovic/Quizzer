package com.my.projects.quizapp.presentation.quiz.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.model.QuestionModel
import com.my.projects.quizapp.databinding.FragmentScoreBinding
import com.my.projects.quizapp.databinding.SaveQuizLayoutBinding
import com.my.projects.quizapp.di.QuizInjector
import com.my.projects.quizapp.presentation.quiz.adapter.QuestionsAdapter
import com.my.projects.quizapp.presentation.quiz.controller.QuizViewModel
import timber.log.Timber

class ScoreFragment : Fragment() {
    private lateinit var scoreBinding: FragmentScoreBinding
    private lateinit var quizViewModel: QuizViewModel

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
        quizViewModel = ViewModelProvider(
            requireActivity(),
            QuizInjector(requireActivity().application).provideQuizViewModelFactory()
        ).get(QuizViewModel::class.java)

        scoreBinding.btnSave.setOnClickListener {
            createSavingDialog()
        }
        initSummaryRecyclerView()
        showHideSummary()
        observeData()
    }

    private fun observeData() {
        quizViewModel.score.observe(viewLifecycleOwner, { score ->
            scoreBinding.txtTotalQuestions.text =
                quizViewModel.getCurrentQuizzesListSize().toString()
            scoreBinding.txtCorrectAnswers.text = score.toString()
            scoreBinding.txtWrongAnswers.text =
                (quizViewModel.getCurrentQuizzesListSize() - score).toString()

        })

        quizViewModel.snackBarSaved.observe(viewLifecycleOwner,{isSaved ->
            isSaved.getContentIfNotHandled()?.let {
                if (it) {
                   showSanckBar("Quiz Saved successfully",it)
                }else{
                    showSanckBar("UnSuccessfully Save!",it)
                }
            }
        })
    }

    private fun initSummaryRecyclerView() {
        //get DataList
        list = quizViewModel.onGetQuizLogs()

        //Setup RecyclerView
        scoreBinding.recyclerQuestions.layoutManager = LinearLayoutManager(requireContext())
        adapter = QuestionsAdapter(list)
        scoreBinding.recyclerQuestions.adapter = adapter

    }

    private fun showHideSummary() {
        var isHidden = true
        scoreBinding.layoutShowLogs.setOnClickListener {
            if (isHidden) {
                scoreBinding.recyclerQuestions.visibility = VISIBLE
                scoreBinding.icShowLogs.setImageResource(R.drawable.ic_round_arrow_down)
                isHidden = false
            } else {
                scoreBinding.recyclerQuestions.visibility = GONE
                scoreBinding.icShowLogs.setImageResource(R.drawable.ic_round_arrow_right)
                isHidden = true
            }
        }
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
            quizViewModel.saveQuiz(name)
        }.show()
    }

    private fun showSanckBar(text:String,isSeccessful:Boolean)=
        Snackbar.make(requireView(),text, Snackbar.LENGTH_LONG).let {
            if(isSeccessful) it.setBackgroundTint(Color.GREEN)
            else it.setBackgroundTint(Color.RED)
        }.show()



    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHasOptionsMenu(true)

        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Timber.d("Custom Nav")
                    findNavController().navigate(R.id.action_score_to_categories)
                }
            })
    }


}