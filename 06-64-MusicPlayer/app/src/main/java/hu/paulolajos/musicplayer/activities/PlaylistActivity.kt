package hu.paulolajos.musicplayer.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import hu.paulolajos.musicplayer.R
import hu.paulolajos.musicplayer.databinding.ActivityPlaylistBinding
import hu.paulolajos.musicplayer.adapters.PlaylistViewAdapter
import hu.paulolajos.musicplayer.data.MusicPlaylist
import hu.paulolajos.musicplayer.data.Playlist
import hu.paulolajos.musicplayer.databinding.PlaylistAddLayoutBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PlaylistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaylistBinding
    private lateinit var adapter: PlaylistViewAdapter

    companion object {
        var musicPlaylist: MusicPlaylist = MusicPlaylist()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_MusicPlayer)

        setContentView(binding.root)
        binding.backButton.setOnClickListener {
            finish()
        }
        val recyclerView = binding.listViewPA
        adapter = PlaylistViewAdapter(this, playlistList = musicPlaylist.ref)
        recyclerView.adapter = adapter
        recyclerView.setItemViewCacheSize(50)
        recyclerView.hasFixedSize()
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.floatingActionButton.setOnClickListener {
            showCustomDialog()
        }
    }

    private fun showCustomDialog() {
        val customDialog =
            LayoutInflater.from(this).inflate(R.layout.playlist_add_layout, binding.root, false)
        val binder = PlaylistAddLayoutBinding.bind(customDialog)
        val builder = MaterialAlertDialogBuilder(this)
            .setView(customDialog)
            .setTitle("Add New Playlist")
            .setPositiveButton("Add") { dialog, _ ->
                val playlistName = binder.playlistName.text
                val createdBy = binder.yourName.text
                if (playlistName != null && createdBy != null)
                    if (playlistName.isNotEmpty() && createdBy.isNotEmpty()) {
                        addPlaylist(playlistName.toString(), createdBy.toString())
                    }
                dialog.dismiss()
            }
        builder.show()
    }

    private fun addPlaylist(name: String, createdBy: String) {
        var playlistExists = false
        for (i in musicPlaylist.ref) {
            if (name == i.name) {
                playlistExists = true
                break
            }
        }
        if (playlistExists) Toast.makeText(this, "Playlist Already Exist!!", Toast.LENGTH_SHORT)
            .show()
        else {
            val tempPlaylist = Playlist()
            tempPlaylist.name = name
            tempPlaylist.playlist = ArrayList()
            tempPlaylist.createdBy = createdBy
            val calendar = Calendar.getInstance().time
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
            tempPlaylist.createdOn = sdf.format(calendar)
            musicPlaylist.ref.add(tempPlaylist)
            adapter.refreshPlaylist()
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}