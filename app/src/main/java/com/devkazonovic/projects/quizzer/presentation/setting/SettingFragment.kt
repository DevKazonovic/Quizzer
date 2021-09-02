package com.devkazonovic.projects.quizzer.presentation.setting

import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.devkazonovic.projects.quizzer.R
import com.devkazonovic.projects.quizzer.domain.enums.ThemeType
import com.devkazonovic.projects.quizzer.domain.manager.AppSettingManager.Companion.KEY_COUNT_DOWN_TIMER_PERIOD
import com.devkazonovic.projects.quizzer.domain.manager.AppSettingManager.Companion.KEY_NUMBER_OF_QUESTIONS
import com.devkazonovic.projects.quizzer.util.extensions.setToolbar

const val KEY_SETTING_THEME = "KEY SETTING THEME"

class SettingFragment :
    PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val themePreference: ListPreference? = findPreference(KEY_SETTING_THEME)
        themePreference?.summaryProvider =
            Preference.SummaryProvider<ListPreference> { preference ->
                val value = preference.entry
                value
            }

        val numberOfQuestionsPreference: EditTextPreference? =
            findPreference(KEY_NUMBER_OF_QUESTIONS)
        numberOfQuestionsPreference?.let {
            it.setOnBindEditTextListener { editText ->
                editText.inputType = InputType.TYPE_CLASS_NUMBER
            }
            it.summaryProvider = Preference.SummaryProvider<EditTextPreference> { preference ->
                val value = preference.text
                "$value Questions (Max: 50)"
            }
            it.setOnPreferenceChangeListener { preference, newValue ->
                val input = (newValue as String).toInt()
                input <= 50
            }
        }

        val countDownTimerPreference: ListPreference? = findPreference(KEY_COUNT_DOWN_TIMER_PERIOD)
        countDownTimerPreference?.let {
            it.summaryProvider = Preference.SummaryProvider<ListPreference> { preference ->
                val value = preference.value
                "$value seconds per Question"
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        setToolbar(toolbar)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        sharedPreferences?.let {
            if (key == KEY_SETTING_THEME) {
                val theme =
                    it.getString(key, ThemeType.THEME_DEFAULT.name) ?: ThemeType.THEME_DEFAULT.name
                when (ThemeType.valueOf(theme)) {
                    ThemeType.THEME_DEFAULT -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    }
                    ThemeType.THEME_DARK -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }

                    ThemeType.THEME_LIGHT -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences
            .unregisterOnSharedPreferenceChangeListener(this)
    }


}