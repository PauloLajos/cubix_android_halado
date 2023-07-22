package com.cubix.goodbuydemo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cubix.goodbuydemo.data.AdsAdapter
import com.cubix.goodbuydemo.data.AdsData
import com.cubix.goodbuydemo.databinding.FragmentListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class FragmentList: Fragment() {

    private var _binding : FragmentListBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var databaseReference: DatabaseReference

    private val adsAdapter: AdsAdapter by lazy {
        AdsAdapter()
    }
    val adsList = ArrayList<AdsData>()

    companion object {
        const val TAG = "FragmentList"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            getAds()
        }
        binding.btnSearchSingle.setOnClickListener {
            searchByTitle(binding.etsearchValue.text.toString())
        }
        binding.btngender.setOnClickListener {
            filterPrice()
        }

        binding.btnAdd.setOnClickListener {
            val mainActivity = activity as MainActivity
            mainActivity.showFragmentAdd()
        }
    }

    // filter by specified value
    private  fun filter(e: String) {
        //Declaring the array list that holds the filtered values
        val filteredItem = ArrayList<AdsData>()
        // looping through the array list to obtain the required value
        for (item in adsList) {
            if (item.title!!.lowercase(Locale.ROOT).contains(e.lowercase(Locale.ROOT))) {
                filteredItem.add(item)
            }
        }
        // adding the filted value to adapter
        adsAdapter.submitList(filteredItem)
    }

    // search by specified value
    private fun searchByTitle(title: String) {
        // adding a value listener to database reference to perform search
        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Checking if the value exists
                if (snapshot.exists()) {
                    adsList.clear()
                    // looping through to values
                    for (i in snapshot.children) {
                        val ads = i.getValue(AdsData::class.java)
                        // checking if the name searched is available and adding to the array list
                        if (ads!!.title == title){
                            adsList.add(ads)
                        }
                    }
                    //setting data to recyclerview
                    adsAdapter.submitList(adsList)
                    binding.recyclerAds.adapter = adsAdapter
                } else{
                    Toast.makeText(context, "Data does not exist", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    // filter in general
    private fun filterPrice(){
        //specifying path and filter category and adding a listener
        databaseReference
            .orderByChild("price")
            .equalTo("100")
            .addValueEventListener(object:ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()) {
                        adsList.clear()
                        for (i in snapshot.children) {
                            val price = i.getValue(AdsData::class.java)
                            adsList.add(price!!)
                        }
                        adsAdapter.submitList(adsList)
                        binding.recyclerAds.adapter = adsAdapter
                    } else{
                        Toast.makeText(context, "Data is not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    // fetching all values from firebase
    private fun getAds() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        val ads = i.getValue(AdsData::class.java)
                        if (ads != null) {
                            adsList.add(ads)
                        }
                    }
                    adsAdapter.submitList(adsList)
                    binding.recyclerAds.adapter = adsAdapter
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