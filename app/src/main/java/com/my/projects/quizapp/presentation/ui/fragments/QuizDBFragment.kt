package com.my.projects.quizapp.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.DataState
import com.my.projects.quizapp.data.db.entity.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.data.model.QuestionModel
import com.my.projects.quizapp.data.model.QuizSetting
import com.my.projects.quizapp.databinding.FragmentQuizBinding
import com.my.projects.quizapp.databinding.FragmentQuizDbBinding
import com.my.projects.quizapp.presentation.controller.QuizViewModel
import com.my.projects.quizapp.presentation.ui.adapter.QuestionsAdapter
import com.my.projects.quizapp.presentation.ui.adapter.QuizzesAdapter
import com.my.projects.quizapp.util.Const.Companion.KEY_QUIZ_SETTING
import com.my.projects.quizapp.util.extensions.*
import timber.log.Timber


class QuizDBFragment : Fragment() {
    private lateinit var quizViewModel: QuizViewModel
    private lateinit var binding: FragmentQuizDbBinding
    private lateinit var adapter:QuizzesAdapter
    private lateinit var list: MutableList<QuizWithQuestionsAndAnswers>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuizDbBinding.inflate(inflater)



        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        quizViewModel=ViewModelProvider(requireActivity()).get(QuizViewModel::class.java)

        binding.btnClear.setOnClickListener {
            quizViewModel.deleteAllQuizzes(requireContext())
        }
        quizViewModel.quizzes.observe(viewLifecycleOwner,{
            //Setup RecyclerView
            binding.recyclerQuiz.layoutManager= LinearLayoutManager(requireContext())
            adapter= QuizzesAdapter(it)
            binding.recyclerQuiz.adapter=adapter
        })

    }


}