package hu.paulolajos.taxidemo.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import hu.paulolajos.taxidemo.R
import hu.paulolajos.taxidemo.databinding.FragmentChatBinding
import hu.paulolajos.taxidemo.models.ChatMessage
import hu.paulolajos.taxidemo.models.OrdersInProgress
import hu.paulolajos.taxidemo.models.User
import java.util.Date

class ChatFragment : Fragment() {

    private var messagesRef: String? = null

    private var toId: String? = null
    private var alreadyAdded: Boolean? = null

    val uid = FirebaseAuth.getInstance().uid

    //val adapter = GroupAdapter<GroupieViewHolder>()
    val adapter = GroupieAdapter()

    private var _binding: FragmentChatBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        const val TAG = "ChatFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Retrieve and inflate the layout for this fragment
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alreadyAdded = true
        loadMessages()

        binding.buttonChat.setOnClickListener {
            val ref = FirebaseDatabase.getInstance()
                .reference
                .child("OrdersInProgress").child(messagesRef.toString()).child("messages")
                .push()

            val message = ChatMessage(
                uid!!,
                toId!!,
                binding.editTextChat.text.toString(),
                System.currentTimeMillis() / 1000
            )
            ref.setValue(message)

            binding.editTextChat.text.clear()
        }
    }

    private fun loadMessages() {
        val chatButton = binding.buttonChat
        val chatEditText = binding.editTextChat
        val personTextView = binding.chatPersonTextView
        val chatRecyclerView = binding.chatRecyclerView
        val warningTextView = binding.warningTextView

        val ref = FirebaseDatabase
            .getInstance()
            .reference
            .child("OrdersInProgress")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (alreadyAdded == true) {
                    snapshot.children.forEach {
                        val orderinprogress = it.getValue(OrdersInProgress::class.java)
                        if (orderinprogress!!.driver == uid) {
                            chatEditText.visibility = View.VISIBLE
                            chatButton.visibility = View.VISIBLE

                            toId = orderinprogress.user
                            messagesRef = it.key

                            fetchMessages()
                            showPerson()

                            Log.d(TAG, "one")

                            alreadyAdded = false

                        } else if (orderinprogress!!.user == uid) {
                            chatEditText.visibility = View.VISIBLE
                            chatButton.visibility = View.VISIBLE

                            toId = orderinprogress.driver
                            messagesRef = it.key

                            fetchMessages()
                            showPerson()

                            Log.d(TAG, "two")

                            alreadyAdded = false
                        }
                    }
                }
                if (alreadyAdded == true) {

                    chatButton.visibility = View.INVISIBLE
                    chatEditText.visibility = View.INVISIBLE
                    personTextView.visibility = View.INVISIBLE
                    chatRecyclerView.visibility = View.INVISIBLE
                    warningTextView.visibility = View.VISIBLE
                }
            }
        })
    }

    fun showPerson() {
        val personTextView = binding.chatPersonTextView
        val ref = FirebaseDatabase
            .getInstance()
            .reference
            .child("users").child(toId.toString())

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val userfrombase = snapshot.getValue(User::class.java)
                personTextView.text = userfrombase!!.username + " Phone: " + userfrombase.phone
            }
        })
    }

    fun fetchMessages() {
        val chatRecyclerView = binding.chatRecyclerView
        chatRecyclerView.adapter = adapter

        val ref =
            FirebaseDatabase
                .getInstance()
                .reference
                .child("OrdersInProgress").child(messagesRef.toString()).child("messages")

        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "Failed to load message")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                if (chatMessage != null) {
                    if (chatMessage.fromId == uid)
                        adapter.add(ChatToItem(chatMessage))
                    else
                        adapter.add(ChatFromItem(chatMessage))
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
        })
    }

    /**
     * Frees the binding object when the Fragment is destroyed.
     */
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

class ChatFromItem(val chatMessage: ChatMessage) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_from_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.text_message_body).text = chatMessage.messageText
        viewHolder.itemView.findViewById<TextView>(R.id.text_message_time).text =
            getDateTimeFromEpocLongOfSeconds(chatMessage.timestamp)
    }

    private fun getDateTimeFromEpocLongOfSeconds(epoc: Long): String? {
        return try {
            val netDate = Date(epoc * 1000)
            netDate.hours.toString() + ":" + netDate.minutes + "." + netDate.seconds
        } catch (e: Exception) {
            e.toString()
        }
    }
}

class ChatToItem(val chatMessage: ChatMessage) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_to_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.text_message_bodyy).text = chatMessage.messageText
        viewHolder.itemView.findViewById<TextView>(R.id.text_message_timee).text =
            getDateTimeFromEpocLongOfSeconds(chatMessage.timestamp)
    }

    private fun getDateTimeFromEpocLongOfSeconds(epoc: Long): String? {
        return try {
            val netDate = Date(epoc * 1000)
            netDate.hours.toString() + ":" + netDate.minutes + "." + netDate.seconds
        } catch (e: Exception) {
            e.toString()
        }
    }
}