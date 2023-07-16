package com.cubix.goodbuydemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.cubix.goodbuydemo.adapter.AdsAdapter
import com.cubix.goodbuydemo.databinding.ActivityMainBinding
import com.cubix.goodbuydemo.databinding.ActivitySearchBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    lateinit var adsAdapter: AdsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.svAdsSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adsAdapter.filter.filter(newText)
                return false
            }
        })

        adsAdapter = AdsAdapter(
            this,
            FirebaseAuth.getInstance().currentUser!!.uid
        )

        binding.rwSearch.adapter = adsAdapter
    }
}