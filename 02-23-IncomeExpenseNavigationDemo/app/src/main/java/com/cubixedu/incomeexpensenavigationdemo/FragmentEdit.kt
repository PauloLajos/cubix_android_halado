package com.cubixedu.incomeexpensenavigationdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.cubixedu.incomeexpensenavigationdemo.data.BudgetDatabase
import com.cubixedu.incomeexpensenavigationdemo.data.BudgetItem
import com.cubixedu.incomeexpensenavigationdemo.databinding.FragmentEditBinding
import com.cubixedu.incomeexpensenavigationdemo.page.FragmentEditStatePageAdapter
import com.google.android.material.tabs.TabLayoutMediator

class FragmentEdit : Fragment() {

    private lateinit var pageNames: Array<String>

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private var pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            Toast.makeText(
                context, "Selected position: $position",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        binding.editViewPager.unregisterOnPageChangeCallback(pageChangeCallback)
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentStatePagerAdapter = FragmentEditStatePageAdapter(requireParentFragment(), 2)
        binding.editViewPager.adapter = fragmentStatePagerAdapter

        binding.editViewPager.registerOnPageChangeCallback(pageChangeCallback)

        pageNames = resources.getStringArray(R.array.tab_names)

        TabLayoutMediator(binding.tabLayout, binding.editViewPager) { tab, position ->
            tab.text = pageNames[position]
        }.attach()

        //binding.mainViewPager.setPageTransformer(ZoomOutPageTransformer())
        //binding.mainViewPager.setPageTransformer(DepthPageTransformer())

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}