package com.my.projects.quizapp.presentation.history.detail

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.my.projects.quizapp.MainActivity
import com.my.projects.quizapp.QuizApplication
import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.local.model.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.databinding.ActivityMainBinding
import com.my.projects.quizapp.databinding.FragmentQuizDetailBinding
import com.my.projects.quizapp.databinding.SaveQuizLayoutBinding
import com.my.projects.quizapp.presentation.ViewModelProviderFactory
import com.my.projects.quizapp.presentation.history.detail.adapter.QuestionsWithAnswersAdapter
import com.my.projects.quizapp.util.Const.Companion.KEY_QUIZ_ID
import com.my.projects.quizapp.util.Const.Companion.cats
import com.my.projects.quizapp.util.UiUtil
import com.my.projects.quizapp.util.converters.Converters
import com.my.projects.quizapp.util.extensions.hideKeyboard
import com.my.projects.quizapp.util.extensions.setColor
import com.my.projects.quizapp.util.extensions.setColorWithCallback
import javax.inject.Inject

class QuizDetailFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory
    private lateinit var viewModel: QuizDetailViewModel
    private var quizID: Long = 0

    private lateinit var binding: FragmentQuizDetailBinding
    private lateinit var mainActivityBinding: ActivityMainBinding


    private lateinit var adapter: QuestionsWithAnswersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            quizID = it.getLong(KEY_QUIZ_ID)
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
        val activity = (activity as MainActivity)
        setHasOptionsMenu(true)
        mainActivityBinding = activity.mainBinding
        activity.supportActionBar?.title = " "
        activity.supportActionBar?.subtitle = " "
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        observeData()
    }

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

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(QuizDetailViewModel::class.java)
        viewModel.quizID.value = quizID
    }

    private fun observeData() {
        viewModel.getQuizDetail().observe(viewLifecycleOwner, { quiz ->
            onDisplayQuizDetails(quiz)
        })

        viewModel.isQuizUpdated.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let {
                hideKeyBoared()
                if (it) {
                    snackbar(getString(R.string.snackbar_success_update), it).show()
                    viewModel.refresh()
                } else {
                    snackbar(getString(R.string.snackbar_failure_update), it).show()
                }
            }
        })

        viewModel.isQuizDeleted.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let {
                hideKeyBoared()
                if (it) {
                    val snackbar =
                        snackbarWithCallBack(getString(R.string.snackbar_success_delete), it)
                    snackbar.show()
                    if (!snackbar.isShown) onNavigateUp()
                } else {
                    snackbar(getString(R.string.snackbar_failure_delete), it).show()
                }
            }
        })
    }

    private fun onDisplayQuizDetails(data: QuizWithQuestionsAndAnswers) {

        val activity = (activity as MainActivity)
        activity.supportActionBar?.title = data.quizEntity.title
        activity.supportActionBar?.subtitle =
            Converters.noTimeDateToString(data.quizEntity.date.time)

        cats.find { cat -> cat.id == data.quizEntity.category }?.let {
            binding.txtViewQuizDetailCategoryLabel.text = it.name
            binding.imageViewQuizDetailCategoryIcon.setImageResource(it.icon)
        }

        binding.txtViewQuizDetailTotalQuestions.text = data.questions.size.toString()
        binding.txtViewQuizDetailTotalCorrectAnswers.text = data.quizEntity.score.toString()
        binding.txtViewQuizDetailTotalWrongAnswers.text =
            (data.questions.size - data.quizEntity.score).toString()

        binding.recyclerViewQuizDetailQuizUserAnswers.layoutManager =
            LinearLayoutManager(requireContext())
        adapter = QuestionsWithAnswersAdapter(data.questions)
        binding.recyclerViewQuizDetailQuizUserAnswers.adapter = adapter

    }

    private fun onNavigateUp() = findNavController().navigateUp()

    private fun showUpdateDialog() {
        val currentQuiz = viewModel.getCuurentQuiz()

        val builder = MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle("Quiz Name")
        }
        val layout = SaveQuizLayoutBinding.inflate(layoutInflater)
        val nameEt = layout.txtInputLayoutQuizName

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
                    viewModel.onQuizUpdate(it)
                }
            }
            .setOnDismissListener { hideKeyBoared() }
            .show()
    }

    private fun showDeleteDialog() {
        val currentQuiz = viewModel.getCuurentQuiz()

        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(getString(R.string.dialog_title_delete))
        }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Delete") { _, _ ->
                currentQuiz?.let { viewModel.onQuizDelete(it) }
            }
            .setOnDismissListener {
                hideKeyBoared()
            }
            .show()

    }

    private fun snackbarWithCallBack(text: String, isSeccessful: Boolean): Snackbar =
        Snackbar.make(mainActivityBinding.root, text, Snackbar.LENGTH_LONG).let {
            it.setColorWithCallback(isSeccessful, requireContext()) { onNavigateUp() }
            it.setAnchorView(mainActivityBinding.fabMain)
        }

    private fun snackbar(text: String, isSeccessful: Boolean): Snackbar =
        Snackbar.make(mainActivityBinding.root, text, Snackbar.LENGTH_LONG).let {
            it.setColor(isSeccessful, requireContext())
            it.setAnchorView(mainActivityBinding.fabMain)
        }

    private fun hideKeyBoared() {
        (activity as MainActivity).hideKeyboard()
    }

}