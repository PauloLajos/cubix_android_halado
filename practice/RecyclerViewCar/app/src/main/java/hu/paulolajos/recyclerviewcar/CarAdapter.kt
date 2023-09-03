package hu.paulolajos.recyclerviewcar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import hu.paulolajos.recyclerviewcar.databinding.ItemCarBinding

class CarAdapter : RecyclerView.Adapter<CarAdapter.ViewHolder>() {

    private lateinit var binding: ItemCarBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarAdapter.ViewHolder {
        binding = ItemCarBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: CarAdapter.ViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = differ.currentList.size

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        fun setData(item : Car){
            binding.apply {
                makeTextView.text = item.make.toString()
                modelTextView.text = item.model
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Car>(){
        override fun areItemsTheSame(oldItem: Car, newItem: Car): Boolean {
            return  oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Car, newItem: Car): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

}