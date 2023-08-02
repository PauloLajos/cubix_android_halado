package hu.paulolajos.fragmentsdemo3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import hu.paulolajos.fragmentsdemo3.databinding.ItemViewBinding

class LetterAdapter :
    RecyclerView.Adapter<LetterAdapter.LetterViewHolder>() {

    // Generates a [CharRange] from 'A' to 'Z' and converts it to a list
    private val list = ('A').rangeTo('Z').toList()

    /**
     * Provides a reference for the views needed to display items in your list.
     */
    inner class LetterViewHolder(val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * Creates new views with R.layout.item_view as its template
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LetterViewHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        // Setup custom accessibility delegate to set the text read
        binding.root.accessibilityDelegate = Accessibility

        return LetterViewHolder(binding)
    }

    /**
     * Replaces the content of an existing view with new data
     */
    override fun onBindViewHolder(holder: LetterViewHolder, position: Int) {
        val item = list[position]
        holder.binding.buttonItem.text = item.toString()

        // Assigns a [OnClickListener] to the button contained in the [ViewHolder]
        holder.binding.buttonItem.setOnClickListener {
            // Create an action from WordList to DetailList
            // using the required arguments
            val action = LetterListFragmentDirections
                .actionLetterListFragmentToWordListFragment(
                    letter = holder.binding.buttonItem.text.toString()
                )
            // Navigate using that action
            holder.binding.root.findNavController().navigate(action)
        }
    }

    // Setup custom accessibility delegate to set the text read with
    // an accessibility service
    companion object Accessibility : View.AccessibilityDelegate() {
        override fun onInitializeAccessibilityNodeInfo(
            host: View,
            info: AccessibilityNodeInfo
        ) {
            super.onInitializeAccessibilityNodeInfo(host, info)
            // With `null` as the second argument to [AccessibilityAction], the
            // accessibility service announces "double tap to activate".
            // If a custom string is provided,
            // it announces "double tap to <custom string>".
            val customString = host.context?.getString(R.string.look_up_words)
            val customClick =
                AccessibilityNodeInfo.AccessibilityAction(
                    AccessibilityNodeInfo.ACTION_CLICK,
                    customString
                )
            info.addAction(customClick)
        }
    }
}