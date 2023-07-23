package com.cubix.fragmentsdemo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cubix.fragmentsdemo.MainActivity
import com.cubix.fragmentsdemo.R
import com.cubix.fragmentsdemo.databinding.FragmentXmlBinding

class XmlFragment : Fragment() {

    private var _binding: FragmentXmlBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "XmlFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentXmlBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textView.text = StringBuilder()
            .append("Xml fragment - ")
            .append((activity as MainActivity).fragmentMessage)

        binding.btnGoOneFragment.setOnClickListener {
            (activity as MainActivity).showFragment(R.id.fragment_container_view_program, OneFragment(), OneFragment.TAG)
        }
        binding.btnGoTwoFragment.setOnClickListener {
            (activity as MainActivity).showFragment(R.id.fragment_container_view_program, TwoFragment(), TwoFragment.TAG)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}