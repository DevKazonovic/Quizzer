package com.my.projects.quizapp.presentation.ui.widgets

import android.content.Context
import android.graphics.Color
import android.widget.RadioButton

class LogsRadioButtons {

    companion object{
        private const val answerSize=16f

        fun getUserCorrectRadio(context:Context, text:String,id:Int):RadioButton{
            return RadioButton(context).apply {
                this.id = id
                this.text = "$text (Your Answer)"
                this.textSize = answerSize
                this.setTextColor(Color.GREEN)
                this.isChecked=true
                this.isClickable=false
            }
        }


        fun getUserInCorrectRadio(context:Context, text:String,id:Int):RadioButton{
            return RadioButton(context).apply {
                this.id = id
                this.text = "$text (Your Answer)"
                this.textSize = answerSize
                this.isChecked = false
                this.setTextColor(Color.RED)
                this.isClickable = false
            }
        }


        fun getCorrectRadio(context:Context, text:String,id:Int):RadioButton{
            return RadioButton(context).apply {
                this.id = id
                this.text = "$text (Correct Answer)"
                this.textSize = answerSize
                this.setTextColor(Color.GREEN)
                this.isChecked = true
                this.isClickable = false
            }
        }


        fun getInCorrectRadio(context:Context, text:String,id:Int):RadioButton{
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