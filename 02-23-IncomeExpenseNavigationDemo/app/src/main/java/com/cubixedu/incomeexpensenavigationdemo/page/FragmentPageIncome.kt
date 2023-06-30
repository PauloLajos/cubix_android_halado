package com.cubixedu.incomeexpensenavigationdemo.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cubixedu.incomeexpensenavigationdemo.MainActivity
import com.cubixedu.incomeexpensenavigationdemo.R
import com.cubixedu.incomeexpensenavigationdemo.databinding.FragmentPageExpenseBinding
import com.cubixedu.incomeexpensenavigationdemo.databinding.FragmentPageIncomeBinding

class FragmentPageIncome : Fragment() {

    private var _binding: FragmentPageIncomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPageIncomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvIncome.text = "Sum: ${(activity as MainActivity).dataManager.income}"
        binding.btIncome.setOnClickListener {
            (activity as MainActivity).dataManager.income += binding.etIncome.text.toString().toFloat()
            binding.tvIncome.text = "Sum: ${(activity as MainActivity).dataManager.income}"
        }
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
}