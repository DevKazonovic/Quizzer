package com.my.projects.quizapp.presentation.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.db.entity.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.databinding.FragmentHistoryBinding
import com.my.projects.quizapp.presentation.controller.QuizViewModel
import com.my.projects.quizapp.presentation.ui.adapter.QuizzesAdapter
import com.my.projects.quizapp.presentation.ui.widgets.ThemeModeDialog
import com.my.projects.quizapp.util.Const.Companion.KEY_QUIZ
import timber.log.Timber


class HistoryFragment : Fragment() {

    private lateinit var quizViewModel: QuizViewModel
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: QuizzesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun onNavigationToDetails(data: QuizWithQuestionsAndAnswers) {
        findNavController().navigate(R.id.action_history_to_quizDetail, bundleOf(KEY_QUIZ to data))
    }
    private fun showAlertDialog(){
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle("Are you sure you want to delete it all ?")
        }.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }.setPositiveButton("Save") { dialog, which ->
            quizViewModel.deleteAllQuizzes(requireContext())
        }.show()

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        quizViewModel = ViewModelProvider(requireActivity()).get(QuizViewModel::class.java)

        quizViewModel.getStoredUserQuizzes(requireContext())

        quizViewModel.quizzes.observe(viewLifecycleOwner, {
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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clear -> {
                showAlertDialog()
                return true
            }
            R.id.mode_switch -> {
                ThemeModeDialog().show(requireActivity().supportFragmentManager, "ThemeModeDialog")
                return true
            }
        }
        return false
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.history_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}