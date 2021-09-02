package com.devkazonovic.projects.quizzer.presentation.setting

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.preference.*
import com.devkazonovic.projects.quizzer.R
import com.devkazonovic.projects.quizzer.domain.manager.AppSettingManager.Companion.KEY_APP_THEME
import com.devkazonovic.projects.quizzer.domain.manager.AppSettingManager.Companion.KEY_COUNT_DOWN_TIMER_PERIOD
import com.devkazonovic.projects.quizzer.domain.manager.AppSettingManager.Companion.KEY_NUMBER_OF_QUESTIONS
import com.devkazonovic.projects.quizzer.util.ThemeUtil
import com.devkazonovic.projects.quizzer.util.extensions.setToolbar


class SettingFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val appThemeSwitchPreference: SwitchPreferenceCompat? = findPreference(KEY_APP_THEME)
        appThemeSwitchPreference?.setOnPreferenceChangeListener { preference, newValue ->
            ThemeUtil.updateAppTheme(newValue as Boolean)
            true
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

}