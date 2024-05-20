package com.example.tripy.presentation.Adapters


import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tripy.R
import com.example.tripy.databinding.CalendarCardBinding


class DaysAdapter(
    val days: List<Int>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<DaysAdapter.ViewHolder>() {

    private var selectedPosition: Int = 0


    interface OnItemClickListener {
        fun onItemClick(day: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CalendarCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return days.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = days[position]
        holder.bind(day,position == selectedPosition)


        holder.itemView.setOnClickListener {
            Log.e("click-->", "item click")
            selectedPosition = holder.adapterPosition
            notifyDataSetChanged()
            itemClickListener.onItemClick(day)
        }
    }

    inner class ViewHolder(private val binding: CalendarCardBinding) :
        RecyclerView.ViewHolder(binding.root) {



        @SuppressLint("ResourceAsColor")
        fun bind(day: Int, isSelected: Boolean) {
            binding.calendarDay.text = day.toString()


            // Set background color based on selection
            val backgroundColor = if (isSelected) {
                ContextCompat.getColor(binding.root.context, R.color.dark_blue)

            } else {
                ContextCompat.getColor(binding.root.context, R.color.white)
            }

            //text color change
            val textColor = if (isSelected) {
                ContextCompat.getColor(binding.root.context, R.color.white)
            } else {
                ContextCompat.getColor(binding.root.context, R.color.black)
            }
            binding.calendarLayout.setBackgroundColor(backgroundColor)
            binding.calendarDay.setTextColor(textColor)

        }

    }
}