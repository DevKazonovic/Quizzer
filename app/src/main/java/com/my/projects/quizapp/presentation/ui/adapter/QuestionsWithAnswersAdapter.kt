package com.my.projects.quizapp.presentation.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.db.entity.Answer
import com.my.projects.quizapp.data.db.entity.relations.QuestionWithAnswers
import com.my.projects.quizapp.data.model.QuestionModel
import com.my.projects.quizapp.databinding.CardQuestionBinding
import com.my.projects.quizapp.presentation.ui.widgets.LogsRadioButtons.Companion.getCorrectRadio
import com.my.projects.quizapp.presentation.ui.widgets.LogsRadioButtons.Companion.getInCorrectRadio
import com.my.projects.quizapp.presentation.ui.widgets.LogsRadioButtons.Companion.getUserCorrectRadio
import com.my.projects.quizapp.presentation.ui.widgets.LogsRadioButtons.Companion.getUserInCorrectRadio

class QuestionsWithAnswersAdapter(private val questions: List<QuestionWithAnswers>) :
    RecyclerView.Adapter<QuestionsWithAnswersAdapter.QuestionsWithAnswersViewHolder>() {

    class QuestionsWithAnswersViewHolder(var binding: CardQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val context: Context = itemView.context

        fun bind(data: QuestionWithAnswers) {
            binding.txtQuestion.text = data.question.question
            displayAnswers(binding,context,data.answers)
        }

        private fun displayAnswers(binding: CardQuestionBinding, context:Context, data: List<Answer>){
            binding.radioGroupAnswer.clearCheck()
            binding.radioGroupAnswer.removeAllViews()
            val layoutParams: RelativeLayout.LayoutParams =
                RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    this.setMargins(0, 8, 0, 12)
                }
            var id = 0

            data.forEach {
                if (it.isUser) {
                    if (it.isCorrect) {
                        binding.radioGroupAnswer.addView(
                            getUserCorrectRadio(context, it.answer,id), layoutParams
                        )
                        binding.icCorrect.setImageResource(R.drawable.ic_round_check_circle)
                    } else {
                        binding.radioGroupAnswer.addView(
                            getUserInCorrectRadio(context, it.answer,id), layoutParams
                        )
                    }
                } else {
                    if (it.isCorrect) {
                        binding.radioGroupAnswer.addView(
                            getCorrectRadio(context, it.answer,id), layoutParams
                        )
                    } else {
                        binding.radioGroupAnswer.addView(
                            getInCorrectRadio(context, it.answer,id),
                            layoutParams
                        )
                    }
                }
                id++
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionsWithAnswersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = CardQuestionBinding.inflate(inflater, parent, false)
        return QuestionsWithAnswersViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionsWithAnswersViewHolder, position: Int) {
        holder.bind(questions[position])
    }

    override fun getItemCount(): Int = questions.size



}