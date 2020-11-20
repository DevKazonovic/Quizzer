package com.my.projects.quizapp.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.my.projects.quizapp.databinding.FragmentHistoryBinding
import com.my.projects.quizapp.presentation.controller.QuizViewModel
import com.my.projects.quizapp.presentation.ui.adapter.QuizzesAdapter


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

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        quizViewModel = ViewModelProvider(requireActivity()).get(QuizViewModel::class.java)

        quizViewModel.getStoredUserQuizzes(requireContext())

        binding.btnClear.setOnClickListener {
            quizViewModel.deleteAllQuizzes(requireContext())
        }
        quizViewModel.quizzes.observe(viewLifecycleOwner, {
            //Setup RecyclerView
            binding.recyclerQuiz.layoutManager = LinearLayoutManager(requireContext())
            adapter = QuizzesAdapter(it)
            binding.recyclerQuiz.adapter = adapter
        })

    }


}