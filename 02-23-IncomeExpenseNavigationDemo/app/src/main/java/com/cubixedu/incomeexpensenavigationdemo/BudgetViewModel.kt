package com.cubixedu.incomeexpensenavigationdemo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.cubixedu.incomeexpensenavigationdemo.data.BudgetDatabase
import com.cubixedu.incomeexpensenavigationdemo.data.BudgetItem

class BudgetViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BudgetRepository

    init {
        val budgetDao = BudgetDatabase
            .getDatabase(application, viewModelScope, application.resources)
            .budgetDao()
        repository = BudgetRepository(budgetDao)
    }

    fun getAllBudget(): LiveData<List<BudgetItem>> {
        return repository.getAllBudget()
    }
}