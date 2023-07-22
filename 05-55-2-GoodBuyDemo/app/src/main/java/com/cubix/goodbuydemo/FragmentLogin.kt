package com.cubix.goodbuydemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cubix.goodbuydemo.databinding.FragmentLoginBinding

class FragmentLogin: Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding
        get() = _binding!!

    companion object {
        const val TAG = "FragmentLogin"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.detailsButton.setOnClickListener{
            val mainActivity = activity as MainActivity
            mainActivity.showFragmentList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}