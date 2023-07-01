package com.cubixedu.incomeexpensenavigationdemo.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.cubixedu.incomeexpensenavigationdemo.BudgetRepository
import com.cubixedu.incomeexpensenavigationdemo.data.Budget
import com.cubixedu.incomeexpensenavigationdemo.data.BudgetDatabase
import com.cubixedu.incomeexpensenavigationdemo.data.BudgetItem
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BudgetRepository

    init {
        val budgetDao = BudgetDatabase
            .getDatabase(application, viewModelScope, application.resources)
            .budgetDao()
        repository = BudgetRepository(budgetDao)
    }

    fun getBudget(budget: BudgetItem): LiveData<Budget> {
        return repository.getBudget(budget.id)
    }

    fun updateBudget(budget: Budget) = viewModelScope.launch {
        repository.updateBudget(budget)
    }

    fun deleteBudget(budget: Budget) = viewModelScope.launch {
        repository.deleteBudget(budget)
    }
}