package com.example.tripy.room

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Update
import androidx.room.Upsert


@Dao
interface ExpenseDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: ExpenseTable)


//    @Upsert
//    suspend fun insert(expense: ExpenseTable)

    @Query("SELECT * FROM expense_table WHERE date = :date AND tripId = :tripId")
    suspend fun getExpenseByDate(date: String, tripId: Int): List<ExpenseTable>?

    @Update
    suspend fun updateExpense(expense: ExpenseTable)

    @Query("DELETE FROM expense_table WHERE id = :id AND tripId = :tripId")
    suspend fun deleteExpense(id: Int, tripId: Int)

    @Query("DELETE FROM expense_table where tripId =:tripId")
    suspend fun deleteExpenseData(tripId: Int)

}
