package hu.paulolajos.noteonmap.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import hu.paulolajos.noteonmap.data.Note
import hu.paulolajos.noteonmap.data.sampleData

class NotesViewModel : ViewModel() {
    private val _notes = MutableLiveData<MutableList<Note>>()

    companion object {
        private const val PATH = "note"
    }

    val notes: MutableLiveData<MutableList<Note>>
        get() = _notes

    init {
        getDataFromFirebase()

        // _notes.value = sampleData.toMutableList()
        _notes.value = mutableListOf()
    }

    fun addNote(note: Note) {
        //_notes.value?.add( note)

        val database = Firebase.database
        val myRef = database.getReference(PATH)

        myRef.push().setValue(note)
    }

    private fun getDataFromFirebase() {
        val database = Firebase.database
        val myRef = database.getReference(PATH)

        myRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val value = snapshot.getValue<HashMap<String, Any>>()
                val latLng = value?.get("latLng") as HashMap< String, Double>
                _notes.value = _notes.value?.apply { add(
                    Note(
                        user = value["user"] as String, text = value["text"] as String,
                        latLng = LatLng(
                            latLng["latitude"] ?: 0.0, latLng["longitude"] ?: 0.0,
                        ),
                    ),
                )}
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}