package com.my.projects.quizapp.presentation.history.list.adpter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.my.projects.quizapp.data.local.model.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.databinding.CardQuizBinding
import com.my.projects.quizapp.util.Const.Companion.cats
import com.my.projects.quizapp.util.converters.Converters
import timber.log.Timber


class HistoryAdapter(
    private val _quizzes: List<QuizWithQuestionsAndAnswers>,
    val listener: ItemClickListener
) : RecyclerView.Adapter<HistoryAdapter.QuizzesViewHolder>() {

    interface ItemClickListener {
        fun onItemClick(data: QuizWithQuestionsAndAnswers)
    }


    inner class QuizzesViewHolder(var binding: CardQuizBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: QuizWithQuestionsAndAnswers) {
            Timber.d(data.toString())
            binding.txtviewCardquizQuiztitle.text = data.quizEntity.title
            binding.txtviewCardquizQuizsavedate.text =
                Converters.noTimeDateToString(data.quizEntity.date.time)
            binding.txtviewCardquizQuizcategory.text =
                cats.find { it.id == data.quizEntity.category }?.name
            binding.root.setOnClickListener {
                listener.onItemClick(data)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizzesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = CardQuizBinding.inflate(inflater, parent, false)
        return QuizzesViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizzesViewHolder, position: Int) {
        holder.bind(_quizzes[position])
    }

    override fun getItemCount(): Int = _quizzes.size

}


