package com.example.tripy.room

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense_table")
data class ExpenseTable(


    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val tripId:Int?,
    val description:String?,
    val amount:Double?,
    val date:String ,
    @Embedded
    val category:Category?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString().toString(),
        TODO("category")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeValue(tripId)
        parcel.writeString(description)
        parcel.writeValue(amount)
        parcel.writeString(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExpenseTable> {
        override fun createFromParcel(parcel: Parcel): ExpenseTable {
            return ExpenseTable(parcel)
        }

        override fun newArray(size: Int): Array<ExpenseTable?> {
            return arrayOfNulls(size)
        }
    }
}


