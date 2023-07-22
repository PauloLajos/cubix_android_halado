package com.cubix.firebasesearchfilteringdemo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.cubix.firebasesearchfilteringdemo.data.Students
import com.cubix.firebasesearchfilteringdemo.data.StudentsAdapter
import com.cubix.firebasesearchfilteringdemo.databinding.FragmentSecondBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale
import java.util.Locale.filter

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var databaseReference: DatabaseReference

    private val studentAdapter: StudentsAdapter by lazy {
        StudentsAdapter()
    }
    val studentsList = ArrayList<Students>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

/*        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
 */

        // initializing databaseReference
        databaseReference = FirebaseDatabase
            .getInstance()
            .getReference(MainActivity.FIREBASE_PATH)

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                filter(editable.toString())
            }

        })

        binding.btnSearch.setOnClickListener {
            getStudents()
        }
        binding.btnSearchSingle.setOnClickListener {
            searchByName(binding.etsearchValue.text.toString())
        }
        binding.btngender.setOnClickListener {
            filterGender()
        }
    }

    // filter by specified value
    private  fun filter(e: String) {
        //Declaring the array list that holds the filtered values
        val filteredItem = ArrayList<Students>()
        // looping through the array list to obtain the required value
        for (item in studentsList) {
            if (item.name!!.lowercase(Locale.ROOT).contains(e.lowercase(Locale.ROOT))) {
                filteredItem.add(item)
            }
        }
        // adding the filted value to adapter
        studentAdapter.submitList(filteredItem)
    }

    // search by specified value
    private fun searchByName(name: String) {
        // adding a value listener to database reference to perform search
        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Checking if the value exists
                if (snapshot.exists()) {
                    studentsList.clear()
                    // looping through to values
                    for (i in snapshot.children) {
                        val student = i.getValue(Students::class.java)
                        // checking if the name searched is available and adding to the array list
                        if (student!!.name == name){
                            studentsList.add(student)
                        }
                    }
                    //setting data to recyclerview
                    studentAdapter.submitList(studentsList)
                    binding.recyclerStudents.adapter = studentAdapter
                } else{
                    Toast.makeText(context, "Data does not exist", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    // filter in general
    private fun filterGender(){
        //specifying path and filter category and adding a listener
        databaseReference
            .orderByChild("gender")
            .equalTo("female")
            .addValueEventListener(object:ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    studentsList.clear()
                    for (i in snapshot.children) {
                        val female = i.getValue(Students::class.java)
                        studentsList.add(female!!)
                    }
                    studentAdapter.submitList(studentsList)
                    binding.recyclerStudents.adapter = studentAdapter
                } else{
                    Toast.makeText(context, "Data is not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    // fetching all values from firebase
    private fun getStudents() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        val student= i.getValue(Students::class.java)
                        if (student != null) {
                            studentsList.add(student)
                        }
                    }
                    studentAdapter.submitList(studentsList)
                    binding.recyclerStudents.adapter = studentAdapter
                } else {
                    Toast.makeText(context, "Data Does not Exist", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}