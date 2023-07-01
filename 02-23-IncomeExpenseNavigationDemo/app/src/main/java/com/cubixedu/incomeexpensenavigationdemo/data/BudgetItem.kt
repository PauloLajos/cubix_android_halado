package com.cubixedu.incomeexpensenavigationdemo.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class BudgetItem(
    @ColumnInfo(name = "id") var id: Int,
    @ColumnInfo(name = "value") val value: Float
) : Parcelable
