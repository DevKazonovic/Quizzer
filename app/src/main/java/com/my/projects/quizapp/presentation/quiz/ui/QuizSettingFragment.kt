package com.my.projects.quizapp.presentation.quiz.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.model.CategoryModel
import com.my.projects.quizapp.data.model.QuizSetting
import com.my.projects.quizapp.databinding.FragmentQuizSettingBinding
import com.my.projects.quizapp.di.QuizInjector
import com.my.projects.quizapp.presentation.common.adapter.MaterialSpinnerAdapter
import com.my.projects.quizapp.presentation.quiz.controller.QuizViewModel
import com.my.projects.quizapp.presentation.quiz.util.Const.Companion.DIFFICULTIES
import com.my.projects.quizapp.presentation.quiz.util.Const.Companion.KEY_CATEGORY
import com.my.projects.quizapp.presentation.quiz.util.Const.Companion.TYPES


class QuizSettingFragment : Fragment() {

    private lateinit var binding: FragmentQuizSettingBinding
    private var category: CategoryModel? = null
    private lateinit var quizViewModel: QuizViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        category = arguments?.getSerializable(KEY_CATEGORY) as CategoryModel?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizSettingBinding.inflate(inflater)

        quizViewModel = ViewModelProvider(
            requireActivity(),
            QuizInjector(requireActivity().application).provideQuizViewModelFactory()
        ).get(QuizViewModel::class.java)


        binding.btnStartQuiz.setOnClickListener {
            quizViewModel.getQuiz(getQuizSetting())
            it.findNavController().navigate(R.id.action_quizSetting_to_quiz)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txtCatLabel.text = category?.name
        category?.icon?.let { binding.catIcon.setImageResource(it) }
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


        binding.difficultyField.setAdapter(difficultiesAdapter)
        binding.typeField.setAdapter(typesAdapter)

    }

    private fun getQuizSetting(): QuizSetting {
        val amount = binding.amountField.text.toString()

        return QuizSetting(
            (if (amount.isNotEmpty()) amount.toInt() else 10),
            category?.id,
            TYPES[binding.typeField.text.toString()],
            DIFFICULTIES[binding.difficultyField.text.toString()]
        )
    }
}