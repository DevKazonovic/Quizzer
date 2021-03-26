package com.my.projects.quizapp.presentation.quiz.setting

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.my.projects.quizapp.QuizApplication
import com.my.projects.quizapp.R
import com.my.projects.quizapp.databinding.FragmentQuizSettingBinding
import com.my.projects.quizapp.domain.enums.Difficulty
import com.my.projects.quizapp.domain.enums.Type
import com.my.projects.quizapp.domain.manager.SharedPreferenceManager
import com.my.projects.quizapp.domain.model.Category
import com.my.projects.quizapp.domain.model.QuizSetting
import com.my.projects.quizapp.presentation.ViewModelProviderFactory
import com.my.projects.quizapp.presentation.common.adapter.MaterialSpinnerAdapter
import com.my.projects.quizapp.presentation.quiz.QuizViewModel
import com.my.projects.quizapp.util.Const.Companion.KEY_CATEGORY
import com.my.projects.quizapp.util.QuizUtil.Companion.COUNTDOWNPERIODS
import com.my.projects.quizapp.util.QuizUtil.Companion.DIFFICULTIES
import com.my.projects.quizapp.util.QuizUtil.Companion.TYPES
import com.my.projects.quizapp.util.UiUtil
import com.my.projects.quizapp.util.extensions.setToolbar
import javax.inject.Inject


class QuizSettingFragment : Fragment() {

    private var category: Category? = null

    @Inject lateinit var viewModelFactory: ViewModelProviderFactory
    private val viewModel: QuizViewModel by navGraphViewModels(R.id.graph_quiz) {
        viewModelFactory
    }

    @Inject lateinit var sharedPreferenceManager: SharedPreferenceManager

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
        setToolbar(binding.toolbar)

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
        binding.textViewCategoryName.text = category?.name
        category?.icon?.let { binding.imageViewCategoryIcon.setImageResource(it) }
        initInputFields()
    }

    private fun initInputFields() {

        binding.editTextQuestionsNumber.doOnTextChanged { text, _, _, _ ->
            if(!text.isNullOrEmpty()){
                if(text.toString().toLong() > 50){
                    binding.btnStartQuiz.isEnabled = false
                    binding.textInputLayoutQuestionsNumber.error = "Maximum number of question is 50"
                }else{
                    binding.btnStartQuiz.isEnabled = true
                    binding.textInputLayoutQuestionsNumber.error = null
                }
            }else{
                binding.btnStartQuiz.isEnabled = false
                binding.textInputLayoutQuestionsNumber.error = "required."
            }

        }

        binding.editTextQuestionsNumber.text =
            UiUtil.getEditableInstance(sharedPreferenceManager.getNumberOfQuestions().toString())


        val countDownPeriodsAdapter = MaterialSpinnerAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            COUNTDOWNPERIODS.toTypedArray()
        )

        val difficultiesAdapter = MaterialSpinnerAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            DIFFICULTIES.keys.map { it.text }.toTypedArray()
        )

        val typesAdapter = MaterialSpinnerAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            TYPES.keys.map { it.text }.toTypedArray()
        )

        binding.editTextQuestionsCountDown.setAdapter(countDownPeriodsAdapter)
        binding.editTextQuestionsDifficulty.setAdapter(difficultiesAdapter)
        binding.editTextQuestionsType.setAdapter(typesAdapter)

        binding.editTextQuestionsCountDown.setText(sharedPreferenceManager.getCountDownTimer().toString())
        binding.editTextQuestionsDifficulty.setText(Difficulty.ANY.text)
        binding.editTextQuestionsType.setText(Type.ANY.text)

    }
    private fun getQuizSetting(): QuizSetting {
        val amount = binding.editTextQuestionsNumber.text.toString()
        return QuizSetting(
            (if (amount.isNotEmpty()) amount.toInt() else 10),
            category?.id,
            TYPES[Type.valueOf(binding.editTextQuestionsType.text.toString())],
            DIFFICULTIES[Difficulty.valueOf(binding.editTextQuestionsDifficulty.text.toString())]
        )
    }
}