package com.cubixedu.incomeexpensenavigationdemo.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "budget")
@Parcelize
data class Budget(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @SerializedName("value") val value: Float
) : Parcelable