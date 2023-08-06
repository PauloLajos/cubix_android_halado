package hu.paulolajos.simplemusicplayer2.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import hu.paulolajos.simplemusicplayer2.R
import hu.paulolajos.simplemusicplayer2.databinding.FragmentListBinding
import java.lang.StringBuilder

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
        exitTransition = inflater.inflateTransition(R.transition.slide_left)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSongTitle.text = StringBuilder().append("ATB - Hold you.mp3")
        binding.tvSongTitle.setOnClickListener {
            //findNavController().navigate(R.id.playFragment)
            requireFragmentManager().commit {
                // Instantiate a new instance before adding
                val myFragment = PlayFragment()
                add(R.id.fragment_container_view, myFragment)
                setReorderingAllowed(true)
            }
        }
    }
}