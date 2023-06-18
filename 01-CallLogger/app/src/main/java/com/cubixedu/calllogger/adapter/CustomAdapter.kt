package com.cubixedu.calllogger.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cubixedu.calllogger.MainActivity
import com.cubixedu.calllogger.data.AppDatabase
import com.cubixedu.calllogger.data.OutCallEntity
import com.cubixedu.calllogger.databinding.RowOutcallBinding

class CustomAdapter(private val context: Context):
    ListAdapter<OutCallEntity, CustomAdapter.ViewHolder>(CallDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowOutcallBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: RowOutcallBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(outCallItem: OutCallEntity ) {
            binding.tvDate.text = outCallItem.callDate
            binding.tvPhone.text = outCallItem.phoneNumber
            binding.btnCall.setOnClickListener {
                // call the number
                val intentDial = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${outCallItem.phoneNumber}"))
                (context as MainActivity).startActivity(intentDial)
            }
        }
    }
}

class CallDiffCallback : DiffUtil.ItemCallback<OutCallEntity>() {
    override fun areItemsTheSame(oldItem: OutCallEntity, newItem: OutCallEntity): Boolean {
        return oldItem.callId == newItem.callId
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: OutCallEntity, newItem: OutCallEntity): Boolean {
        return oldItem == newItem
    }
}