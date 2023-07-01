package com.practice.tennisplayers.details

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.practice.tennisplayers.InternetUrl
import com.practice.tennisplayers.R
import com.practice.tennisplayers.database.Player
import com.practice.tennisplayers.database.PlayerListItem
import com.practice.tennisplayers.databinding.FragmentDetailsBinding
import com.practice.tennisplayers.ui.CircleTransformation
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.Locale

class DetailsFragment : DialogFragment(), Toolbar.OnMenuItemClickListener {

    private var _binding : FragmentDetailsBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var detailViewModel: DetailViewModel

    private lateinit var player: Player
    private lateinit var playerListItem: PlayerListItem

    companion object {

        const val PLAYER_KEY = "player_key"

        fun newInstance(player: PlayerListItem): DetailsFragment {
            val fragment = DetailsFragment()
            val args = Bundle().apply {
                putParcelable(PLAYER_KEY, player)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_Fragment)
        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_delete -> {
                deleteCurrentPlayer()
                true
            }
            else -> false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.detailsToolbar.setNavigationOnClickListener {
            dismiss()
        }

        // inflate the menu with the star
        binding.detailsToolbar.inflateMenu(R.menu.menu_details)
        binding.detailsToolbar.setOnMenuItemClickListener(this)
        val starMenuItem = binding.detailsToolbar.menu.findItem(R.id.action_favorite)
        val checkbox = starMenuItem.actionView as CheckBox

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            arguments?.getParcelable(PLAYER_KEY, PlayerListItem::class.java)?.let { playerListItem = it }
        else
            arguments?.getParcelable<PlayerListItem>(PLAYER_KEY)?.let { playerListItem = it }

        detailViewModel.getPlayer(playerListItem).observe(viewLifecycleOwner, Observer {
            this.player = it
            setupFavoriteToggle(checkbox, it)
            displayPlayer()
        })
    }

    private fun displayPlayer() {
        // load the image
        Picasso.get()
            .load(InternetUrl.espn + player.imageUrl)
            .error(R.drawable.error_list_image)
            .placeholder(R.drawable.default_list_image)
            .transform(CircleTransformation())
            .into(binding.playerImage)

        // Load the player info
        binding.textViewPlayerName.text =
            String.format(Locale.getDefault(), "%s %s", player.firstName, player.lastName)
        binding.textViewPlayerDescription.text = player.description
        binding.textViewPlayerCountry.text = player.country
        binding.textViewPlayerRank.text = player.rank.toString()
        binding.textViewPlayerPoints.text = getString(R.string.player_points,
            NumberFormat.getNumberInstance().format(player.points))
        binding.textViewPlayerAgeGender.text =
            getString(R.string.player_age_gender, player.age, player.gender)
    }

    private fun setupFavoriteToggle(checkBox: CheckBox, player: Player) {
        checkBox.setOnCheckedChangeListener { _, b ->
            player.favorite = b
            detailViewModel.updatePlayer(player)
        }
        checkBox.isChecked = player.favorite
    }

    private fun deleteCurrentPlayer() {
        detailViewModel.deletePlayer(player)
        dismiss()
    }
}