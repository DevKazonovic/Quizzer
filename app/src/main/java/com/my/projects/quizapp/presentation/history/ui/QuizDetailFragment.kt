package com.my.projects.quizapp.presentation.history.ui

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.local.entity.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.databinding.FragmentQuizDetailBinding
import com.my.projects.quizapp.databinding.SaveQuizLayoutBinding
import com.my.projects.quizapp.di.QuizInjector
import com.my.projects.quizapp.presentation.history.adapter.QuestionsWithAnswersAdapter
import com.my.projects.quizapp.presentation.history.controller.HistoryViewModel
import com.my.projects.quizapp.util.Const.Companion.KEY_QUIZ
import com.my.projects.quizapp.util.Const.Companion.cats
import timber.log.Timber

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
            requireActivity(),
            QuizInjector(requireActivity().application).provideHistoryViewModelFactory()
        ).get(HistoryViewModel::class.java)

        observe()
        displayDetails()
    }

    private fun observe() {
        viewModel.isQuizUpdated.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let {
                if (it) {
                    createCrudActionSnackbar("Quiz Updated successfully", it).show()
                } else {
                    createCrudActionSnackbar("UnSuccessfully Update!", it).show()
                }
            }
        })

        viewModel.isQuizDeleted.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let {
                if (it) {
                    val snackbar = createCrudActionSnackbar("Quiz Deleted successfully", it)
                    snackbar.show()
                    if (!snackbar.isShown) backToHistory()
                } else {
                    createCrudActionSnackbar("UnSuccessfully Delete!", it).show()
                }
            }
        })
    }

    private fun displayDetails() {
        cats.find { cat -> cat.id == data.quiz.category }?.let {
            binding.txtCatLabelDetail.text = it.name
            binding.catIconDetail.setImageResource(it.icon)
        }

        binding.txtTotalQuestions.text = data.questions.size.toString()
        binding.txtCorrectAnswers.text = data.quiz.score.toString()
        binding.txtWrongAnswers.text = (data.questions.size - data.quiz.score).toString()

        binding.recyclerQuestions.layoutManager = LinearLayoutManager(requireContext())
        adapter = QuestionsWithAnswersAdapter(data.questions)
        binding.recyclerQuestions.adapter = adapter
    }

    private fun backToHistory() = findNavController().navigateUp()

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

    private fun createCrudActionSnackbar(text: String, isSeccessful: Boolean): Snackbar =
        Snackbar.make(binding.coordinator, text, Snackbar.LENGTH_LONG).let {
            if (isSeccessful) {
                it.setBackgroundTint(Color.GREEN)
                it.addCallback(
                    object : Snackbar.Callback() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            super.onDismissed(transientBottomBar, event)
                            backToHistory()
                        }
                    }
                )
            } else it.setBackgroundTint(Color.RED)
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
        inflater.inflate(R.menu.menu_quiz_edit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


}