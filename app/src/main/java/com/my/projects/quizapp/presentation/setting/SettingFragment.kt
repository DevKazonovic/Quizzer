package com.my.projects.quizapp.presentation.setting

import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.my.projects.quizapp.R
import com.my.projects.quizapp.domain.manager.SharedPreferenceManager.Companion.KEY_COUNT_DOWN_TIMER_PERIOD
import com.my.projects.quizapp.domain.manager.SharedPreferenceManager.Companion.KEY_NUMBER_OF_QUESTIONS
import com.my.projects.quizapp.util.UiUtil


class SettingFragment :
    PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val countingPreference: EditTextPreference? = findPreference(KEY_COUNT_DOWN_TIMER_PERIOD)
        countingPreference?.let{
            it.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER

            }
            it.summaryProvider = Preference.SummaryProvider<EditTextPreference> { preference ->
                val value = preference.text
                "$value seconds per Question"
            }
        }

        val numberOfQuestionPreference: EditTextPreference? = findPreference(KEY_NUMBER_OF_QUESTIONS)
        numberOfQuestionPreference?.let{
            it.setOnBindEditTextListener { editText ->
                editText.inputType = InputType.TYPE_CLASS_NUMBER
            }
            it.summaryProvider = Preference.SummaryProvider<EditTextPreference> { preference ->
                val value = preference.text
                "$value Questions"
            }
        }

    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        updateThemeMode()
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun updateThemeMode() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val isDarkMode = sharedPreferences.getBoolean("KEY_DARK_MODE", false)
        if (isDarkMode) {
            UiUtil.setNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            UiUtil.setNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

}