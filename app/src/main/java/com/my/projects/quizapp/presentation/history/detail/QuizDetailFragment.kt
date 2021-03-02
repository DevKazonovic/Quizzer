package com.my.projects.quizapp.presentation.history.detail

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.local.QuizDB
import com.my.projects.quizapp.data.local.entity.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.data.repository.QuizLocalRepository
import com.my.projects.quizapp.databinding.FragmentQuizDetailBinding
import com.my.projects.quizapp.databinding.SaveQuizLayoutBinding
import com.my.projects.quizapp.presentation.history.detail.adapter.QuestionsWithAnswersAdapter
import com.my.projects.quizapp.util.Const.Companion.KEY_QUIZ_ID
import com.my.projects.quizapp.util.Const.Companion.cats
import com.my.projects.quizapp.util.UiUtil
import com.my.projects.quizapp.util.converters.Converters

class QuizDetailFragment : Fragment() {

    private var quizID: Long = 0

    private lateinit var newViewModel: QuizDetailViewModel

    private lateinit var binding: FragmentQuizDetailBinding
    private lateinit var adapter: QuestionsWithAnswersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            quizID = it.getLong(KEY_QUIZ_ID)
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
        initViewModel()
        observeData()
    }

    private fun initViewModel() {
        val repository = QuizLocalRepository(QuizDB.getInstance(requireContext()))
        val factory = QuizDetailViewModelFactory(
            repository,
            quizID
        )
        newViewModel = ViewModelProvider(this, factory).get(QuizDetailViewModel::class.java)
    }

    private fun observeData() {
        newViewModel.getQuizDetail().observe(viewLifecycleOwner, { quiz ->
            displayDetails(quiz)
        })

        newViewModel.isQuizUpdated.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let {
                if (it) {
                    createCrudActionSnackbar("Quiz Updated successfully", it).show()
                    newViewModel.refresh()
                } else {
                    createCrudActionSnackbar("UnSuccessfully Update!", it).show()
                }
            }
        })

        newViewModel.isQuizDeleted.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let {
                if (it) {
                    val snackbar =
                        createCrudActionSnackbarWithCallBack("Quiz Deleted successfully", it)
                    snackbar.show()
                    if (!snackbar.isShown) backToHistory()
                } else {
                    createCrudActionSnackbar("UnSuccessfully Delete!", it).show()
                }
            }
        })
    }

    private fun displayDetails(data: QuizWithQuestionsAndAnswers) {

        val activity = requireActivity() as AppCompatActivity

        activity.supportActionBar?.title = data.quiz.title
        activity.supportActionBar?.subtitle = Converters.noTimeDateToString(data.quiz.date.time)

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

    private fun showUpdateDialog() {
        val currentQuiz = newViewModel.getCuurentQuiz()

        val builder = MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle("Quiz Name")
        }
        val layout = SaveQuizLayoutBinding.inflate(layoutInflater)
        val nameEt = layout.nameInLayout

        nameEt.editText?.text = UiUtil.getEditbaleInstance().newEditable(currentQuiz?.title)

        builder.setView(layout.root)
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
            .setPositiveButton("Update") { _, _ ->
                val name = nameEt.editText?.text.toString()
                val quiz = currentQuiz?.copy(title = name)?.apply {
                    this.id = currentQuiz.id
                }?.let {
                    newViewModel.onQuizUpdate(it)
                }
            }
            .show()
    }

    private fun showDeleteDialog() {
        val currentQuiz = newViewModel.getCuurentQuiz()

        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(getString(R.string.all_deletealter))
        }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Delete") { _, _ ->
                currentQuiz?.let { newViewModel.onQuizDelete(it) }
            }
            .show()

    }

    private fun createCrudActionSnackbarWithCallBack(
        text: String,
        isSeccessful: Boolean
    ): Snackbar = Snackbar.make(binding.coordinator, text, Snackbar.LENGTH_LONG).let {
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

    private fun createCrudActionSnackbar(
        text: String,
        isSeccessful: Boolean
    ): Snackbar = Snackbar.make(binding.coordinator, text, Snackbar.LENGTH_LONG).let {
        if (isSeccessful) {
            it.setBackgroundTint(Color.GREEN)
        } else it.setBackgroundTint(Color.RED)
    }


    //Callbacks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                showUpdateDialog()
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