package com.my.projects.quizapp.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.my.projects.quizapp.data.local.entity.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.databinding.FragmentQuizDetailBinding
import com.my.projects.quizapp.presentation.ui.adapter.QuestionsWithAnswersAdapter
import com.my.projects.quizapp.util.Const.Companion.KEY_QUIZ

class QuizDetailFragment : Fragment() {

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
    ): View? {
        binding = FragmentQuizDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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


}