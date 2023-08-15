package hu.paulolajos.taxidemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.LocationCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hu.paulolajos.taxidemo.R
import hu.paulolajos.taxidemo.databinding.ListItemBinding
import hu.paulolajos.taxidemo.models.AvailableDrive
import hu.paulolajos.taxidemo.models.OrderData
import hu.paulolajos.taxidemo.models.OrdersInProgress

class RecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<AvailableDrive> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DriveViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        )
    }

    fun submitList(blogList: List<AvailableDrive>) {
        items = blogList
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DriveViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    inner class DriveViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.list_title)
        val description: TextView = itemView.findViewById(R.id.list_description)
        fun bind(drive: AvailableDrive) {
            title.setText(drive.name)
            description.setText("Distance: " + drive.distance + " Rate: " + drive.price)
            itemView.setOnClickListener {
                var firstlocation: GeoLocation? = null
                var secondlocation: GeoLocation

                val uid = FirebaseAuth.getInstance().uid
                val ref = FirebaseDatabase
                    .getInstance("https://taxidemo-638fe-default-rtdb.europe-west1.firebasedatabase.app")
                    .getReference("/OrderRequests")

                val geoFire = GeoFire(ref)
                geoFire.getLocation(drive.user, object : LocationCallback {
                    override fun onLocationResult(key: String?, location: GeoLocation?) {
                        if (location != null) {
                            firstlocation = location
                            geoFire.removeLocation(
                                drive.user,
                                GeoFire.CompletionListener { key, error ->

                                })
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError?) {
                        //
                    }

                })

                var refsecond = FirebaseDatabase.getInstance().getReference("/OrderRequestsTarget")

                val geoFiresecond = GeoFire(refsecond)
                geoFiresecond.getLocation(drive.user, object : LocationCallback {
                    override fun onLocationResult(key: String?, location: GeoLocation?) {
                        if (location != null) {
                            geoFiresecond.removeLocation(
                                drive.user,
                                GeoFire.CompletionListener { key, error ->
                                    secondlocation = location
                                    val refthird =
                                        FirebaseDatabase.getInstance()
                                            .getReference("/OrderData/" + key)

                                    refthird.addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onCancelled(error: DatabaseError) {
                                            //To change body of created functions use File | Settings | File Templates.
                                        }

                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            val orderdata = snapshot.getValue(OrderData::class.java)

                                            refsecond = FirebaseDatabase.getInstance()
                                                .getReference("/OrdersInProgress").push()
                                            val orderinprogress = OrdersInProgress(
                                                uid!!,
                                                key!!,
                                                firstlocation!!.latitude,
                                                firstlocation!!.longitude,
                                                secondlocation!!.latitude,
                                                secondlocation!!.longitude,
                                                orderdata!!.price,
                                                orderdata.distance,
                                                0.0,
                                                System.currentTimeMillis() / 1000
                                            )
                                            refsecond.setValue(orderinprogress)

                                            refthird.removeValue()

                                            val reffourth = FirebaseDatabase.getInstance()
                                                .getReference("/users/" + uid + "/status")
                                            reffourth.setValue(true)
                                        }
                                    })
                                })
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError?) {
                        //
                    }
                })
            }
        }
    }
}