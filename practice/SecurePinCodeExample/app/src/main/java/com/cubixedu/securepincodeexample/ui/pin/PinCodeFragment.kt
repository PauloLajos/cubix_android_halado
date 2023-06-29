package com.cubixedu.securepincodeexample.ui.pin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cubixedu.securepincodeexample.data.EncryptSharedPreferences
import com.cubixedu.securepincodeexample.databinding.FragmentPinCodeBinding

class PinCodeFragment : Fragment() {

    private var _binding : FragmentPinCodeBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var viewModel: PinCodeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPinCodeBinding.inflate(layoutInflater, container, false)

        val sharedPreferences =
            EncryptSharedPreferences.getInstance(requireContext()).sharedPreferences
        viewModel = ViewModelProvider(
            this,
            PinCodeViewModelFactory(sharedPreferences))[PinCodeViewModel::class.java]

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    companion object {
        fun newInstance() = PinCodeFragment()
    }
}