package com.my.projects.quizapp.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.model.CategoryModel
import com.my.projects.quizapp.databinding.FragmentCategoriesBinding
import com.my.projects.quizapp.presentation.controller.QuizViewModel
import com.my.projects.quizapp.presentation.ui.adapter.CategoriesAdapter
import com.my.projects.quizapp.util.Const.Companion.KEY_CATEGORY
import com.my.projects.quizapp.util.Const.Companion.cats


class CategoriesFragment : Fragment() {

    private lateinit var quizViewModel: QuizViewModel
    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(inflater)
        setCategories()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        quizViewModel = ViewModelProvider(requireActivity()).get(QuizViewModel::class.java)

    }

    private fun setCategories() {
        binding.recyclerCats.layoutManager = LinearLayoutManager(requireContext())
        categoriesAdapter = CategoriesAdapter(cats,
            object : CategoriesAdapter.OnItemClickListener {
                override fun onItemClick(cat: CategoryModel) {
                    onCategorySelected(cat)
                }
            })
        binding.recyclerCats.adapter = categoriesAdapter
    }

    private fun onCategorySelected(cat: CategoryModel) {
        findNavController().navigate(
            R.id.action_categories_to_quizSetting,
            bundleOf(KEY_CATEGORY to cat)
        )
    }


}