package com.cubix.goodbuydemo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cubix.goodbuydemo.data.AdsData
import com.cubix.goodbuydemo.databinding.FragmentAddBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FragmentAdd: Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var databaseReference: DatabaseReference

    companion object {
        const val TAG = "FragmentAdd"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAddBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnList.setOnClickListener{
            val mainActivity = activity as MainActivity
            mainActivity.showFragmentList()
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
        val adsID = databaseReference.push().key
        if (adsID == null) {
            Log.w(TAG, "Couldn't get push key for ads")
            return
        }

        val ads = AdsData(
            adsID,
            FirebaseAuth.getInstance().currentUser!!.uid,
            binding.etTitle.text.toString(),
            binding.etDetail.text.toString(),
            binding.etPrice.text.toString(),
            binding.etContact.text.toString()
        )

        databaseReference
            .child(adsID)
            .setValue(ads)
            .addOnSuccessListener {
                Toast.makeText(context, "ADS SAVED", Toast.LENGTH_LONG).show()

                binding.etTitle.text.clear()
                binding.etDetail.text.clear()
                binding.etPrice.text.clear()
                binding.etContact.text.clear()

                binding.etTitle.requestFocus()

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