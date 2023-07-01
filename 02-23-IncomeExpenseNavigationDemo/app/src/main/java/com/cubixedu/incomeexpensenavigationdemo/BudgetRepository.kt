package com.cubixedu.incomeexpensenavigationdemo

import androidx.lifecycle.LiveData
import com.cubixedu.incomeexpensenavigationdemo.data.Budget
import com.cubixedu.incomeexpensenavigationdemo.data.BudgetDao
import com.cubixedu.incomeexpensenavigationdemo.data.BudgetItem

class BudgetRepository(private val budgetDao: BudgetDao) {

    fun getAllBudget(): LiveData<List<BudgetItem>> {
        return budgetDao.getAllBudget()
    }

    fun getBudget(id: Int): LiveData<Budget> {
        return budgetDao.getBudget(id)
    }

    suspend fun updateBudget(budget: Budget) {
        budgetDao.updateBudget(budget)
    }

    suspend fun deleteBudget(budget: Budget) {
        budgetDao.deleteBudget(budget)
    }
}