package hu.paulolajos.rickandmortydemo.ui.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import hu.paulolajos.rickandmortydemo.databinding.CharactersFragmentBinding

@AndroidEntryPoint
class CharactersFragment : Fragment()
    //,CharactersAdapter.CharacterItemListener
{

    private var _binding: CharactersFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //private val viewModel: CharactersViewModel by viewModels()
    //private lateinit var adapter: CharactersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = CharactersFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}