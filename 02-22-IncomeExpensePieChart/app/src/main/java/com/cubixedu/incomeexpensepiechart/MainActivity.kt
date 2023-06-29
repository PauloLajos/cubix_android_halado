package com.cubixedu.incomeexpensepiechart

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.cubixedu.incomeexpensepiechart.databinding.ActivityMainBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

class MainActivity : AppCompatActivity(), OnChartValueSelectedListener {

    private var income = 100000
    private var expense = 50000

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.chartBalance.setEntryLabelTextSize(12f)

        updateChart()

        mainBinding.btnIncome.setOnClickListener {
            if (!TextUtils.isEmpty(mainBinding.etMoney.text.toString())) {
                income += mainBinding.etMoney.text.toString().toInt()
                updateChart()
            }
        }

        mainBinding.btnExpense.setOnClickListener {
            if (!TextUtils.isEmpty(mainBinding.etMoney.text.toString())) {
                expense += mainBinding.etMoney.text.toString().toInt()
                updateChart()
            }
        }

        mainBinding.chartBalance.setOnChartValueSelectedListener(this)
    }

    private fun updateChart() {
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(income.toFloat(), "Income"))
        entries.add(PieEntry(expense.toFloat(), "Expense"))

        val dataSet = PieDataSet(entries, "Balance")

        dataSet.setDrawIcons(false)

        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        val colors = ArrayList<Int>()
        colors.add(Color.GREEN)
        colors.add(Color.RED)

        /*for (c in ColorTemplate.PASTEL_COLORS) {
            colors.add(c)
        }*/
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLUE)

        mainBinding.chartBalance.data = data

        mainBinding.chartBalance.highlightValues(null)

        mainBinding.chartBalance.invalidate()
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }

    override fun onNothingSelected() {

    }
}