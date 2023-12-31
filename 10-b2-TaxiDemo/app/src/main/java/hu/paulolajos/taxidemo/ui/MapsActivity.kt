package hu.paulolajos.taxidemo.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hu.paulolajos.taxidemo.R
import hu.paulolajos.taxidemo.databinding.ActivityMapsBinding
import hu.paulolajos.taxidemo.ui.fragments.ChatFragment
import hu.paulolajos.taxidemo.ui.fragments.MapFragment
import hu.paulolajos.taxidemo.ui.fragments.OrdersFragment
import hu.paulolajos.taxidemo.ui.fragments.RouteFragment


class MapsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMapsBinding

    private var toggle: ActionBarDrawerToggle? = null
    private var fragment: Fragment? = null
    private lateinit var uid: String

    companion object {
        const val TAG = "MapsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uid = FirebaseAuth.getInstance().uid ?: ""

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        toggle = ActionBarDrawerToggle(
            this,
            binding.drawer,
            binding.toolbar,
            R.string.drawerOpen,
            R.string.drawerClose
        )
        binding.drawer.addDrawerListener(toggle!!)
        toggle!!.syncState()

        //fragment = supportFragmentManager.findFragmentById(R.id.frameContainer)
        binding.navigationView.setNavigationItemSelectedListener(this)

        val ref = FirebaseDatabase
            .getInstance("https://taxidemo-638fe-default-rtdb.europe-west1.firebasedatabase.app")
            .reference
            .child("info").child("connected")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java)
                if (connected!!) {
                    val con =
                        FirebaseDatabase
                            .getInstance("https://taxidemo-638fe-default-rtdb.europe-west1.firebasedatabase.app")
                            .reference
                            .child("users").child(uid).child("isOnline")
                    con.setValue(true)
                    con.onDisconnect().setValue(false)
                }
            }
        })

        if (savedInstanceState == null)
            replaceFragment(MapFragment())
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "cos")
        when (item.itemId) {
            R.id.mapmenuitem -> {
                Log.d(TAG, "MapFragment")
                replaceFragment(MapFragment())
            }

            R.id.newcourse -> {
                Log.d(TAG, "RouteFragment")
                replaceFragment(RouteFragment())
            }

            R.id.chatmenuitem -> {
                Log.d(TAG, "ChatFragment")
                replaceFragment(ChatFragment())
            }

            R.id.ordersMenuItem -> {
                Log.d(TAG, "OrdersFragment")
                replaceFragment(OrdersFragment())
            }

            R.id.logoutmenuitem -> {
                logout()
            }
        }
        return true
    }

    private fun logout() {
        var instance = FirebaseAuth.getInstance().signOut()

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        //
    }

    private fun replaceFragment(fragment: Fragment, routeId: Int? = null) {

        // creating and initializing variable for fragment transaction.
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()

        if (routeId != null) {
            val bundle = Bundle()
            bundle.putInt("routeId", routeId)
            fragment.arguments = bundle
        }

        // replacing the parent container with parent fragment.
        ft.replace(R.id.frameContainer, fragment)

        // committing the transaction.
        ft.commit()

        /*
        this.fragment = fragment
         */
    }
}