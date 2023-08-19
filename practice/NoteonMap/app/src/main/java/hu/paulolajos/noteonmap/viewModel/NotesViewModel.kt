package hu.paulolajos.noteonmap.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hu.paulolajos.noteonmap.data.Note
import hu.paulolajos.noteonmap.data.sampleData

class NotesViewModel : ViewModel() {
    private val _notes = MutableLiveData<MutableList<Note>>()

    val notes: MutableLiveData<MutableList<Note>>
        get() = _notes

    init {
        _notes.value = sampleData.toMutableList()
    }
}