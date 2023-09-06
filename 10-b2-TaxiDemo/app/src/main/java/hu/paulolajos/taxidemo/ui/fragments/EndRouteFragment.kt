package hu.paulolajos.taxidemo.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hu.paulolajos.taxidemo.R
import hu.paulolajos.taxidemo.models.OrdersInProgress

class EndRouteFragment : Fragment() {

    private var root: View? = null
    private lateinit var myDriver: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myDriver = requireArguments().getString("myDriver").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_end_route, container, false)
        val ratingBar = root!!.findViewById<RatingBar>(R.id.ratingBar)
        ratingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { p0, p1, p2 ->
                Log.d("thisrating", p1.toString())

                val ref = FirebaseDatabase
                    .getInstance()
                    .getReference("/users/" + myDriver)
                    .child("users").child(myDriver).child("orders")
                    .orderByKey()
                    .limitToLast(1)

                ref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach { it ->
                            val orderKey = it.key
                            val orderfirst = it.getValue(OrdersInProgress::class.java)
                            val secondRef = FirebaseDatabase
                                .getInstance()
                                .reference
                                .child("users").child(myDriver).child("orders")
                                .child(orderKey.toString()).child("rating")

                            secondRef.setValue(p1)

                            val refthird = FirebaseDatabase
                                .getInstance()
                                .reference
                                .child("users")

                            refthird.addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(error: DatabaseError) {
                                    //To change body of created functions use File | Settings | File Templates.
                                }

                                override fun onDataChange(snapshot: DataSnapshot) {
                                    snapshot.children.forEach { it ->
                                        val thisUserOrders = it.child("orders")
                                        thisUserOrders.children.forEach {
                                            val orderThis =
                                                it.getValue(OrdersInProgress::class.java)
                                            if (orderThis!!.timestamp == orderfirst!!.timestamp) {
                                                val thisRating = it.child("rating")
                                                thisRating.ref.setValue(p1)
                                            }
                                        }
                                    }
                                }

                            })
                        }
                        val fragmentMap = MapFragment()
                        activity!!.supportFragmentManager.beginTransaction()
                            .replace(R.id.container, fragmentMap).commit()
                    }
                })
            }
        return root
    }
}