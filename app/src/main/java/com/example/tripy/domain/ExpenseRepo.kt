package com.example.tripy.domain

import com.example.tripy.room.ExpenseDao
import com.example.tripy.room.ExpenseTable
import javax.inject.Inject

class ExpenseRepo @Inject constructor(private val dao: ExpenseDao) {

    suspend fun insertExpense(expenseTable: ExpenseTable) {
        return dao.insert(expenseTable)
    }

    suspend fun updateExpense(expenseTable: ExpenseTable) {
        return dao.updateExpense(expenseTable)
    }

    suspend fun getExpenseByDate(date: String, tripId: Int): List<ExpenseTable>? {
        return dao.getExpenseByDate(date, tripId)
    }

    suspend fun deleteExpenseById(id: Int, tripId: Int) {
        return dao.deleteExpense(id, tripId)
    }

    suspend fun  deleteExpenses(tripId: Int){
        return dao.deleteExpenseData(tripId)
    }
}