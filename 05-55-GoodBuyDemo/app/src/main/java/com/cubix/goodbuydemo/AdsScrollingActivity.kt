package com.cubix.goodbuydemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cubix.goodbuydemo.adapter.AdsAdapter
import com.cubix.goodbuydemo.data.AdsData
import com.cubix.goodbuydemo.databinding.ActivityAdsScrollingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot

class AdsScrollingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdsScrollingBinding

    private lateinit var adsAdapter: AdsAdapter

    private lateinit var eventListener: EventListener<QuerySnapshot>
    private lateinit var queryRef: CollectionReference
    private var listenerReg: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdsScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title

        binding.fabExpand.setOnClickListener {
            binding.fabAdd.visibility = if (binding.fabAdd.visibility === View.INVISIBLE) View.VISIBLE else View.INVISIBLE
            binding.fabSearch.visibility = if (binding.fabSearch.visibility === View.INVISIBLE) View.VISIBLE else View.INVISIBLE
        }

        binding.fabAdd.setOnClickListener { view ->
            startActivity(Intent(this, CreateAdsActivity::class.java))
        }

        binding.fabSearch.setOnClickListener { view ->
            startActivity(Intent(this, SearchActivity::class.java))
        }

        adsAdapter = AdsAdapter(
            this,
            FirebaseAuth.getInstance().currentUser!!.uid
        )
        binding.recyclerAds.adapter = adsAdapter

        initFirebaseQuery()
    }

    private fun initFirebaseQuery() {
        queryRef = FirebaseFirestore.getInstance().collection(CreateAdsActivity.COLLECTION_ADS)

        eventListener = object : EventListener<QuerySnapshot> {
            override fun onEvent(querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException?) {
                if (e != null) {
                    Toast.makeText(
                        this@AdsScrollingActivity, "Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }

                for (docChange in querySnapshot?.documentChanges!!) {
                    when (docChange.type) {
                        DocumentChange.Type.ADDED -> {
                            val ads = docChange.document.toObject(AdsData::class.java)
                            adsAdapter.addAds(ads, docChange.document.id)
                        }
                        DocumentChange.Type.REMOVED -> {
                            adsAdapter.removeAdsByKey(docChange.document.id)
                        }
                        DocumentChange.Type.MODIFIED -> {}
                        else -> {}
                    }
                }

            }
        }

        listenerReg = queryRef.addSnapshotListener(eventListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        listenerReg?.remove()
    }
}