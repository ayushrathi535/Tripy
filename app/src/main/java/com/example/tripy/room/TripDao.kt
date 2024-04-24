package com.example.tripy.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface TripDao {

    @Query("SELECT * FROM trip_table")
    suspend fun getAllTrips(): List<TripTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripTable)

    @Delete
    suspend fun deleteTrip(trip: TripTable)

    @Query("UPDATE trip_table SET tripName = :name, tripLocation = :location, startDate = :start, endDate = :end, currency = :currency WHERE id = :id")
    suspend fun updateTrip(id: Int?, name: String, location: String, start: String, end: String, currency: String)

}