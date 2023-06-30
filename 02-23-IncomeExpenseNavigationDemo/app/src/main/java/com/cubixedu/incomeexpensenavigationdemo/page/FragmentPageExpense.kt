package com.cubixedu.incomeexpensenavigationdemo.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cubixedu.incomeexpensenavigationdemo.R
import com.cubixedu.incomeexpensenavigationdemo.databinding.FragmentEditBinding
import com.cubixedu.incomeexpensenavigationdemo.databinding.FragmentPageExpenseBinding

class FragmentPageExpense : Fragment() {

    private var _binding: FragmentPageExpenseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPageExpenseBinding.inflate(inflater, container, false)
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
}