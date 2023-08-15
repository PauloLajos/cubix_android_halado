package hu.paulolajos.taxidemo.ui.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import hu.paulolajos.taxidemo.R
import hu.paulolajos.taxidemo.databinding.FragmentOrdersBinding
import hu.paulolajos.taxidemo.models.OrdersInProgress
import hu.paulolajos.taxidemo.models.User
import hu.paulolajos.taxidemo.ui.DriveActivity
import hu.paulolajos.taxidemo.ui.MapsActivity
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date

class OrdersFragment : Fragment() {

    private var root: View? = null
    private var rating: Double? = null
    private var earnings: Double? = null
    private var ratingCounter = 0

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "OrdersFragment"
    }

    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)

        listenToOrders()

        return binding.root
    }

    fun getPrice(): Double {
        val value = earnings
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        Log.d(TAG, df.format(value))
        return df.format(value).replace(",", ".").toDouble()
    }

    fun getRating(value: Double): Double {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(value).replace(",", ".").toDouble()
    }

    private fun listenToOrders() {
        earnings = 0.0
        rating = 0.0
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("users/" + uid + "/orders")

        binding.ordersRecyclerView.adapter = adapter
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val order = it.getValue(OrdersInProgress::class.java)
                    Log.d(TAG, order!!.distance.toString())

                    adapter.add(OrderItem(order!!))
                    earnings = earnings!! + order.price
                    Log.d(TAG, earnings.toString())

                    if (order.rating != null && order.rating != 0.0) {
                        rating = rating!! + order.rating
                        ratingCounter++
                        Log.d(RouteFragment.TAG, rating.toString())
                    }
                }
                if (activity is MapsActivity) {
                    adapter.setOnItemClickListener { item, view ->
                        val thisItem = item as OrderItem

                        if (thisItem.order.rating == 0.0) {
                            val fragmentRate = RateFragment()
                            val bundle = Bundle()

                            bundle.putLong("timestamp", thisItem.order.timestamp)
                            fragmentRate.arguments = bundle

                            activity!!.supportFragmentManager.beginTransaction()
                                .replace(R.id.container, fragmentRate)
                                .commit()
                        }
                    }
                }
                if (ratingCounter != 0)
                    if (activity is DriveActivity) {
                        val temp = rating!! / ratingCounter
                        binding.ratingOrdersTextView.text =
                            "Average rating: " + getRating(temp).toString()
                    }
                if (activity is DriveActivity) {
                    binding.earningsTextView.text = "Total earnings:" + getPrice() + "$"
                }
            }
        })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

class OrderItem(val order: OrdersInProgress) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.order_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val spanFlag = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        val myCustomizedString =
            SpannableStringBuilder().append("Rate: ", StyleSpan(Typeface.BOLD), spanFlag)
                .append(order.price.toString() + "$")
        val myDistanceString =
            SpannableStringBuilder().append("Distance: ", StyleSpan(Typeface.BOLD), spanFlag)
                .append(order.distance.toString() + "m")

        viewHolder.itemView.findViewById<TextView>(R.id.priceDistanceTextView).text =
            myCustomizedString
        viewHolder.itemView.findViewById<TextView>(R.id.orderTextView).text = myDistanceString

        val sdf = SimpleDateFormat("yy/MM/dd hh:mm")
        val netDate = Date(order.timestamp * 1000)
        val date = sdf.format(netDate)
        viewHolder.itemView.findViewById<TextView>(R.id.textViewDate).text = date.toString()

        if (order.rating == 0.0) {
            viewHolder.itemView.findViewById<TextView>(R.id.textViewRate).text = " -"
            /*
            Picasso.get().load(R.drawable.ic_launcher_background)
                .into(viewHolder.itemView.findViewById<TextView>(R.id.imageViewOrderRated))

             */

        } else {
            viewHolder.itemView.findViewById<TextView>(R.id.textViewRate).text =
                order.rating.toString()
            /*
            Picasso.get().load(R.drawable.ic_launcher_background)
                .into(viewHolder.itemView.findViewById<TextView>(R.id.imageViewOrderRated))

             */
        }
        val ref = FirebaseDatabase.getInstance().getReference("users/" + order.user)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                val customerString =
                    SpannableStringBuilder().append("Client: ", StyleSpan(Typeface.BOLD), spanFlag)
                        .append(user?.username)
                viewHolder.itemView.findViewById<TextView>(R.id.textViewOrderUser).text =
                    customerString
            }

        })
        val secondref = FirebaseDatabase.getInstance().getReference("users/" + order.driver)
        secondref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val driver = snapshot.getValue(User::class.java)
                val driverString = SpannableStringBuilder().append(
                    "Driver: ",
                    StyleSpan(Typeface.BOLD),
                    spanFlag
                ).append(driver?.username)
                viewHolder.itemView.findViewById<TextView>(R.id.textViewOrderDriver).text =
                    driverString
            }
        })
    }
}