package com.practice.tennisplayers

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.practice.tennisplayers.database.PlayerListItem
import com.practice.tennisplayers.database.PlayersDatabase

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlayerRepository

    init {
        val playerDao = PlayersDatabase
            .getDatabase(application, viewModelScope, application.resources)
            .playerDao()
        repository = PlayerRepository(playerDao)
    }

    fun getAllPlayers(): LiveData<List<PlayerListItem>> {
        return repository.getAllPlayers()
    }
}