package com.example.grocerysystem.categoryMgmt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.grocerysystem.R
import com.example.grocerysystem.databinding.ItemCategoryBinding
import com.example.grocerysystem.model.CategoryItem


class AdapterShowCategory(
    val data: ArrayList<CategoryItem>,private val onDeleteClick: (CategoryItem) -> Unit
) : RecyclerView.Adapter<AdapterShowCategory.ExViewHolder>() {

    class ExViewHolder(var binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExViewHolder {
        val view = DataBindingUtil.inflate<ItemCategoryBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_category, parent, false
        )
        return ExViewHolder(view)

    }

    override fun onBindViewHolder(holder: ExViewHolder, position: Int) {
        holder.binding.categoryTxt.text = data[position].title
        holder.binding.delete.setOnClickListener {
            onDeleteClick(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}