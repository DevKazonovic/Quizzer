package com.my.projects.quizapp.presentation.history.ui

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.local.entity.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.databinding.FragmentHistoryBinding
import com.my.projects.quizapp.di.QuizInjector
import com.my.projects.quizapp.presentation.history.adapter.QuizzesAdapter
import com.my.projects.quizapp.presentation.history.controller.HistoryViewModel
import com.my.projects.quizapp.util.Const.Companion.KEY_QUIZ
import timber.log.Timber


class HistoryFragment : Fragment() {

    private lateinit var viewModel: HistoryViewModel
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: QuizzesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            QuizInjector(requireActivity().application).provideHistoryViewModelFactory()
        ).get(HistoryViewModel::class.java)

        viewModel.getStoredUserQuizzes()

        viewModel.quizzes.observe(viewLifecycleOwner, {
            //Setup RecyclerView
            binding.recyclerQuiz.layoutManager = LinearLayoutManager(requireContext())
            adapter = QuizzesAdapter(it, object : QuizzesAdapter.ItemClickListener {
                override fun onItemClick(data: QuizWithQuestionsAndAnswers) {
                    Timber.d("OnItemClick")
                    onNavigationToDetails(data)
                }
            })
            binding.recyclerQuiz.adapter = adapter
        })
    }

    private fun onNavigationToDetails(data: QuizWithQuestionsAndAnswers) {
        findNavController().navigate(R.id.action_history_to_quizDetail, bundleOf(KEY_QUIZ to data))
    }

    private fun showAlertDialog() {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(getString(R.string.all_deletealter))
        }.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }.setPositiveButton("Save") { _, _ ->
            viewModel.deleteAllQuizzes()
        }.show()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clear -> {
                showAlertDialog()
                return true
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_history, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}