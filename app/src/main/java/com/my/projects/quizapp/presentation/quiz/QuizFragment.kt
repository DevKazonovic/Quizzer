package com.my.projects.quizapp.presentation.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.model.QuizModel
import com.my.projects.quizapp.data.model.QuizSetting
import com.my.projects.quizapp.databinding.FragmentQuizBinding
import com.my.projects.quizapp.util.Const.Companion.KEY_AMOUNT
import com.my.projects.quizapp.util.Const.Companion.KEY_CATEGORY
import com.my.projects.quizapp.util.Const.Companion.KEY_DIFFICULTY
import com.my.projects.quizapp.util.Const.Companion.KEY_TYPE
import timber.log.Timber


class QuizFragment : Fragment() {

    private lateinit var quizBinding: FragmentQuizBinding
    private lateinit var quizViewModel: QuizViewModel

    private var amount: Int = 10
    private var category: Int? = null
    private var difficulty: String? = null
    private var type: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {
            category = it.getInt(KEY_CATEGORY)
            amount = it.getInt(KEY_AMOUNT)
            difficulty = it.getString(KEY_DIFFICULTY)
            type = it.getString(KEY_TYPE)

            Timber.d("cat: $category, amount: $amount, difficulty: $difficulty, type:$type")

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        quizBinding = FragmentQuizBinding.inflate(inflater)


        quizBinding.btnNext.setOnClickListener {
            quizViewModel.onQuizAnswered(quizBinding.radioGroupAnswer.checkedRadioButtonId)
            quizViewModel.moveToNextQuiz()
        }

        return quizBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        quizViewModel = ViewModelProvider(requireActivity()).get(QuizViewModel::class.java)

        observeDataChange()

        quizViewModel.getQuizzes(QuizSetting(amount, category, type, difficulty))

    }


    private fun observeDataChange() {

        quizViewModel.currentQuiz.observe(viewLifecycleOwner, {
            displayQuiz(it)
        })

        quizViewModel.navigateToScore.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let {
                if (it) navigateToScorePage()
            }
        })
    }

    private fun displayQuiz(quiz: QuizModel) {
        var id = 0

        quizBinding.radioGroupAnswer.removeAllViews()

        quizBinding.txtQuestion.text = quiz.question()

        quiz.answers.forEach {
            val radioButton = RadioButton(requireContext()).apply {
                this.id = id
                this.text = it.answer
                this.textSize = 18f
            }
            val layoutParams: RelativeLayout.LayoutParams =
                RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
            layoutParams.setMargins(0, 8, 0, 12)
            quizBinding.radioGroupAnswer.addView(radioButton, layoutParams)
            id++

        }
    }

    private fun navigateToScorePage() {
        findNavController().navigate(R.id.action_quiz_to_score)
    }
}