package com.cubix.goodbuydemo.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cubix.goodbuydemo.databinding.AdsRowBinding

class AdsAdapter: ListAdapter<AdsData, AdsAdapter.MyHolder>(COMPARATOR) {

    // Calculates the difference between the available data and new data
    private object COMPARATOR: DiffUtil.ItemCallback<AdsData>(){
        override fun areItemsTheSame(oldItem: AdsData, newItem: AdsData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AdsData, newItem: AdsData): Boolean {
            return oldItem.id == newItem.id
        }
    }

    // An inner class that maps data with the available views
    inner class MyHolder(private val binding: AdsRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(ads: AdsData?) {
            binding.tvTitle.text = ads?.title
            binding.tvDetail.text = ads?.detail
            binding.tvPrice.text = ads?.price
            binding.tvContact.text = ads?.contact

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(AdsRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val ads = getItem(position)
        holder.bind(ads)
    }
}