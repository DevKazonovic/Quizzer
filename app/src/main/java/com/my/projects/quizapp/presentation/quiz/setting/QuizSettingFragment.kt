package com.my.projects.quizapp.presentation.quiz.setting

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.my.projects.quizapp.QuizApplication
import com.my.projects.quizapp.R
import com.my.projects.quizapp.databinding.FragmentQuizSettingBinding
import com.my.projects.quizapp.domain.model.Category
import com.my.projects.quizapp.domain.model.QuizSetting
import com.my.projects.quizapp.presentation.ViewModelProviderFactory
import com.my.projects.quizapp.presentation.common.adapter.MaterialSpinnerAdapter
import com.my.projects.quizapp.presentation.quiz.QuizViewModel
import com.my.projects.quizapp.util.Const.Companion.DIFFICULTIES
import com.my.projects.quizapp.util.Const.Companion.KEY_CATEGORY
import com.my.projects.quizapp.util.Const.Companion.TYPES
import javax.inject.Inject


class QuizSettingFragment : Fragment() {

    private var category: Category? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory

    private val viewModel: QuizViewModel by navGraphViewModels(R.id.graph_quiz) {
        viewModelFactory
    }

    private lateinit var binding: FragmentQuizSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        category = arguments?.getSerializable(KEY_CATEGORY) as Category?

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizSettingBinding.inflate(inflater)

        binding.btnStartQuiz.setOnClickListener {
            viewModel.getQuiz(getQuizSetting())
            it.findNavController().navigate(R.id.action_quizSetting_to_quiz)
        }

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as QuizApplication).component.inject(this)
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