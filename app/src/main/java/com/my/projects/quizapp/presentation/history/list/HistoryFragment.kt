package com.my.projects.quizapp.presentation.history.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.my.projects.quizapp.QuizApplication
import com.my.projects.quizapp.R
import com.my.projects.quizapp.databinding.FragmentHistoryBinding
import com.my.projects.quizapp.domain.mapper.toHistoryQuiz
import com.my.projects.quizapp.domain.model.HistoryQuiz
import com.my.projects.quizapp.presentation.ViewModelProviderFactory
import com.my.projects.quizapp.util.BundleUtil.KEY_HISTORY_QUIZ_ID
import com.my.projects.quizapp.util.extensions.hide
import com.my.projects.quizapp.util.extensions.setToolbar
import com.my.projects.quizapp.util.extensions.show
import javax.inject.Inject


class HistoryFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory
    private val viewModel: HistoryViewModel by navGraphViewModels(R.id.graph_history) {
        viewModelFactory
    }
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: HistoryAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as QuizApplication).component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater)
        setToolbar(binding.toolbar)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.swipeRefreshLayout.setOnRefreshListener {
            onRefresh()
            binding.swipeRefreshLayout.isRefreshing = false
        }
        observeData()
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_clear -> {
                    showDeleteAlertDialog()
                    true
                }
                else -> false
            }
        }
    }

    private fun observeData() {
        viewModel.quizzesMediatorLiveData.observe(viewLifecycleOwner, { list ->
            onDisplayData(list.map { it.toHistoryQuiz() })
        })
    }

    private fun onDisplayData(list: List<HistoryQuiz>) {
        if (list.isNullOrEmpty()) {
            onDisplayDataSatat(getString(R.string.history_emptylist_state))
        } else {
            binding.layoutErrors.hide()
            binding.layoutData.show()
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            adapter = HistoryAdapter(QuizShowDetailListener {
                findNavController().navigate(
                    R.id.action_history_to_quizDetail,
                    bundleOf(KEY_HISTORY_QUIZ_ID to it.id)
                )
            },
                QuizDeleteListener { item, position ->
                    viewModel.onQuizDelete(item.id)
                    adapter.deleteItemAt(position)
                }
            )
            binding.recyclerView.adapter = adapter
            adapter.addHeaderAndSubmitList(list)

        }
    }

    private fun onDisplayDataSatat(message: String) {
        binding.layoutData.hide()
        binding.layoutErrors.show()
        binding.textViewError.text = message
    }

    private fun onRefresh() {
        viewModel.onRefresh()
    }

    private fun showDeleteAlertDialog() {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(getString(R.string.history_dialog_title_delete))
        }
            .setNegativeButton(getString(R.string.history_action_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.history_action_yes)) { _, _ ->
                viewModel.onDeleteAllQuizzes()
            }
            .show()
    }


}