package hu.paulolajos.taxidemo.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
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
import hu.paulolajos.taxidemo.ui.fragments.OrdersFragment

class DriveActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityDriveBinding

    private var firstListen = false
    private val CHANNEL_ID = "TaxiDemo"
    private val notificationId = 1110
    private lateinit var uid:String

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

        fragment = supportFragmentManager.findFragmentByTag("myfragment")
        binding.navigationView.setNavigationItemSelectedListener(this)

        val ref = FirebaseDatabase.getInstance().getReference(".info/connected")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val connected=snapshot.getValue(Boolean::class.java)
                if(connected!!){
                    val con= FirebaseDatabase.getInstance().getReference("users/"+uid+"/isOnline")
                    con.setValue(true)
                    con.onDisconnect().setValue(false)
                }
            }

        })
        listenOrders()
        if(savedInstanceState==null)
            replaceFragment(MapFragment())
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
        Log.d(TAG,"cos")
        when(item.itemId){
            R.id.mapmenuitem ->{
                Log.d("klik","map")
                replaceFragment(MapFragment())
            }
            R.id.newcourse->{
                Log.d("klik","newcourse")
                replaceFragment(DriverFragment())
            }
            R.id.chatmenuitem->{
                replaceFragment(ChatFragment())
            }
            R.id.ordersMenuItem->{
                replaceFragment(OrdersFragment())
            }
            R.id.logoutmenuitem->{
                logout()
            }
        }
        return true
    }

    private fun replaceFragment(fragment: Fragment, routeId:Int?=null) {
        this.fragment = fragment
        supportFragmentManager.popBackStack()
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        if(routeId != null){
            val bundle = Bundle()
            bundle.putInt("routeId",routeId)
            fragment.arguments = bundle
        }

        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }
}