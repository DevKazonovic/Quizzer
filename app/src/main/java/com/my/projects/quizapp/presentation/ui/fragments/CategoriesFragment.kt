package com.my.projects.quizapp.presentation.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.model.Category
import com.my.projects.quizapp.databinding.FragmentCategoriesBinding
import com.my.projects.quizapp.presentation.controller.QuizViewModel
import com.my.projects.quizapp.presentation.ui.adapter.CategoriesAdapter
import com.my.projects.quizapp.util.Const.Companion.KEY_CATEGORY


class CategoriesFragment : Fragment() {

    private lateinit var quizViewModel: QuizViewModel
    private lateinit var binding: FragmentCategoriesBinding
    private val cats = listOf(
        Category(9, "General Knowledge"),
        Category(23, "History"),
        Category(24, "Politics"),
        Category(21, "Sport"),
        Category(26, "Celebrities"),
        Category(27, "Animals")
    )

    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(inflater)

        setCategories()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        quizViewModel = ViewModelProvider(requireActivity()).get(QuizViewModel::class.java)
        binding.btnSeeAll.setOnClickListener {
            quizViewModel.getStoredUserQuizzes(requireContext())
            findNavController().navigate(R.id.action_categories_to_quizzesDB)
        }

    }

    private fun setCategories() {
        binding.recyclerCats.layoutManager= LinearLayoutManager(requireContext())
        categoriesAdapter=CategoriesAdapter(cats,
            object :CategoriesAdapter.OnItemClickListener{
                override fun onItemClick(cat: Category) {
                    onCategorySelected(cat.id)
                }
            })
        binding.recyclerCats.adapter=categoriesAdapter
    }


    private fun onCategorySelected(catID: Int) {
        findNavController().navigate(
            R.id.action_categories_to_quizSetting,
            bundleOf(KEY_CATEGORY to catID)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        requireActivity().menuInflater.inflate(R.menu.app_setting, menu)
    }


}