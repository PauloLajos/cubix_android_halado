package com.cubixedu.incomeexpensenavigationdemo.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cubixedu.incomeexpensenavigationdemo.DataManager
import com.cubixedu.incomeexpensenavigationdemo.databinding.FragmentPagesBinding

class FragmentPageExpense : Fragment() {

    private var _binding: FragmentPagesBinding? = null
    private val binding get() = _binding!!

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
            FragmentPageExpense()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btAdd.text = "Expense"
        binding.tvSum.text = "Sum: ${DataManager.expense}"

        binding.btAdd.setOnClickListener {
            DataManager.expense += binding.etValue.text.toString().toFloat()
            binding.tvSum.text = "Sum: ${DataManager.expense}"
        }
    }

}