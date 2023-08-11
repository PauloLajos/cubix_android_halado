package hu.paulolajos.databindingdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.paulolajos.databindingdemo.databinding.ItemCarBinding

class CarAdapter(
    private val carList: List<Car>
) : RecyclerView.Adapter<CarViewHolder>() {

    private lateinit var binding: ItemCarBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        binding = ItemCarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarViewHolder(binding)
    }

    override fun getItemCount(): Int = carList.size

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = carList[position]
        holder.bind(car)
    }
}