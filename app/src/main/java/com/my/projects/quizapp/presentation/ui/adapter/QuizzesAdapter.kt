package com.my.projects.quizapp.presentation.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.my.projects.quizapp.R
import com.my.projects.quizapp.data.db.entity.relations.QuestionWithAnswers
import com.my.projects.quizapp.data.db.entity.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.data.model.QuestionModel
import com.my.projects.quizapp.databinding.CardQuestionBinding
import com.my.projects.quizapp.databinding.CardQuizBinding
import com.my.projects.quizapp.presentation.ui.widgets.LogsRadioButtons
import com.my.projects.quizapp.util.converters.Converters.Companion.dateToString

class QuizzesAdapter(private val questions: List<QuizWithQuestionsAndAnswers>) : RecyclerView.Adapter<QuizzesAdapter.QuizzesViewHolder>(){

    class QuizzesViewHolder(var binding: CardQuizBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val context: Context = itemView.context

        fun bind(data:QuizWithQuestionsAndAnswers) {
            binding.quizName.text= data.quiz.title
            binding.quizDate.text=dateToString(data.quiz.date.time)

            //Setup RecyclerView
            binding.recyclerQuizQuestions.layoutManager= LinearLayoutManager(context)
            val adapter = QuestionsWithAnswersAdapter(data.questions)
            binding.recyclerQuizQuestions.adapter=adapter
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizzesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = CardQuizBinding.inflate(inflater, parent, false)
        return QuizzesViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizzesViewHolder, position: Int) {
        holder.bind(questions[position])
    }

    override fun getItemCount(): Int = questions.size

}