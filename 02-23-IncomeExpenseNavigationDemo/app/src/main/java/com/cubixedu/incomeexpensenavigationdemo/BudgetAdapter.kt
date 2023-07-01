package com.cubixedu.incomeexpensenavigationdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.cubixedu.incomeexpensenavigationdemo.data.BudgetItem
import java.util.Locale

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
        val lifeCycleOwner = parent.context as LifecycleOwner
        val view = LayoutInflater.from(parent.context).inflate(R.layout.budget_row, parent, false)
        return ViewHolder(view, lifeCycleOwner)
    }

    override fun getItemCount() = budget.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewValue.text = budget[position].value.toString()

        val resourceId = if (budget[position].value >= 0) {
            R.drawable.ic_plus
        } else {
            R.drawable.ic_minus
        }
        holder.imageViewBudget.setImageResource(resourceId)

        holder.itemView.setOnClickListener {
            listener?.invoke(budget[position])
        }
    }

    class ViewHolder(itemView: View, private val lifecycleOwner: LifecycleOwner) : RecyclerView.ViewHolder(itemView) {

        val imageViewBudget: ImageView = itemView.findViewById(R.id.ivBudget)
        val textViewValue: TextView = itemView.findViewById(R.id.tvValue)
        val textViewSum: TextView = itemView.findViewById(R.id.tvSum)
    }
}