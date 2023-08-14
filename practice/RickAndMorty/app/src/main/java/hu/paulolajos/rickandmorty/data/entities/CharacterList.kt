package hu.paulolajos.rickandmorty.data.entities

import hu.paulolajos.rickandmorty.data.entities.Character

data class CharacterList(
    val info: Info,
    val results: List<Character>
)