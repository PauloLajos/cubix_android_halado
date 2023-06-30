package com.cubixedu.incomeexpensenavigationdemo.page

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentEditStatePageAdapter(fragment: Fragment, private val itemsCount: Int) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            FragmentPageIncome.newInstance()
        } else {
            FragmentPageExpense.newInstance()
        }
    }
}