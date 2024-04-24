package com.example.tripy.presentation.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tripy.databinding.ExpenseCardBinding
import com.example.tripy.room.ExpenseTable

class ExpenseAdapter(
//    var expenses: List<ExpenseTable>,
    private var editButtonClickListener: OnEditButtonClickListener? = null,
    private var deleteButtonClickListener: OnDeleteButtonClickListener? = null
) :
    RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    interface OnEditButtonClickListener {
        fun onEditButtonClick(expense: ExpenseTable)
    }


    interface OnDeleteButtonClickListener {
        fun onDeleteButtonClick(expense: ExpenseTable, itemView: View)
    }

//    fun updateData(newList: List<ExpenseTable>) {
//        expenses = newList
//        notifyDataSetChanged()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ExpenseCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ExpenseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {

        val expense = differ.currentList[position]

        holder.bind(expense, holder.itemView)

    }


    inner class ExpenseViewHolder(private val binding: ExpenseCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(expense: ExpenseTable, itemView: View) {
            binding.apply {
                expenseTitle.text = expense.category?.name
                expenseDescription.text = expense.description
                amount.text = expense.amount.toString()
                expenseType.setImageResource(expense.category?.icon ?: 0)


                editBtn.setOnClickListener {
                    editButtonClickListener?.onEditButtonClick(expense)
                }

                deleteBtn.setOnClickListener {
                    deleteButtonClickListener?.onDeleteButtonClick(expense, itemView)
                }
            }

            this.itemView.setOnClickListener {
                binding.galleryContent.visibility =
                    if (binding.galleryContent.visibility == View.VISIBLE) {
                        View.GONE
                    } else {
                        View.VISIBLE
                    }


                binding.crudContent.visibility =
                    if (binding.crudContent.visibility == View.VISIBLE) {
                        View.GONE
                    } else {
                        View.VISIBLE
                    }
            }


        }

    }


   private val differCallback = object : DiffUtil.ItemCallback<ExpenseTable>(){
        override fun areItemsTheSame(oldItem: ExpenseTable, newItem: ExpenseTable): Boolean {

            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: ExpenseTable, newItem: ExpenseTable): Boolean {
            return  oldItem==newItem
        }

    }

    val differ = AsyncListDiffer(this,differCallback)
}

