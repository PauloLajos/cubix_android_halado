package hu.paulolajos.daggerhiltdemo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import hu.paulolajos.daggerhiltdemo.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // viewModels() delegate used to get
    // by view models will automatically construct the viewmodels using Hilt
    private val viewModel:MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cryptocurrencyList = binding.cryptocurrencyList
        cryptocurrencyList.layoutManager = LinearLayoutManager(requireActivity())


        observeCryptoCurrency()
    }

    // Observing the live data
    private fun observeCryptoCurrency() {
        viewModel.cryptoCurrency.observe(requireActivity()) {
            binding.cryptocurrencyList.adapter = CryptocurrencyAdapter(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}