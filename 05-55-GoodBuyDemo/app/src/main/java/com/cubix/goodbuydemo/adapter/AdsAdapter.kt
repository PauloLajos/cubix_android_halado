package com.cubix.goodbuydemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cubix.goodbuydemo.data.AdsData
import com.cubix.goodbuydemo.databinding.AdsRowBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class AdsAdapter : RecyclerView.Adapter<AdsAdapter.ViewHolder>, Filterable {

    var context: Context
    var adsList = mutableListOf<AdsData>()
    var adsKeys = mutableListOf<String>()

    var adsFilterList = mutableListOf<AdsData>()

    var currentUid: String

    constructor(context: Context, uid: String) : super() {
        this.context = context
        this.currentUid = uid
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsAdapter.ViewHolder {
        val binding = AdsRowBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdsAdapter.ViewHolder, position: Int) {
        var ads = adsList.get(holder.adapterPosition)

        holder.tvAuthor.text = ads.author
        holder.tvTitle.text = ads.title
        holder.tvBody.text = ads.body

        if (currentUid == ads.uid) {
            holder.btnDelete.visibility = View.VISIBLE
        } else {
            holder.btnDelete.visibility = View.GONE
        }

        holder.btnDelete.setOnClickListener {
            removeAds(holder.adapterPosition)
        }

        if (ads.imgUrl.isNotEmpty()){
            holder.ivPhoto.visibility = View.VISIBLE
            Glide.with(context).load(ads.imgUrl).into(holder.ivPhoto)
        } else {
            holder.ivPhoto.visibility = View.GONE
        }
    }

    fun addAds(ads: AdsData, key: String) {
        adsList.add(ads)
        adsKeys.add(key)
        notifyItemInserted(adsList.lastIndex)
    }

    // when I remove the ads object
    private fun removeAds(index: Int) {
        FirebaseFirestore.getInstance().collection("ads").document(
            adsKeys[index]
        ).delete()

        adsList.removeAt(index)
        adsKeys.removeAt(index)
        notifyItemRemoved(index)
    }

    // when somebody else removes an object
    fun removeAdsByKey(key: String) {
        val index = adsKeys.indexOf(key)
        if (index != -1) {
            adsList.removeAt(index)
            adsKeys.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    override fun getItemCount(): Int {
        return adsList.size
    }

    inner class ViewHolder(private val binding: AdsRowBinding) : RecyclerView.ViewHolder(binding.root){
        var tvAuthor = binding.tvAuthor
        var tvTitle = binding.tvTitle
        var tvBody = binding.tvBody
        var btnDelete = binding.btnDelete
        var ivPhoto = binding.ivPhoto
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    adsFilterList = adsList
                } else {
                    val resultList = mutableListOf<AdsData>()
                    for (row in adsList) {
                        resultList.add(row)
                    }
                    adsFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = adsFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                adsFilterList = results?.values as ArrayList<AdsData>
                notifyDataSetChanged()
            }
        }
    }
}