package com.my.projects.quizapp.presentation.quiz.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.my.projects.quizapp.R
import com.my.projects.quizapp.databinding.FragmentCategoriesBinding
import com.my.projects.quizapp.domain.model.Category
import com.my.projects.quizapp.util.Const.Companion.KEY_CATEGORY
import com.my.projects.quizapp.util.Const.Companion.cats


class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(inflater)
        setCategories()
        return binding.root
    }

    private fun setCategories() {
        binding.recyclerviewCategories.layoutManager = GridLayoutManager(requireContext(), 2)
        categoriesAdapter = CategoriesAdapter(cats,
            object : CategoriesAdapter.OnItemClickListener {
                override fun onItemClick(cat: Category) {
                    onCategorySelected(cat)
                }
            })
        binding.recyclerviewCategories.adapter = categoriesAdapter
    }

    private fun onCategorySelected(cat: Category) {
        findNavController().navigate(
            R.id.action_categories_to_quizSetting,
            bundleOf(KEY_CATEGORY to cat)
        )
    }


}