package com.cubix.goodbuydemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cubix.goodbuydemo.databinding.FragmentListBinding

class FragmentList: Fragment() {

    private var _binding : FragmentListBinding? = null
    private val binding
        get() = _binding!!

    companion object {
        const val TAG = "FragmentList"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.detailsButton.setOnClickListener{
            val mainActivity = activity as MainActivity
            mainActivity.showFragmentAdd()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}