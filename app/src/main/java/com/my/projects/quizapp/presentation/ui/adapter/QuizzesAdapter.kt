package com.my.projects.quizapp.presentation.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.my.projects.quizapp.data.db.entity.Quiz
import com.my.projects.quizapp.data.db.entity.relations.QuestionWithAnswers
import com.my.projects.quizapp.data.db.entity.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.data.db.entity.relations.asQuiz
import com.my.projects.quizapp.databinding.CardQuizBinding
import com.my.projects.quizapp.util.converters.Converters.Companion.dateToString

class QuizzesAdapter(var _quizzes: List<QuizWithQuestionsAndAnswers>) :
    RecyclerView.Adapter<QuizzesAdapter.QuizzesViewHolder>() {

    val quizzes = _quizzes.map {
        it.asQuiz(false)
    }

    inner class QuizzesViewHolder(var binding: CardQuizBinding) :
        RecyclerView.ViewHolder(binding.root) {


        private val context: Context = itemView.context

        init {
            binding.cardView2.setOnClickListener {
                val quiz = quizzes[adapterPosition]
                quiz.isExpanded = !quiz.isExpanded
                notifyItemChanged(adapterPosition)
            }
        }

        fun bind(data: QuizParent) {


            binding.quizName.text = data.quiz.title
            binding.quizDate.text = dateToString(data.quiz.date.time)


            if (data.isExpanded) binding.recyclerQuizQuestions.visibility = VISIBLE
            else binding.recyclerQuizQuestions.visibility = GONE

            //Setup RecyclerView
            binding.recyclerQuizQuestions.layoutManager = LinearLayoutManager(context)
            val adapter = QuestionsWithAnswersAdapter(data.questions)
            binding.recyclerQuizQuestions.adapter = adapter
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizzesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = CardQuizBinding.inflate(inflater, parent, false)
        return QuizzesViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizzesViewHolder, position: Int) {
        holder.bind(quizzes[position])
    }

    override fun getItemCount(): Int = quizzes.size

}

class QuizParent(
    val quiz: Quiz,
    val questions: List<QuestionWithAnswers>,
    var isExpanded: Boolean = false
)



