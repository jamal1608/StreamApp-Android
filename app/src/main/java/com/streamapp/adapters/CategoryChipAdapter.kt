package com.streamapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.streamapp.R

class CategoryChipAdapter(
    private val categories: List<String>,
    private val onCategoryClick: (String) -> Unit
) : RecyclerView.Adapter<CategoryChipAdapter.CategoryViewHolder>() {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val chip = LayoutInflater.from(parent.context).inflate(R.layout.chip_item, parent, false) as Chip
        return CategoryViewHolder(chip)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val chip = holder.itemView as Chip
        chip.text = categories[position]
        chip.isChecked = position == selectedPosition
        chip.setOnClickListener {
            val oldPosition = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(oldPosition)
            notifyItemChanged(selectedPosition)
            onCategoryClick(categories[position])
        }
    }

    override fun getItemCount() = categories.size

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
