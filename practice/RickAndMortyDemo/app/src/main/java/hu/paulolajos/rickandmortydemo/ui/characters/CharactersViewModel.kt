package hu.paulolajos.rickandmortydemo.ui.characters

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.paulolajos.rickandmortydemo.data.repository.CharacterRepository
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    repository: CharacterRepository
) : ViewModel() {

    val characters = repository.getCharacters()
}