package com.cubixedu.incomeexpensenavigationdemo.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cubixedu.incomeexpensenavigationdemo.BudgetAdapter
import com.cubixedu.incomeexpensenavigationdemo.BudgetViewModel
import com.cubixedu.incomeexpensenavigationdemo.DataManager
import com.cubixedu.incomeexpensenavigationdemo.data.BudgetItem
import com.cubixedu.incomeexpensenavigationdemo.databinding.FragmentPagesBinding
import com.cubixedu.incomeexpensenavigationdemo.details.DetailsFragment

class FragmentPageIncome : Fragment() {

    private var _binding: FragmentPagesBinding? = null
    private val binding get() = _binding!!

    private lateinit var budgetViewModel: BudgetViewModel
    private lateinit var adapter: BudgetAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FragmentPageIncome()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BudgetAdapter(mutableListOf())

        budgetViewModel = ViewModelProvider(this)[BudgetViewModel::class.java]
        budgetViewModel.getAllBudget().observe(viewLifecycleOwner, Observer<List<BudgetItem>> { budget ->
            adapter.swapData(budget)
        })

        binding.btAdd.text = "Income"
        binding.tvSum.text = "Sum: ${DataManager.income}"

        binding.btAdd.setOnClickListener {
            if (binding.etValue.text.toString().isNotEmpty()) {
                    DataManager.income += binding.etValue.text.toString().toFloat()
            }
            binding.tvSum.text = "Sum: ${DataManager.income}"
        }
    }
}