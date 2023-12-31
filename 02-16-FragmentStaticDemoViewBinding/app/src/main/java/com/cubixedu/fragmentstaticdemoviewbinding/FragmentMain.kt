package com.cubixedu.fragmentstaticdemoviewbinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cubixedu.fragmentstaticdemoviewbinding.databinding.FragmentMainBinding
import java.util.Date

class FragmentMain: Fragment() {

    private var _binding: FragmentMainBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnShow.setOnClickListener {
            binding.tvData.text = Date(System.currentTimeMillis()).toString()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}