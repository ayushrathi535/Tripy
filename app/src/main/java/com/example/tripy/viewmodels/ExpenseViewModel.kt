package com.example.tripy.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripy.room.ExpenseTable
import com.example.tripy.domain.ExpenseRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(private val repo: ExpenseRepo) : ViewModel() {


    private val _currentExpense: MutableStateFlow<List<ExpenseTable>> =
        MutableStateFlow(emptyList())

    val currentExpense: StateFlow<List<ExpenseTable>> = _currentExpense

    fun insertExpense(expenseTable: ExpenseTable) {

        viewModelScope.launch {
            repo.insertExpense(expenseTable)
            Log.d("viewmodel-->>", "expense insert success")

        }

    }

    fun updateExpense(expenseTable: ExpenseTable) {
        viewModelScope.launch {
            repo.updateExpense(expenseTable)
        }
    }

    suspend fun getExpenseByDate(date: String, tripId: Int) {

        viewModelScope.launch {
            Log.e("expense viewmodel--->", "success")
            // Call the repository function to get expenses by date
            val expenses = repo.getExpenseByDate(date, tripId)
            _currentExpense.value = expenses ?: emptyList()
            Log.e("expense viewmodel:--->", _currentExpense.value.size.toString())
        }

    }

    suspend fun deleteExpense(expenseTable: ExpenseTable) {
        viewModelScope.launch {
            repo.deleteExpenseById(expenseTable.id, expenseTable?.tripId!!)
        }
    }
    suspend fun deleteExpenses(tripId: Int) {
        viewModelScope.launch {
            repo.deleteExpenses(tripId)
        }
    }

}