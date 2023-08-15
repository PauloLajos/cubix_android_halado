package hu.paulolajos.taxidemo.ui

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.MapFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hu.paulolajos.taxidemo.R
import hu.paulolajos.taxidemo.databinding.ActivityDriveBinding
import hu.paulolajos.taxidemo.ui.fragments.ChatFragment
import hu.paulolajos.taxidemo.ui.fragments.DriverFragment
import hu.paulolajos.taxidemo.ui.fragments.MapFragment as MyMapFragment
import hu.paulolajos.taxidemo.ui.fragments.OrdersFragment

class DriveActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityDriveBinding

    private var firstListen = false
    private val CHANNEL_ID = "TaxiDemo"
    private val notificationId = 1110
    private lateinit var uid: String

    private var toggle: ActionBarDrawerToggle? = null
    private var fragment: Fragment? = null

    companion object {
        const val TAG = "DriverActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        uid = FirebaseAuth.getInstance().uid ?: ""

        toggle = ActionBarDrawerToggle(
            this,
            binding.drawer,
            binding.toolbar,
            R.string.drawerOpen,
            R.string.drawerClose
        )

        binding.drawer.addDrawerListener(toggle!!)
        toggle!!.syncState()

        fragment = supportFragmentManager.findFragmentById(R.id.fragmentmap)
        binding.navigationView.setNavigationItemSelectedListener(this)

        val ref = FirebaseDatabase
            .getInstance("https://taxidemo-638fe-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference(".info/connected")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java)
                if (connected!!) {
                    val con =
                        FirebaseDatabase.getInstance().getReference("users/" + uid + "/isOnline")
                    con.setValue(true)
                    con.onDisconnect().setValue(false)
                }
            }

        })
        listenOrders()
        if (savedInstanceState == null)
            replaceFragment(MyMapFragment())
    }

    private fun createNotificationChannel() {
        val name = "name"
        val descriptionText = "description"

        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "cos")
        when (item.itemId) {
            R.id.mapmenuitem -> {
                Log.d(TAG, "map")
                replaceFragment(MyMapFragment())
            }

            R.id.newcourse -> {
                Log.d(TAG, "newcourse")
                replaceFragment(DriverFragment())
            }

            R.id.chatmenuitem -> {
                replaceFragment(ChatFragment())
            }

            R.id.ordersMenuItem -> {
                replaceFragment(OrdersFragment())
            }

            R.id.logoutmenuitem -> {
                logout()
            }
        }
        return true
    }

    private fun replaceFragment(fragment: Fragment, routeId: Int? = null) {
        this.fragment = fragment
        supportFragmentManager.popBackStack()
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        if (routeId != null) {
            val bundle = Bundle()
            bundle.putInt("routeId", routeId)
            fragment.arguments = bundle
        }

        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }

    private fun listenOrders() {
        val con = FirebaseDatabase.getInstance().getReference("users/" + uid + "/status")
        con.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "onDataChange" + firstListen.toString())
                if (!firstListen) {
                    firstListen = true
                } else {
                    val mystatus = snapshot.getValue(Boolean::class.java)
                    sendNotification(mystatus!!)
                }
            }
        })
    }

    @SuppressLint("MissingPermission")
    fun sendNotification(mystatus: Boolean) {
        Log.d(TAG, "sendNotification" + mystatus.toString())
        if (mystatus) {

            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.starticon)
                .setContentTitle("Status of the order")
                .setContentText("Order added")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(this)) {
                notify(notificationId, builder.build())
            }
        } else {
            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.starticon)
                .setContentTitle("Status of the order")
                .setContentText("Order completed")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(this)) {
                notify(notificationId, builder.build())
            }
        }
    }

    private fun logout() {
        var instance = FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        val con = FirebaseDatabase
                .getInstance("https://taxidemo-638fe-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("users/" + uid + "/isOnline")
        con.setValue(false)

        killActivity()
    }

    private fun killActivity() {
        finish()
    }

    override fun onStop() {
        val uid = FirebaseAuth.getInstance().uid

        // val ref= FirebaseDatabase.getInstance().getReference("/AvailableDrivers")
        // var geofire= GeoFire(ref)
        // geofire.removeLocation(uid,object:GeoFire.CompletionListener{
        //    override fun onComplete(key: String?, error: DatabaseError?) {

        //    }

        // })
        super.onStop()
    }
}