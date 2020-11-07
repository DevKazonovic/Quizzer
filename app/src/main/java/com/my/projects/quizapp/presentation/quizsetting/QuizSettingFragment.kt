package com.my.projects.quizapp.presentation.quizsetting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.model.QuizSetting
import com.my.projects.quizapp.databinding.FragmentQuizSettingBinding
import com.my.projects.quizapp.util.Const.Companion.KEY_CATEGORY
import com.my.projects.quizapp.util.Const.Companion.KEY_QUIZ_SETTING
import com.my.projects.quizapp.util.MaterialSpinnerAdapter
import com.my.projects.quizapp.util.QuizSettingUtil.Companion.DIFFICULTIES
import com.my.projects.quizapp.util.QuizSettingUtil.Companion.TYPES
import timber.log.Timber


class QuizSettingFragment : Fragment() {

    private lateinit var quizSettingBinding: FragmentQuizSettingBinding
    private var category: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        category = arguments?.getInt(KEY_CATEGORY)
        Timber.d("$category")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        quizSettingBinding = FragmentQuizSettingBinding.inflate(inflater)


        quizSettingBinding.btnStartQuiz.setOnClickListener {
            it.findNavController().navigate(R.id.action_quizSetting_to_quiz, getSettingBundle())
        }

        return quizSettingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInputFields()
    }


    private fun initInputFields() {
        val difficultiesAdapter = MaterialSpinnerAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            DIFFICULTIES.keys.toTypedArray()
        )

        val typesAdapter = MaterialSpinnerAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            TYPES.keys.toTypedArray()
        )


        quizSettingBinding.difficultyField.setAdapter(difficultiesAdapter)
        quizSettingBinding.typeField.setAdapter(typesAdapter)

    }

    private fun getSettingBundle(): Bundle {
        val amount = quizSettingBinding.amountField.text.toString()
        val quizSetting = QuizSetting(
            (if (amount.isNotEmpty()) amount.toInt() else 10),
            category,
            TYPES[quizSettingBinding.typeField.text.toString()],
            DIFFICULTIES[quizSettingBinding.difficultyField.text.toString()]
        )

        return bundleOf(
            KEY_QUIZ_SETTING to quizSetting
        )
    }
}