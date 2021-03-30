package com.my.projects.quizapp.presentation.main.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.CategoriesStore
import com.my.projects.quizapp.databinding.FragmentCategoriesBinding
import com.my.projects.quizapp.util.BundleUtil.KEY_QUIZ_CATEGORY_SELECTED


class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewCategories.layoutManager = GridLayoutManager(requireContext(), 2)
        categoriesAdapter = CategoriesAdapter(CategoriesStore.cats, CategoryClickListener {
            findNavController().navigate(
                R.id.action_mainPage_to_graph_quiz,
                bundleOf(KEY_QUIZ_CATEGORY_SELECTED to it.id)
            )
        })
        binding.recyclerViewCategories.adapter = categoriesAdapter
    }

}