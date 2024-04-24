package com.example.tripy.domain

import com.example.tripy.room.TripDao
import com.example.tripy.room.TripTable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class TripRepo @Inject constructor(private val dao: TripDao){

    suspend fun insertTrip(tripTable: TripTable){
        dao.insertTrip(tripTable)
    }

    suspend fun deleteTrip(tripTable: TripTable){
        dao.deleteTrip(tripTable)
    }

    suspend fun updateTrip(id: Int?, name: String, location: String, start: String, end: String, currency: String) {
        dao.updateTrip(id, name, location, start, end, currency)
    }


    suspend fun getAllTrips(): List<TripTable> {
        return dao.getAllTrips()
    }

}