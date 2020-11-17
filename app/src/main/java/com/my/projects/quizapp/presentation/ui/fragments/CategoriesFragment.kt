package com.my.projects.quizapp.presentation.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.my.projects.quizapp.R
import com.my.projects.quizapp.databinding.FragmentCategoriesBinding
import com.my.projects.quizapp.presentation.controller.QuizViewModel
import com.my.projects.quizapp.util.Const.Companion.KEY_CATEGORY


class CategoriesFragment : Fragment() {

    private lateinit var quizViewModel: QuizViewModel
    private lateinit var categoriesBinding: FragmentCategoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        categoriesBinding = FragmentCategoriesBinding.inflate(inflater)


        setUpButtonListeners()

        return categoriesBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        quizViewModel = ViewModelProvider(requireActivity()).get(QuizViewModel::class.java)
        categoriesBinding.btnSeeAll.setOnClickListener {
            quizViewModel.getStoredUserQuizzes(requireContext())
            findNavController().navigate(R.id.action_categories_to_quizzesDB)
        }

    }

    private fun setUpButtonListeners() {
        categoriesBinding.btnGK.setOnClickListener {
          navigateToCategory(it,9)
        }

        categoriesBinding.btnSport.setOnClickListener {
            navigateToCategory(it,21)
        }
        categoriesBinding.btnCeleb.setOnClickListener {
            navigateToCategory(it,26)
        }
    }


    private fun navigateToCategory(view:View,catID:Int) {
        view.findNavController().navigate(
            R.id.action_categories_to_quizSetting,
            bundleOf(KEY_CATEGORY to catID)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        requireActivity().menuInflater.inflate(R.menu.app_setting, menu)


    }



}