package com.devkazonovic.projects.quizzer.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import com.devkazonovic.projects.quizzer.R
import com.devkazonovic.projects.quizzer.databinding.FragmentMainBinding
import com.devkazonovic.projects.quizzer.presentation.main.categories.CategoriesFragment

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            showCategorriesFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpListeners()
    }


    private fun showCategorriesFragment() {
        childFragmentManager.commit {
            setReorderingAllowed(true)
            add<CategoriesFragment>(R.id.fragmentContainer_categories)
        }
    }

    private fun setUpListeners() {
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_history -> {
                    findNavController().navigate(R.id.action_mainPage_to_graph_history)
                    true
                }
                R.id.action_setting -> {
                    findNavController().navigate(R.id.action_mainPage_to_graph_setting)
                    true
                }
                else -> false
            }
        }
    }

}