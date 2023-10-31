package com.example.newsapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.databinding.CategoryItemBinding
import com.example.newsapp.util.Constants.BUSINESS
import com.example.newsapp.util.Constants.ENTERTAINMENT
import com.example.newsapp.util.Constants.SINCE
import com.example.newsapp.util.Constants.SPORTS

class CategoriesAdapter() : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {


    var categoryList = mutableListOf<String>()

    @SuppressLint("NotifyDataSetChanged")
    fun setCategoriesList(categoryList: List<String>) {
        this.categoryList = categoryList.toMutableList()
        notifyDataSetChanged()
    }

    inner class CategoriesViewHolder(
        val binding: CategoryItemBinding,
        val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: String) {
            when (category) {
                BUSINESS -> {
                    changeButtonTextAndColor(
                        binding.btnCategory,
                        context.getText(R.string.business),
                        ContextCompat.getColor(context, R.color.red)
                    )
                    return
                }

                ENTERTAINMENT -> {
                    changeButtonTextAndColor(
                        binding.btnCategory,
                        context.getText(R.string.entertainment),
                        ContextCompat.getColor(context, R.color.orange)
                    )
                    return
                }

                SINCE -> {
                    changeButtonTextAndColor(
                        binding.btnCategory,
                        context.getText(R.string.since),
                        ContextCompat.getColor(context, R.color.blue)
                    )
                    return
                }

                SPORTS -> {
                    changeButtonTextAndColor(
                        binding.btnCategory,
                        context.getText(R.string.sports),
                        ContextCompat.getColor(context, R.color.green)
                    )
                    return
                }
            }
        }
    }

    private fun changeButtonTextAndColor(
        btnCategory: AppCompatButton,
        name: CharSequence,
        color: Int
    ) {
        btnCategory.text = name
        btnCategory.backgroundTintList = ColorStateList.valueOf(color)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(
            CategoryItemBinding.inflate(
                LayoutInflater.from(parent.context)
            ),
            parent.context
        )
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val category = categoryList[position]
        holder.bind(category)

        holder.binding.btnCategory.setOnClickListener {
            onItemClick?.invoke(category)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    var onItemClick: ((String) -> Unit)? = null
}