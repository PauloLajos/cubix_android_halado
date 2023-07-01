package com.cubixedu.incomeexpensenavigationdemo

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.cubixedu.incomeexpensenavigationdemo.databinding.FragmentMainBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentMain : Fragment(), OnChartValueSelectedListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            // Handle the back button event
            activity?.finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnEdit.setOnClickListener {
            binding.root.findNavController().navigate(
                FragmentMainDirections.actionFragmentMainToFragmentEdit()
            )
        }

        loadChart()
    }

    private fun loadChart() {

        val entries = ArrayList<PieEntry>()

        val dataSet = PieDataSet(entries, "Balance")

        entries.add(PieEntry(DataManager.income, "Income"))
        entries.add(PieEntry(DataManager.expense, "Expense"))

        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        val colors = ArrayList<Int>()
        colors.add(Color.GREEN)
        colors.add(Color.RED)
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLUE)

        binding.chartBalance.data = data
        binding.chartBalance.highlightValues(null)
        binding.chartBalance.description.text = "Budget"
        binding.chartBalance.invalidate()
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }

    override fun onNothingSelected() {

    }
}