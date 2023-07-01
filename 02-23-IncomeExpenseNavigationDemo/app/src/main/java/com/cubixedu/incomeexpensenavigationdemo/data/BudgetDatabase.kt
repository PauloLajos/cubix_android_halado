package com.cubixedu.incomeexpensenavigationdemo.data

import android.content.Context
import android.content.res.Resources
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cubixedu.incomeexpensenavigationdemo.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(version = 1, entities = [Budget::class], exportSchema = false)
abstract class BudgetDatabase : RoomDatabase() {

    abstract fun budgetDao(): BudgetDao

    private class BudgetDatabaseCallback(
        private val scope: CoroutineScope,
        private val resources: Resources
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val budgetDao = database.budgetDao()
                    prePopulateDatabase(budgetDao)
                }
            }
        }

        private suspend fun prePopulateDatabase(budgetDao: BudgetDao) {
            val jsonString = resources.openRawResource(R.raw.budget).bufferedReader().use {
                it.readText()
            }
            val typeToken = object : TypeToken<List<Budget>>() {}.type
            val allBudget = Gson().fromJson<List<Budget>>(jsonString, typeToken)
            budgetDao.insertAllBudget(allBudget)
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: BudgetDatabase? = null

        fun getDatabase(context: Context, coroutineScope: CoroutineScope, resources: Resources): BudgetDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    BudgetDatabase::class.java,
                    "budget_database")
                    .addCallback(BudgetDatabaseCallback(coroutineScope, resources))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }


}