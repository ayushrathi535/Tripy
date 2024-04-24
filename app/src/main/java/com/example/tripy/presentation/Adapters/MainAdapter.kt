package com.example.tripy.presentation.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tripy.databinding.DiaryCardBinding
import com.example.tripy.room.TripTable


class TripAdapter(
    private var tripList: List<TripTable>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(trip: TripTable)
    }

    fun updateData(newList: List<TripTable>) {
        tripList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val binding = DiaryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TripViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tripList.size
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = tripList[position]
        holder.bind(trip)
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(trip)
        }

    }

    inner class TripViewHolder(val binding: DiaryCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(trip: TripTable) {
            binding.apply {
                locationName.text = trip.tripLocation
                tripName.text = trip.tripName
                tripCurrency.text = trip.currency
                tripCost.text = "null&void"
                startDate.text = trip.startDate
                endDate.text = trip.endDate
            }
        }
    }
}


