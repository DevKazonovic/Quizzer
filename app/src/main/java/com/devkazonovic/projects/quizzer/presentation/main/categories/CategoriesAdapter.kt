package com.devkazonovic.projects.quizzer.presentation.main.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devkazonovic.projects.quizzer.databinding.CardCategoryBinding
import com.devkazonovic.projects.quizzer.domain.model.Category

class CategoriesAdapter(
    private val list: List<Category>,
    private val clickListener: CategoryClickListener
) : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    class CategoriesViewHolder(
        private val itemBinding: CardCategoryBinding,
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(cat: Category, clickListener: CategoryClickListener) {
            itemBinding.textViewCardCategoryName.text = cat.name
            itemBinding.imageViewCardCategoryIcon.setImageResource(cat.icon)
            itemBinding.root.setOnClickListener {
                clickListener.onClick(cat)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = CardCategoryBinding.inflate(inflater, parent, false)
        return CategoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.bind(list[position], clickListener)
    }

    override fun getItemCount(): Int = list.size
}

class CategoryClickListener(val callback: (category: Category) -> Unit) {
    fun onClick(category: Category) = callback(category)
}