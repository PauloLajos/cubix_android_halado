package com.cubixedu.dynamicfragmentdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cubixedu.dynamicfragmentdemo.databinding.FragmentDetailsBinding

class DetailsFragment(): Fragment() {

    companion object {
        const val TAG = "DetailFragment"
        private const val NAME = "NAME"

        fun newInstance(name: String): DetailsFragment{
            val fragment = DetailsFragment()
            val bundle = Bundle()
            bundle.putString(NAME,name)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var _binding : FragmentDetailsBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name=requireArguments().getString(NAME)

        binding.nameTextView.text = "Hello $name"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}