package com.cubixedu.incomeexpensenavigationdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cubixedu.incomeexpensenavigationdemo.data.BudgetItem

class BudgetAdapter(
    private val budget: MutableList<BudgetItem>
) : RecyclerView.Adapter<BudgetAdapter.ViewHolder>() {

    private var listener: ((BudgetItem) -> Unit)? = null

    fun swapData(budget: List<BudgetItem>) {
        this.budget.clear()
        this.budget.addAll(budget)
        notifyDataSetChanged()
    }

    fun setOnBudgetTapListener(listener: ((BudgetItem) -> Unit)) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.budget_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = players.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewPlayerName.text =
            String.format(
                Locale.getDefault(), "%s %s", players[position].firstName,
                players[position].lastName)
        holder.textViewPlayerCountry.text = players[position].country


        Picasso.get()
            .load(InternetUrl.espn + players[position].imageUrl)
            .error(R.drawable.error_list_image)
            .placeholder(R.drawable.default_list_image)
            .transform(CircleTransformation())
            .into(holder.imageViewPlayer)

        val resourceId = if (players[position].favorite) {
            R.drawable.ic_star
        } else {
            R.drawable.ic_star_border
        }
        holder.imageViewFavorite.setImageResource(resourceId)

        holder.itemView.setOnClickListener {
            listener?.invoke(players[position])
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageViewPlayer: ImageView = itemView.findViewById(R.id.imageViewPlayer)
        val textViewPlayerName: TextView = itemView.findViewById(R.id.textViewPlayerName)
        val textViewPlayerCountry: TextView = itemView.findViewById(R.id.textViewPlayerCountry)
        val imageViewFavorite: ImageView = itemView.findViewById(R.id.imageViewFavorite)
    }
}