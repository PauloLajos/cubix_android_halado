package com.cubix.fragmentsdemo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cubix.fragmentsdemo.MainActivity
import com.cubix.fragmentsdemo.R
import com.cubix.fragmentsdemo.databinding.FragmentTwoBinding

class TwoFragment : Fragment() {

    private var _binding: FragmentTwoBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "TwoFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTwoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textView.text = StringBuilder()
            .append("Two fragment - ")
            .append((activity as MainActivity).fragmentMessage)

        binding.btnGoOneFragment.setOnClickListener {
            (activity as MainActivity).showFragment(R.id.fragment_container_view_program, OneFragment(), tag = OneFragment.TAG)

            //findNavController().navigate(R.id.action_twoFragment_to_oneFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}