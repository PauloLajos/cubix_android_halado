package com.cubixedu.dynamicfragmentdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cubixedu.dynamicfragmentdemo.databinding.FragmentHomeBinding

class HomeFragment: Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding
        get() = _binding!!

    companion object {
        const val TAG = "HomeFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.detailsButton.setOnClickListener{
            val mainActivity = activity as MainActivity
            mainActivity.showDetails(binding.nameEditText.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}