package com.cubix.fragmentsdemo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
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
        }

        showFragmentMenu()

    }

    private fun showFragmentMenu() {
        val menuHost: MenuHost = requireActivity()

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.fragmentmenu_action_done -> {
                        Toast.makeText(
                            context,
                            "Menu -Done- selected",
                            Toast.LENGTH_SHORT
                        ).show()

                        true
                    }
                    R.id.fragmentmenu_action_settings -> {
                        Toast.makeText(
                            context,
                            "Menu -Done- selected",
                            Toast.LENGTH_SHORT
                        ).show()

                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}