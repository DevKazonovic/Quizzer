package com.my.projects.quizapp.presentation.history.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.local.QuizDB
import com.my.projects.quizapp.data.local.entity.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.data.model.SortBy
import com.my.projects.quizapp.data.repository.QuizRepositoryImpl
import com.my.projects.quizapp.databinding.FragmentHistoryBinding
import com.my.projects.quizapp.presentation.history.adapter.QuizzesAdapter
import com.my.projects.quizapp.presentation.history.controller.HistoryViewModel
import com.my.projects.quizapp.presentation.history.controller.HistoryViewModelFactory
import com.my.projects.quizapp.presentation.quiz.util.Const.Companion.KEY_QUIZ_ID
import com.my.projects.quizapp.util.extensions.hide
import com.my.projects.quizapp.util.extensions.show
import timber.log.Timber


class HistoryFragment : Fragment() {

    private val viewModel: HistoryViewModel by navGraphViewModels(R.id.graph_history_list) {
        HistoryViewModelFactory(QuizRepositoryImpl(QuizDB.getInstance(requireContext())))
    }
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

        binding.swipeRefresh.setOnRefreshListener {
            refresh()
            binding.swipeRefresh.isRefreshing = false
        }
        startObserve()
    }

    private fun startObserve() {
        viewModel.quizzesMediatorLiveData.observe(viewLifecycleOwner, {
            displayData(it)
        })


    }

    private fun displayData(list: List<QuizWithQuestionsAndAnswers>) {
        //Setup RecyclerView
        if (list.isEmpty()) {
            binding.lblEmptyList.show()
        } else {
            binding.lblEmptyList.hide()
            binding.recyclerQuiz.layoutManager = LinearLayoutManager(requireContext())
            adapter = QuizzesAdapter(list, object : QuizzesAdapter.ItemClickListener {
                override fun onItemClick(data: QuizWithQuestionsAndAnswers) {
                    navigateToDetailPage(data.quiz.id)
                }
            })
            binding.recyclerQuiz.adapter = adapter
        }
    }

    private fun refresh() {
        viewModel.onRefresh()
    }

    private fun navigateToDetailPage(data: QuizWithQuestionsAndAnswers) {
        findNavController().navigate(
            R.id.action_history_to_quizDetail,
            bundleOf(KEY_QUIZ_ID to data)
        )
    }

    private fun navigateToDetailPage(quizID: Long) {
        findNavController().navigate(
            R.id.action_history_to_quizDetail,
            bundleOf(KEY_QUIZ_ID to quizID)
        )
    }

    private fun showAlertDialog() {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(getString(R.string.all_deletealter))
        }.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }.setPositiveButton("Save") { _, _ ->
            viewModel.onDeleteAllQuizzes()
        }.show()
    }

    private fun showSortByDialog() {
        val sortByItems = arrayOf(SortBy.LATEST, SortBy.OLDEST, SortBy.TITLE)
        val itemsForDialog = sortByItems.map { item -> item.name }.toTypedArray()
        var checkedItem = sortByItems.indexOf(viewModel.getCurrentSortBy())

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.dialog_title_sort))
            .setNeutralButton(resources.getString(R.string.dialog_action_title_cancle)) { dialog, which ->
                Timber.d("Cancle")
            }
            .setPositiveButton(resources.getString(R.string.dialog_action_title_ok)) { dialog, which ->
                Timber.d("OK: $which")
                viewModel.onSortBy(sortByItems[checkedItem])
            }
            .setSingleChoiceItems(itemsForDialog, checkedItem) { dialog, which ->
                checkedItem = which
            }
            .show()
    }

    private fun showFilterDialog() {
        /*FilterDialogFragment().show(
            requireActivity().supportFragmentManager,
            "FilterDialog"
        )*
         */

        findNavController().navigate(R.id.action_history_to_dialogHistoryFilter)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_clear -> {
                showAlertDialog()
                return true
            }
            R.id.action_sort -> {
                showSortByDialog()
                return true
            }
            R.id.action_filter -> {
                showFilterDialog()
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_history, menu)
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.onSubmitSearch(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.onInstantSearch(newText)
                return false
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }
}