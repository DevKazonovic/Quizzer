package com.my.projects.quizapp.presentation.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.model.QuestionModel
import com.my.projects.quizapp.databinding.CardQuestionBinding
import com.my.projects.quizapp.presentation.ui.widgets.LogsRadioButtons.Companion.getCorrectRadio
import com.my.projects.quizapp.presentation.ui.widgets.LogsRadioButtons.Companion.getInCorrectRadio
import com.my.projects.quizapp.presentation.ui.widgets.LogsRadioButtons.Companion.getUserCorrectRadio
import com.my.projects.quizapp.presentation.ui.widgets.LogsRadioButtons.Companion.getUserInCorrectRadio
import com.my.projects.quizapp.presentation.ui.widgets.LogsRadioButtons.Companion.layoutParams

class QuestionsAdapter(private val questions: MutableList<QuestionModel>) :
    RecyclerView.Adapter<QuestionsAdapter.QuestionsViewHolder>() {

    class QuestionsViewHolder(var binding: CardQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val question: TextView = itemView.findViewById(R.id.txtQuestion)
        private val context: Context = itemView.context

        fun bind(data: QuestionModel) {
            binding.txtQuestion.text = data.question
            displayAnswers(binding, context, data)
        }

        private fun displayAnswers(
            binding: CardQuestionBinding,
            context: Context,
            data: QuestionModel
        ) {
            binding.radioGroupAnswer.clearCheck()
            binding.radioGroupAnswer.removeAllViews()
            var id = 0

            data.answers.forEach {
                if (it.isUser) {
                    if (it.isCorrect) {
                        binding.radioGroupAnswer.addView(
                            getUserCorrectRadio(context, it.answer, id), layoutParams
                        )
                        binding.icCorrect.setImageResource(R.drawable.ic_round_check_circle)
                    } else {
                        binding.radioGroupAnswer.addView(
                            getUserInCorrectRadio(context, it.answer, id), layoutParams
                        )
                    }
                } else {
                    if (it.isCorrect) {
                        binding.radioGroupAnswer.addView(
                            getCorrectRadio(context, it.answer, id), layoutParams
                        )
                    } else {
                        binding.radioGroupAnswer.addView(
                            getInCorrectRadio(context, it.answer, id),
                            layoutParams
                        )
                    }
                }
                id++
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = CardQuestionBinding.inflate(inflater, parent, false)
        return QuestionsViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionsViewHolder, position: Int) {
        holder.bind(questions[position])
    }

    override fun getItemCount(): Int = questions.size


}