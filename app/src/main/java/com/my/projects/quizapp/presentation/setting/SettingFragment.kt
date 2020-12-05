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
import com.my.projects.quizapp.util.Util


class SettingFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val countingPreference: EditTextPreference? = findPreference("KEY_COUNT_DOWN_TIMER")

        countingPreference?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER
        }

        countingPreference?.summaryProvider =
            Preference.SummaryProvider<EditTextPreference> { preference ->
                val value = preference.text
                if (value.toInt() >= 60) {
                    "CountDown is 60 s"
                } else {
                    "CountDown is $value s"
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
            Util.setNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            Util.setNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}