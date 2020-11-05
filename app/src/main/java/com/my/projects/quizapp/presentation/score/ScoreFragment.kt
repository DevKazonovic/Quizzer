package com.my.projects.quizapp.presentation.score

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.my.projects.quizapp.R
import com.my.projects.quizapp.databinding.FragmentScoreBinding
import com.my.projects.quizapp.presentation.quiz.QuizViewModel
import timber.log.Timber

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
            scoreBinding.txtScore.text = "$score/${quizViewModel.getCurrentQuizzesListSize()}"
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHasOptionsMenu(true)

        // This callback will only be called when MyFragment is at least Started.
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Timber.d("Custom Back Action")
                    findNavController().navigate(R.id.action_score_to_categories)
                }
            })
        // The callback can be enabled or disabled here or in the lambda

    }


}