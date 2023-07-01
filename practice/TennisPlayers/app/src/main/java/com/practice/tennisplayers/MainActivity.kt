package com.practice.tennisplayers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practice.tennisplayers.database.PlayerListItem
import com.practice.tennisplayers.databinding.ActivityMainBinding
import com.practice.tennisplayers.details.DetailsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var adapter: PlayerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        // Switch to AppTheme for displaying the activity
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        mainBinding.recyclerView.layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        mainBinding.recyclerView.addItemDecoration(
            DividerItemDecoration(this@MainActivity, RecyclerView
            .VERTICAL)
        )
        adapter = PlayerAdapter(mutableListOf())
        mainBinding.recyclerView.adapter = adapter
        adapter.setOnPlayerTapListener { player ->
            val fragment = DetailsFragment.newInstance(player)

            fragment.show(supportFragmentManager, "DetailsFragment")
        }

        playerViewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)
        playerViewModel.getAllPlayers().observe(this, Observer<List<PlayerListItem>> { players ->
            adapter.swapData(players)
        })
    }
}
