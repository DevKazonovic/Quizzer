package com.my.projects.quizapp.presentation.score

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.my.projects.quizapp.R
import com.my.projects.quizapp.databinding.FragmentScoreBinding
import com.my.projects.quizapp.presentation.quiz.QuizViewModel


private const val ARG_PARAM2 = "param2"


class ScoreFragment : Fragment() {
    private lateinit var scoreBinding: FragmentScoreBinding
    private lateinit var quizViewModel: QuizViewModel


    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        scoreBinding= FragmentScoreBinding.inflate(inflater)



        return scoreBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        quizViewModel = ViewModelProvider(requireActivity()).get(QuizViewModel::class.java)
        quizViewModel.score.observe(viewLifecycleOwner, {score ->
            scoreBinding.txtScore.text = "$score/${quizViewModel.getCurrentQuizzesList()?.size}"
        })
    }


}