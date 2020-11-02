package com.my.projects.quizapp.presentation.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.my.projects.quizapp.databinding.FragmentScoreBinding
import com.my.projects.quizapp.presentation.quiz.QuizViewModel

class ScoreFragment : Fragment() {
    private lateinit var scoreBinding: FragmentScoreBinding
    private lateinit var quizViewModel: QuizViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        scoreBinding = FragmentScoreBinding.inflate(inflater)

        return scoreBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        quizViewModel = ViewModelProvider(requireActivity()).get(QuizViewModel::class.java)
        quizViewModel.score.observe(viewLifecycleOwner, { score ->
            scoreBinding.txtScore.text = "$score/${quizViewModel.getCurrentQuizzesList()?.size}"
        })
    }

}