package hu.paulolajos.databindingdemo

import androidx.recyclerview.widget.RecyclerView
import hu.paulolajos.databindingdemo.databinding.ItemCarBinding

class CarViewHolder(
    private val binding: ItemCarBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(car: Car){
        binding.car = car
    }
}