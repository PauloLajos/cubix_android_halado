package com.cubix.tracklocationservice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.cubix.tracklocationservice.MainActivity
import com.cubix.tracklocationservice.databinding.FragmentLocationBinding

class LocationFragment : Fragment() {

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LocationViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.buttonText.observe(viewLifecycleOwner) { text ->
            // Perform an action with the latest item data.
            binding.foregroundOnlyLocationButton.text = text
        }

        viewModel.logText.observe(viewLifecycleOwner) { text ->
            // Perform an action with the latest item data.
            binding.outputTextView.text = text
        }

        binding.foregroundOnlyLocationButton.setOnClickListener {
            (requireActivity() as MainActivity).buttonClick()
        }
    }

    /**
     * Frees the binding object when the Fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "LocationFragment"
    }
}