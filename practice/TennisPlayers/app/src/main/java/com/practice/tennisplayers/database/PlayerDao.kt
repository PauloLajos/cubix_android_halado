package com.practice.tennisplayers.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlayerDao {

    @Delete
    suspend fun deletePlayer(player: Player)

    @Update
    suspend fun updatePlayer(player: Player)

    @Query("SELECT * FROM players WHERE id = :id")
    fun getPlayer(id: Int): LiveData<Player>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllPlayers(players: List<Player>)

    @Query("SELECT id, firstName, lastName, country, favorite, imageUrl FROM players")
    fun getAllPlayers(): LiveData<List<PlayerListItem>>
}