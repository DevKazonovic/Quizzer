package com.my.projects.quizapp.presentation.history.ui

import android.os.Bundle
import android.text.Editable
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.local.entity.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.databinding.FragmentQuizDetailBinding
import com.my.projects.quizapp.databinding.SaveQuizLayoutBinding
import com.my.projects.quizapp.di.QuizInjector
import com.my.projects.quizapp.presentation.history.adapter.QuestionsWithAnswersAdapter
import com.my.projects.quizapp.presentation.history.controller.HistoryViewModel
import com.my.projects.quizapp.util.Const.Companion.KEY_QUIZ

class QuizDetailFragment : Fragment() {

    private lateinit var viewModel: HistoryViewModel
    private lateinit var data: QuizWithQuestionsAndAnswers
    private lateinit var binding: FragmentQuizDetailBinding
    private lateinit var adapter: QuestionsWithAnswersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            data = it.getSerializable(KEY_QUIZ) as QuizWithQuestionsAndAnswers
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizDetailBinding.inflate(inflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            QuizInjector(requireActivity().application).provideHistoryViewModelFactory()
        ).get(HistoryViewModel::class.java)
        displayDetails()
    }

    private fun displayDetails() {
        binding.txtTotalQuestions.text = data.questions.size.toString()
        binding.txtCorrectAnswers.text = data.quiz.score.toString()
        binding.txtWrongAnswers.text = (data.questions.size - data.quiz.score).toString()

        //Setup RecyclerView
        binding.recyclerQuestions.layoutManager = LinearLayoutManager(requireContext())
        adapter = QuestionsWithAnswersAdapter(data.questions)
        binding.recyclerQuestions.adapter = adapter
    }

    private fun showSaveDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle("Quiz Name")
        }
        val layout = SaveQuizLayoutBinding.inflate(layoutInflater)
        val nameEt = layout.nameInLayout
        nameEt.editText?.text = Editable.Factory.getInstance().newEditable(data.quiz.title)

        builder.setView(layout.root)
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }.setPositiveButton("Update") { _, _ ->
            // Respond to positive button press
            val name = nameEt.editText?.text.toString()
            val quiz = data.quiz.copy(title = name).apply {
                this.id = data.quiz.id
            }
            viewModel.onQuizUpdate(quiz)
        }.show()
    }

    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(getString(R.string.all_deletealter))
        }.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }.setPositiveButton("Delete") { _, _ ->
            viewModel.onQuizDelete(data.quiz)
        }.show()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                showSaveDialog()
                return true
            }
            R.id.delete -> {
                showDeleteDialog()
                return true
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.quiz_edit_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


}