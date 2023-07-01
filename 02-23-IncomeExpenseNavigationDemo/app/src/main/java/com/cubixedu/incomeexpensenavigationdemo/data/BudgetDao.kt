package com.cubixedu.incomeexpensenavigationdemo.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface BudgetDao {
    @Delete
    suspend fun deleteBudget(budget: Budget)

    @Update
    suspend fun updateBudget(budget: Budget)

    @Query("SELECT * FROM budget WHERE id = :id")
    fun getBudget(id: Int): LiveData<Budget>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllBudget(budget: List<Budget>)

    @Query("SELECT id, value FROM budget")
    fun getAllBudget(): LiveData<List<BudgetItem>>

    @Query("SELECT id, value FROM budget WHERE value >= 0")
    fun getIncome(): LiveData<Float>

    @Query("SELECT SUM(value) as valueSum FROM budget WHERE value < 0")
    fun getExpense(): LiveData<Float>
}