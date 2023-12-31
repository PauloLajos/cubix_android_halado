package hu.paulolajos.musicplayer.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import hu.paulolajos.musicplayer.R
import hu.paulolajos.musicplayer.adapters.MusicAdapter
import hu.paulolajos.musicplayer.databinding.ActivitySelectionBinding

class SelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectionBinding
    private lateinit var adapter: MusicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectionBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_MusicPlayer)
        setContentView(binding.root)
        binding.listViewSA.setItemViewCacheSize(30)
        binding.listViewSA.setHasFixedSize(true)
        binding.listViewSA.layoutManager = LinearLayoutManager(this)
        adapter = MusicAdapter(this, MainActivity.songList, selectionActivity = true)
        binding.listViewSA.adapter = adapter
        binding.backButton.setOnClickListener { finish() }
        binding.searchView.clearFocus()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true
            override fun onQueryTextChange(newText: String?): Boolean {
                MainActivity.musicListSearch = ArrayList()
                if (newText != null) {
                    val userInput = newText.lowercase()
                    for (song in MainActivity.songList)
                        if (song.title.lowercase().contains(userInput))
                            MainActivity.musicListSearch.add(song)
                    MainActivity.isSearching = true
                    adapter.updateMusicList(searchList = MainActivity.musicListSearch)
                }
                return true
            }
        })

    }
}