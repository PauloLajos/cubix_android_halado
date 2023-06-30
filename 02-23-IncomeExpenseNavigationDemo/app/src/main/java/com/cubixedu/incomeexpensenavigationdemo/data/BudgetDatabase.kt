package com.cubixedu.incomeexpensenavigationdemo.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cubixedu.incomeexpensenavigationdemo.FragmentMain

@Database(entities = [BudgetData::class], version = 1)
abstract class BudgetDatabase : RoomDatabase() {

    abstract fun budgetDao(): BudgetDao

    companion object {
        private var INSTANCE: BudgetDatabase? = null

        fun getInstance(context: FragmentMain): BudgetDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.requireContext(),
                    BudgetDatabase::class.java, "budgetdata.db"
                )
                .build()
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}