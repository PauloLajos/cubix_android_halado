package hu.paulolajos.rickandmortydemo.data.repository

import hu.paulolajos.rickandmortydemo.data.local.CharacterDao
import hu.paulolajos.rickandmortydemo.data.remote.CharacterRemoteDataSource
import hu.paulolajos.rickandmortydemo.utils.performGetOperation
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val remoteDataSource: CharacterRemoteDataSource,
    private val localDataSource: CharacterDao
) {
/*
    fun getCharacter(id: Int) = performGetOperation(

        databaseQuery = { localDataSource.getCharacter(id) },
        networkCall = { remoteDataSource.getCharacter(id) },
        saveCallResult = { localDataSource.insert(it) }

    )
*/
    fun getCharacters() = performGetOperation(

        databaseQuery = { localDataSource.getAllCharacters() },
        networkCall = { remoteDataSource.getCharacters() },
        saveCallResult = { localDataSource.insertAll(it.results) }

    )
}