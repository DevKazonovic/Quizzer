package com.my.projects.quizapp.presentation.history.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.my.projects.quizapp.R
import com.my.projects.quizapp.databinding.CardQuestionBinding
import com.my.projects.quizapp.domain.model.QuizAnswersSummary
import com.my.projects.quizapp.domain.model.QuizSummary
import com.my.projects.quizapp.presentation.common.widgets.LogsRadioButtons.Companion.getCorrectRadio
import com.my.projects.quizapp.presentation.common.widgets.LogsRadioButtons.Companion.getInCorrectRadio
import com.my.projects.quizapp.presentation.common.widgets.LogsRadioButtons.Companion.getUserCorrectRadio
import com.my.projects.quizapp.presentation.common.widgets.LogsRadioButtons.Companion.getUserInCorrectRadio

class HistoryDetailAdapter(
    private val questions: List<QuizSummary>
) : RecyclerView.Adapter<HistoryDetailAdapter.QuestionAnswersViewHolder>() {

    inner class QuestionAnswersViewHolder(
        var binding: CardQuestionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val context: Context = itemView.context

        fun bind(data: QuizSummary) {
            binding.txtviewCardquestionQuestion.text = data.question
            displayAnswers(binding, context, data.answers)
        }

        private fun displayAnswers(
            binding: CardQuestionBinding,
            context: Context,
            data: List<QuizAnswersSummary>
        ) {
            binding.radiogroupCardquestionAnswerchoices.clearCheck()
            binding.radiogroupCardquestionAnswerchoices.removeAllViews()
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
                        binding.radiogroupCardquestionAnswerchoices.addView(
                            getUserCorrectRadio(context, it.answer, id), layoutParams
                        )
                        binding.imageviewCardquestionIconanswerstate.setImageResource(R.drawable.ic_round_correct)
                    } else {
                        binding.radiogroupCardquestionAnswerchoices.addView(
                            getUserInCorrectRadio(context, it.answer, id), layoutParams
                        )
                        binding.imageviewCardquestionIconanswerstate.setImageResource(R.drawable.ic_round_wrong)
                    }
                } else {
                    if (it.isCorrect) {
                        binding.radiogroupCardquestionAnswerchoices.addView(
                            getCorrectRadio(context, it.answer, id), layoutParams
                        )

                    } else {
                        binding.radiogroupCardquestionAnswerchoices.addView(
                            getInCorrectRadio(context, it.answer, id),
                            layoutParams
                        )
                    }
                }
                id++
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionAnswersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = CardQuestionBinding.inflate(inflater, parent, false)
        return QuestionAnswersViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionAnswersViewHolder, position: Int) {
        holder.bind(questions[position])
    }

    override fun getItemCount(): Int = questions.size


}