package hu.paulolajos.simplemusicplayer3.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import hu.paulolajos.simplemusicplayer3.databinding.FragmentSongListBinding
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import hu.paulolajos.simplemusicplayer3.R
import hu.paulolajos.simplemusicplayer3.adapters.SongListAdapter

class SongListFragment : Fragment() {
    private var _binding: FragmentSongListBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    // Keeps track of which LayoutManager is in use for the [RecyclerView]
    private var isLinearLayoutManager = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Retrieve and inflate the layout for this fragment
        _binding = FragmentSongListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // The usage of an interface lets you inject your own implementation
        val menuHost: MenuHost = requireActivity()

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.layout_menu, menu)

                val layoutButton = menu.findItem(R.id.action_switch_layout)
                setIcon(layoutButton)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_switch_layout -> {
                        // Sets isLinearLayoutManager (a Boolean) to the opposite value
                        isLinearLayoutManager = !isLinearLayoutManager
                        // Sets layout and icon
                        chooseLayout()
                        setIcon(menuItem)

                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        recyclerView = binding.recyclerView

        // Sets the LayoutManager of the recyclerview
        // On the first run of the app, it will be LinearLayoutManager
        chooseLayout()
    }

    /**
     * Frees the binding object when the Fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Sets the LayoutManager for the [RecyclerView] based on the desired orientation of the list.
     *
     * Notice that because the enclosing class has changed from an Activity to a Fragment,
     * the signature of the LayoutManagers has to slightly change.
     */
    private fun chooseLayout() {
        if (isLinearLayoutManager) {
            recyclerView.layoutManager = LinearLayoutManager(context)
        } else {
            recyclerView.layoutManager = GridLayoutManager(context, 4)
        }
        recyclerView.adapter = SongListAdapter()
    }

    private fun setIcon(menuItem: MenuItem?) {
        if (menuItem == null)
            return

        menuItem.icon =
            if (isLinearLayoutManager)
                ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_grid_layout)
            else ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_linear_layout)
    }
}