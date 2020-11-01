package com.my.projects.quizapp.presentation.quiz

import android.os.Bundle
import android.util.AttributeSet
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.radiobutton.MaterialRadioButton
import com.my.projects.quizapp.data.model.QuizModel
import com.my.projects.quizapp.data.model.QuizSetting
import com.my.projects.quizapp.data.remote.QuizApi
import com.my.projects.quizapp.databinding.FragmentQuizBinding
import com.my.projects.quizapp.util.Const.Companion.KEY_AMOUNT
import com.my.projects.quizapp.util.Const.Companion.KEY_CATEGORY
import com.my.projects.quizapp.util.Const.Companion.KEY_DIFFICULTY
import com.my.projects.quizapp.util.Const.Companion.KEY_TYPE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.awt.font.TextAttribute


class QuizFragment : Fragment() {

    private lateinit var quizBinding: FragmentQuizBinding
    private lateinit var quizViewModel: QuizViewModel

    private var amount: Int = 10
    private var category: Int? = null
    private var difficulty: String? = null
    private var type: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizViewModel = ViewModelProvider(this).get(QuizViewModel::class.java)
        arguments?.let {
            category = it.getInt(KEY_CATEGORY)
            amount = it.getInt(KEY_AMOUNT)
            difficulty = it.getString(KEY_DIFFICULTY)
            type = it.getString(KEY_TYPE)
            Timber.d("cat: $category, amount: $amount, difficulty: $difficulty, type:$type")
            quizViewModel.getQuizzes(QuizSetting(amount, category, type, difficulty))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        quizBinding = FragmentQuizBinding.inflate(inflater)

        observeDataChange()

        quizBinding.btnNext.setOnClickListener {
            quizViewModel.moveToNextQuiz()
        }

        return quizBinding.root
    }


    private fun observeDataChange() {
        quizViewModel.quizList.observe(viewLifecycleOwner, {
            Timber.d("$it")
        })
        quizViewModel.currentQuiz.observe(viewLifecycleOwner, {
            displayQuiz(it)
        })
    }
    private fun displayQuiz(quiz: QuizModel) {

        //Call this method to remove all child views from the ViewGroup.
        quizBinding.radioGroupAnswer.removeAllViews()

        quizBinding.txtQuestion.text = quiz.question
        quiz.answers.forEach {
            val radioButton= RadioButton(requireContext()).apply {
                this.text=it.answer
            }
            quizBinding.radioGroupAnswer.addView(radioButton)
        }
    }
}