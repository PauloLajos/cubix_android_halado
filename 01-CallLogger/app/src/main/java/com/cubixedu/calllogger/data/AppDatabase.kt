package com.cubixedu.calllogger.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// from RoomGradeDemo
@Database(entities = [OutCallEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun outCallDAO(): OutCallDAO

    companion object {
        private var outCallInstance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (outCallInstance == null) {
                outCallInstance = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, "outcall_database.db")
                    .build()
            }
            return outCallInstance!!
        }
    }
}