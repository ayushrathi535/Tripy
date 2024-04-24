package com.example.tripy.room

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trip_table")
data class TripTable(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val tripName: String,
    val tripLocation: String,
    val startDate: String,
    val endDate: String,
    val currency: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(tripName)
        parcel.writeString(tripLocation)
        parcel.writeString(startDate)
        parcel.writeString(endDate)
        parcel.writeString(currency)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TripTable> {
        override fun createFromParcel(parcel: Parcel): TripTable {
            return TripTable(parcel)
        }

        override fun newArray(size: Int): Array<TripTable?> {
            return arrayOfNulls(size)
        }
    }
}
