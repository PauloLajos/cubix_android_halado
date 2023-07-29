package hu.paulolajos.fragmentmanagerdemo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.google.android.material.snackbar.Snackbar
import hu.paulolajos.fragmentmanagerdemo.MainActivity
import hu.paulolajos.fragmentmanagerdemo.R
import hu.paulolajos.fragmentmanagerdemo.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
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

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userName = (activity as MainActivity).userName

        if ( userName == "") {
            findNavController().navigate(R.id.loginFragment)

        } else {
            binding.tvFragment.text = StringBuilder()
                .append("Profile ")
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

            binding.fab.setOnClickListener {
                Snackbar.make(it, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }

            binding.toolbar.inflateMenu(R.menu.menu_profile)

            binding.toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_settings -> {
                        // Navigate to settings screen.
                        true
                    }
                    else -> false
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}