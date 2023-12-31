package com.cabamancode.fragmentmanagerdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.cabamancode.fragmentmanagerdemo.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
        exitTransition = inflater.inflateTransition(R.transition.slide_left)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userName = (activity as MainActivity).userName

        if ( userName == "") {
            findNavController().navigate(R.id.loginFragment)

        } else {
            binding.tvFragment.text = StringBuilder()
                .append("Main ")
                .append("- Fragment")

            val helloString = arguments?.getString("helloString")
            binding.tvUserName.text = StringBuilder()
                .append(helloString)
                .append(" ")
                .append(userName)
                .append("!")

            binding.btnLogout.setOnClickListener {
                // Logout, clear backstack
                findNavController()
                    .navigate(
                        R.id.loginFragment,
                    null,
                        NavOptions
                            .Builder()
                            .setPopUpTo(findNavController().graph.startDestinationId, true)
                            .build()
                    )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}