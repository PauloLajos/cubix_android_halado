package hu.paulolajos.rickandmorty.data.repository

import hu.paulolajos.rickandmorty.data.local.CharacterDao
import hu.paulolajos.rickandmorty.data.remote.CharacterRemoteDataSource
import hu.paulolajos.rickandmorty.utils.performGetOperation
import hu.paulolajos.rickandmorty.data.entities.Character
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val remoteDataSource: CharacterRemoteDataSource,
    private val localDataSource: CharacterDao
) {

    fun getCharacter(id: Int) = performGetOperation(
        databaseQuery = { localDataSource.getCharacter(id) },
        networkCall = { remoteDataSource.getCharacter(id) },
        saveCallResult = { localDataSource.insert(it) }
    )

    fun getCharacters() = performGetOperation(
        databaseQuery = { localDataSource.getAllCharacters() },
        networkCall = { remoteDataSource.getCharacters() },
        saveCallResult = { localDataSource.insertAll(it.results) }
    )
}