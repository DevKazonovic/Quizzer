package com.my.projects.quizapp.presentation.history.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.my.projects.quizapp.data.CategoriesStore
import com.my.projects.quizapp.databinding.CardHeaderDateBinding
import com.my.projects.quizapp.databinding.CardHistoryQuizBinding
import com.my.projects.quizapp.domain.model.HistoryItem
import com.my.projects.quizapp.domain.model.HistoryQuiz
import com.my.projects.quizapp.util.converters.Converters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class HistoryAdapter(
    private val clickListener: QuizShowDetailListener,
    private val clickDeleteListener: QuizDeleteListener
) : ListAdapter<HistoryItem, RecyclerView.ViewHolder>(HistoryDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HistoryItem.Header -> ITEM_VIEW_TYPE_HEADER
            is HistoryItem.Item -> ITEM_VIEW_TYPE_ITEM
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_ITEM -> {
                val binding = CardHistoryQuizBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HistoryViewHolder(binding, clickListener, clickDeleteListener)
            }
            ITEM_VIEW_TYPE_HEADER -> {
                val binding = CardHeaderDateBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HeaderViewHolder(binding)
            }
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                val item = getItem(position) as HistoryItem.Header
                holder.bind(item.header)
            }
            is HistoryViewHolder -> {
                val history = getItem(position) as HistoryItem.Item
                holder.bind(history.item)

            }

        }
    }

    class HistoryViewHolder constructor(
        val binding: CardHistoryQuizBinding,
        private val clickListener: QuizShowDetailListener,
        private val clickDeleteListener: QuizDeleteListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HistoryQuiz) {
            binding.textViewTitle.text = item.title
            binding.textViewScore.text =
                "${Converters.scoreIntToPers(item.score, item.questions.size)}% Score"
            binding.imageViewCategoryIcon.setImageResource(CategoriesStore.getCategorie(item.category).icon)
            binding.root.setOnClickListener {
                clickListener.onClick(item)
            }
            binding.viewDeleteHistory.setOnClickListener {
                clickDeleteListener.onDelete(item, adapterPosition)
            }
        }

    }

    class HeaderViewHolder constructor(val binding: CardHeaderDateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(header: String) {
            binding.textViewHeader.text = header
        }
    }

    fun addHeaderAndSubmitList(list: List<HistoryQuiz>) {
        adapterScope.launch {
            val mapFromList = list.groupBy { it.date }
            val result = mutableListOf<HistoryItem>()
            mapFromList
                .toSortedMap { o1, o2 -> o1.compareTo(o2) }
                .forEach { data ->
                    val header = mutableListOf<HistoryItem.Header>()
                    header.add(HistoryItem.Header("${data.key}"))
                    result += header + data.value.map { HistoryItem.Item(it) }
                }
            withContext(Dispatchers.Main) {
                submitList(result)
            }
        }
    }
    fun deleteItemAt(position: Int) {
        val items = mutableListOf<HistoryItem>()
        items.addAll(currentList)
        if (items.getOrNull(position - 1) is HistoryItem.Header &&
            (position + 1 >= items.size || items.getOrNull(position + 1) is HistoryItem.Header)
        ) {
            items.removeAt(position)
            items.removeAt(position - 1)
        } else {
            items.removeAt(position)
        }
        submitList(items)

    }

}

class HistoryDiffCallback : DiffUtil.ItemCallback<HistoryItem>() {
    override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
        return oldItem == newItem
    }
}

class QuizShowDetailListener(val clickListener: (quiz: HistoryQuiz) -> Unit) {
    fun onClick(quiz: HistoryQuiz) = clickListener(quiz)
}

class QuizDeleteListener(val clickListener: (quiz: HistoryQuiz, position: Int) -> Unit) {
    fun onDelete(quiz: HistoryQuiz, position: Int) = clickListener(quiz, position)
}




