package hu.paulolajos.simplemusicplayer3.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import hu.paulolajos.simplemusicplayer3.R
import hu.paulolajos.simplemusicplayer3.databinding.ItemViewBinding
import hu.paulolajos.simplemusicplayer3.fragments.SongListFragmentDirections


class SongListAdapter :
    RecyclerView.Adapter<SongListAdapter.SongViewHolder>() {

    // Generates a list
    data class SongName(val res: Int, val str: String)

    private val list = arrayListOf(
        SongName(
            R.raw.alesha_dixon_drummer_boy,
            "Alesha Dixon - Drummer boy"
        ),
        SongName(
            R.raw.atb_hold_you,
            "Atb - Hold you"
        ),
        SongName(
            R.raw.balkan_fanatik_ha_te_tudnad,
            "Balkan Fanatik - Ha Te Tudnad"
        ),
        SongName(
            R.raw.chris_rea_road_to_hell,
            "Chris_Rea-Road_To_Hell"
        ),
        SongName(
            R.raw.caro_emerald_back_it_up,
            "Caro_Emerald-Back_It_Up_Official_Video_"
        ),
        SongName(
            R.raw.daniel_powter_bad_day,
            "Daniel_Powter-Bad_Day_Official_Music_Video_"
        ),
        SongName(
            R.raw.depeche_mode_people_are_people,
            "Depeche_Mode-People_Are_People"
        ),
        SongName(
            R.raw.groove_coverage_moonlight_shadow,
            "Groove_Coverage-Moonlight_Shadow"
        ),
        SongName(
            R.raw.haddaway_what_is_love,
            "Haddaway-What_Is_Love_Hq_"
        ),
        SongName(
            R.raw.honeybeast_bodotta,
            "Honeybeast-Bodotta"
        ),
        SongName(
            R.raw.jem_they,
            "Jem-They"
        ),
        SongName(
            R.raw.motiva_zenekar_szabad_enekelni,
            "Motiva_Zenekar-Szabad_Enekelni"
        ),
        SongName(
            R.raw.real_mccoy_another_night,
            "Real_Mccoy-Another_Night"
        ),
        SongName(
            R.raw.vad_fruttik_sarga_zsiguli,
            "Vad_Fruttik-Sarga_Zsiguli"
        ),
    )

    /**
     * Provides a reference for the views needed to display items in your list.
     */
    inner class SongViewHolder(val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * Creates new views with R.layout.item_view as its template
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        // Setup custom accessibility delegate to set the text read
        binding.root.accessibilityDelegate = Accessibility

        return SongViewHolder(binding)
    }

    /**
     * Replaces the content of an existing view with new data
     */
    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val item = list[position]
        holder.binding.buttonItem.text = item.str

        // Assigns a [OnClickListener] to the button contained in the [ViewHolder]
        holder.binding.buttonItem.setOnClickListener {
            // Create an action from WordList to DetailList
            // using the required arguments
            val action = SongListFragmentDirections
                .actionSongListFragmentToSongPlayFragment(
                    song = holder.binding.buttonItem.text.toString(),
                    resId = item.res
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