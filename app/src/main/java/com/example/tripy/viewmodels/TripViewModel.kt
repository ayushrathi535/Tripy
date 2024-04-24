package com.example.tripy.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripy.room.TripTable
import com.example.tripy.domain.TripRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TripViewModel @Inject constructor(private val repo: TripRepo) : ViewModel() {

    private val _allTrips: MutableStateFlow<List<TripTable>> = MutableStateFlow(emptyList())
    val allTrips: StateFlow<List<TripTable>> = _allTrips

    init {
        fetchAllTrips()
    }


    fun fetchAllTrips() {
        viewModelScope.launch {
            _allTrips.value = repo.getAllTrips()
        }
    }

    fun insertTrip(tripTable: TripTable) {
        viewModelScope.launch {
            repo.insertTrip(tripTable)
            Log.d("viewmodel-->>", "insert success")
        }
    }

    fun deleteTrip(tripTable: TripTable) {
        viewModelScope.launch {
            repo.deleteTrip(tripTable)
        }
    }

    fun updateTrip(
        id: Int?,
        name: String,
        location: String,
        start: String,
        end: String,
        currency: String
    ) {
        viewModelScope.launch {

            repo.updateTrip(id, name, location, start, end, currency)

            _allTrips.value = repo.getAllTrips()

            Log.e("value is-->>", _allTrips.value.size.toString())

            for (i in _allTrips.value) {
                print("value is-->>" + i.tripName)
            }

            _allTrips.emit(allTrips.value)
        }
    }
}
