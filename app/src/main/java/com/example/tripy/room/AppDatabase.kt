package com.example.tripy.room

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [TripTable::class,ExpenseTable::class],
    version = 1,exportSchema = false)
abstract class AppDatabase:RoomDatabase() {

    abstract fun tripDao(): TripDao
    abstract fun expenseDao(): ExpenseDao

}