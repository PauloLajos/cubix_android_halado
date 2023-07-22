package com.cubix.firebasesearchfilteringdemo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.cubix.firebasesearchfilteringdemo.data.Students
import com.cubix.firebasesearchfilteringdemo.databinding.FragmentFirstBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnList.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.btnSend.setOnClickListener {
            sendData()
        }
    }

    private fun sendData() {
        // initializing databaseReference
        databaseReference =
            FirebaseDatabase
                .getInstance()
                .getReference(MainActivity.FIREBASE_PATH)


        // get unique ID
        val studentID = databaseReference.push().key
        if (studentID == null) {
            Log.w(MainActivity.TAG, "Couldn't get push key for student")
            return
        }

        val student = Students(
            studentID,
            binding.etName.text.toString(),
            binding.etRegNumber.text.toString(),
            binding.etGender.text.toString(),
            binding.etAmount.text.toString(),
            binding.etAge.text.toString()
        )

        databaseReference
            .child(studentID!!)
            .setValue(student)
            .addOnSuccessListener {
                Toast.makeText(context, "Student SAVED", Toast.LENGTH_LONG).show()
                binding.etName.text.clear()
                binding.etRegNumber.text.clear()
                binding.etGender.text.clear()
                binding.etAmount.text.clear()
                binding.etAge.text.clear()

                binding.etName.requestFocus()

                // finish()
            }
            .addOnFailureListener{
                Toast.makeText(context,"Error ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}