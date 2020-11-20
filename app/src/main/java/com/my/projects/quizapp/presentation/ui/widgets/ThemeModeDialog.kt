package com.my.projects.quizapp.presentation.ui.widgets

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.my.projects.quizapp.databinding.SwitchItemBinding
import com.my.projects.quizapp.util.Util

class ThemeModeDialog : BottomSheetDialogFragment() {
    private lateinit var binding: SwitchItemBinding

    @SuppressLint("SwitchIntDef")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SwitchItemBinding.inflate(inflater)
        when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_YES -> binding.modeSwitchControl.isChecked = true
            AppCompatDelegate.MODE_NIGHT_NO -> binding.modeSwitchControl.isChecked = false
        }

        binding.modeSwitchControl.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Util.setNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                Util.setNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        return binding.root
    }
}