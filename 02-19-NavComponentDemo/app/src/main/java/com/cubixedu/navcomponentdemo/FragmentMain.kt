package com.cubixedu.navcomponentdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.Person
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.cubixedu.navcomponentdemo.databinding.FragmentMainBinding

class FragmentMain : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnGo.setOnClickListener {
            binding.root.findNavController().navigate(
                R.id.action_fragmentMain_to_fragmentDetail
            )
        }
    }
}