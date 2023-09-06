package hu.paulolajos.taxidemo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hu.paulolajos.taxidemo.R
import hu.paulolajos.taxidemo.models.OrdersInProgress

class RateFragment : Fragment() {
    private var root: View? = null
    private var timestamp: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_rate, container, false)

        val uid = FirebaseAuth.getInstance().uid

        timestamp = requireArguments().getLong("timestamp")

        val ratingBar = root!!.findViewById<RatingBar>(R.id.ratingBarAfter)
        ratingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { p0, p1, p2 ->
                val ref = FirebaseDatabase
                    .getInstance()
                    .reference
                    .child("users")

                ref.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach { it ->
                            val thisUserOrders = it.child("orders")
                            thisUserOrders.children.forEach {
                                val orderThis = it.getValue(OrdersInProgress::class.java)
                                if (orderThis!!.timestamp == timestamp) {
                                    val thisRating = it.child("rating")
                                    thisRating.ref.setValue(p1)
                                }
                            }
                        }
                        val fragmentOrders = OrdersFragment()
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.container, fragmentOrders)?.commit()
                    }
                })
            }
        return root
    }
}