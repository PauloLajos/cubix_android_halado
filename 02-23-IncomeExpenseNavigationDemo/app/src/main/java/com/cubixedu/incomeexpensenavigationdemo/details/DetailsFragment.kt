package com.cubixedu.incomeexpensenavigationdemo.details

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cubixedu.incomeexpensenavigationdemo.data.Budget
import com.cubixedu.incomeexpensenavigationdemo.data.BudgetItem
import com.cubixedu.incomeexpensenavigationdemo.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {

    private var _binding : FragmentDetailsBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var detailViewModel: DetailViewModel

    private lateinit var budget: Budget
    private lateinit var budgetItem: BudgetItem

    companion object {

        const val BUDGET_KEY = "budget_key"

        fun newInstance(budget: BudgetItem): DetailsFragment {
            val fragment = DetailsFragment()
            val args = Bundle().apply {
                putParcelable(BUDGET_KEY, budget)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU)
            arguments?.getParcelable(BUDGET_KEY, BudgetItem::class.java)?.let { budgetItem = it }
        else
            arguments?.getParcelable<BudgetItem>(BUDGET_KEY)?.let { budgetItem = it }

        detailViewModel.getBudget(budgetItem).observe(viewLifecycleOwner, Observer {
            this.budget = it
            displayBudget()
        })
    }

    private fun displayBudget() {
        binding.tvValue.text = budget.value.toString()
    }

    private fun deleteCurrentBudget() {
        detailViewModel.deleteBudget(budget)
    }
}