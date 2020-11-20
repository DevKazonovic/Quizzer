package com.my.projects.quizapp.presentation.ui.widgets

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.StateListDrawable
import android.view.Gravity
import android.widget.RadioButton
import android.widget.RelativeLayout
import com.my.projects.quizapp.R

class LogsRadioButtons {

    companion object {
        private const val answerSize = 16f
        private const val padding = 12
        val layoutParams: RelativeLayout.LayoutParams =
            RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                this.setMargins(0, 8, 0, 12)
            }

        fun getAnswerRadio(context: Context, text: String, id: Int): RadioButton {
            return RadioButton(context).apply {
                this.id = id
                this.text = text
                this.textSize = 18f
                this.setBackgroundResource(R.drawable.style_answer)
                this.gravity = Gravity.CENTER
                this.setPadding(padding, padding, padding, padding)
                this.buttonDrawable = StateListDrawable()
            }
        }

        fun getUserCorrectRadio(context: Context, text: String, id: Int): RadioButton {
            return RadioButton(context).apply {
                this.id = id
                this.text = "$text (Your Answer)"
                this.textSize = answerSize
                this.setTextColor(Color.GREEN)
                this.isChecked = true
                this.isClickable = false
            }
        }

        fun getUserInCorrectRadio(context: Context, text: String, id: Int): RadioButton {
            return RadioButton(context).apply {
                this.id = id
                this.text = "$text (Your Answer)"
                this.textSize = answerSize
                this.isChecked = false
                this.setTextColor(Color.RED)
                this.isClickable = false
            }
        }

        fun getCorrectRadio(context: Context, text: String, id: Int): RadioButton {
            return RadioButton(context).apply {
                this.id = id
                this.text = "$text (Correct Answer)"
                this.textSize = answerSize
                this.setTextColor(Color.GREEN)
                this.isChecked = true
                this.isClickable = false
            }
        }

        fun getInCorrectRadio(context: Context, text: String, id: Int): RadioButton {
            return RadioButton(context).apply {
                this.id = id
                this.text = text
                this.textSize = answerSize
                this.isChecked = false
                this.isClickable = false
            }
        }
    }
}