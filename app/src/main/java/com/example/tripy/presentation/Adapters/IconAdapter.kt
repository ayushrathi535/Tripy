package com.example.tripy.presentation.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tripy.R
import com.example.tripy.databinding.IconCardBinding
import com.example.tripy.room.Category
import com.example.tripy.room.TripTable

class IconAdapter(
    private val iconList: List<Category>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<IconAdapter.IconViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(icon: Category)
    }
    private var selectedItemIndex: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val binding = IconCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IconViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return iconList.size
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        val icon = iconList[position]
        holder.bind(icon.icon!!, position == selectedItemIndex)
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(icon)
            updateSelectedItem(position)
        }
    }

    fun updateSelectedItem(newSelectedIndex: Int) {
        val previousSelectedItemIndex = selectedItemIndex
        selectedItemIndex = newSelectedIndex
        notifyItemChanged(previousSelectedItemIndex)
        notifyItemChanged(selectedItemIndex)
    }

    inner class IconViewHolder(val binding: IconCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(icon: Int, isSelected: Boolean) {
            binding.apply {
                iconImage.setImageResource(icon)
                if (isSelected) {
                    iconCard.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.blue))
                } else {
                    iconCard.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.white))
                }
            }
        }
    }
}
