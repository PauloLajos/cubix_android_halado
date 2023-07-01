package com.cubixedu.incomeexpensenavigationdemo.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cubixedu.incomeexpensenavigationdemo.DataManager
import com.cubixedu.incomeexpensenavigationdemo.databinding.FragmentPagesBinding

class FragmentPageIncome : Fragment() {

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
            FragmentPageIncome()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btAdd.text = "Income"
        binding.tvSum.text = "Sum: ${DataManager.income}"

        binding.btAdd.setOnClickListener {
            DataManager.income += binding.etValue.text.toString().toFloat()
            binding.tvSum.text = "Sum: ${DataManager.income}"
        }
    }
}