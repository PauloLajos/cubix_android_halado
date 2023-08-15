package hu.paulolajos.rickandmortydemo.data.remote

import hu.paulolajos.rickandmortydemo.data.entities.CharacterList
import hu.paulolajos.rickandmortydemo.data.entities.Character as CharacterItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterService {
    @GET("character")
    suspend fun getAllCharacters() : Response<CharacterList>

    //@GET("character/{id}")
    //suspend fun getCharacter(@Path("id") id: Int): Response<CharacterItem>
}