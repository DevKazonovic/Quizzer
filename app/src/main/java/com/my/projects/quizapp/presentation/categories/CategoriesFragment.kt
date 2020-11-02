package com.my.projects.quizapp.presentation.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.my.projects.quizapp.R
import com.my.projects.quizapp.databinding.FragmentCategoriesBinding
import com.my.projects.quizapp.util.Const.Companion.KEY_CATEGORY


class CategoriesFragment : Fragment() {

    private lateinit var categoriesBinding: FragmentCategoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        categoriesBinding = FragmentCategoriesBinding.inflate(inflater)

        setUpButtonListeners()

        return categoriesBinding.root
    }

    private fun setUpButtonListeners() {
        categoriesBinding.btnCatGK.setOnClickListener {
            it.findNavController()
                .navigate(
                    R.id.action_categories_to_quizSetting,
                    bundleOf(KEY_CATEGORY to 10)
                )
        }
    }

}