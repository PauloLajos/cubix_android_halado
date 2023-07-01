package com.practice.tennisplayers

import androidx.lifecycle.LiveData
import com.practice.tennisplayers.database.Player
import com.practice.tennisplayers.database.PlayerDao
import com.practice.tennisplayers.database.PlayerListItem

class PlayerRepository(private val playerDao: PlayerDao) {

    fun getAllPlayers(): LiveData<List<PlayerListItem>> {
        return playerDao.getAllPlayers()
    }

    fun getPlayer(id: Int): LiveData<Player> {
        return playerDao.getPlayer(id)
    }

    suspend fun updatePlayer(player: Player) {
        playerDao.updatePlayer(player)
    }

    suspend fun deletePlayer(player: Player) {
        playerDao.deletePlayer(player)
    }
}