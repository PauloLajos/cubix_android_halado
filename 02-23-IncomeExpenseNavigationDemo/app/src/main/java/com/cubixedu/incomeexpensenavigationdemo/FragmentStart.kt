package com.cubixedu.incomeexpensenavigationdemo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.andrognito.pinlockview.IndicatorDots
import com.andrognito.pinlockview.PinLockListener
import com.cubixedu.incomeexpensenavigationdemo.databinding.FragmentStartBinding
import java.math.BigInteger
import java.security.MessageDigest

class FragmentStart : Fragment() {

    companion object {
        const val TAG = "PinLockView"
    }

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    private val mPinLockListener: PinLockListener = object : PinLockListener {
        override fun onComplete(pin: String) {
            Log.d(TAG, "Pin complete: $pin")

            fun md5Hash(str: String): String {
                val md = MessageDigest.getInstance("MD5")
                val bigInt = BigInteger(1, md.digest(str.toByteArray(Charsets.UTF_8)))
                return String.format("%032x", bigInt)
            }

            // pin: 1234
            if (md5Hash(pin) == "81dc9bdb52d04dc20036dbd8313ed055")
                binding.root.findNavController().navigate(
                    FragmentStartDirections.actionFragmentStartToFragmentMain()
                )
        }

        override fun onEmpty() {
            Log.d(TAG, "Pin empty")
        }

        override fun onPinChange(pinLength: Int, intermediatePin: String) {
            Log.d(
                TAG,
                "Pin changed, new length $pinLength with intermediate pin $intermediatePin"
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.pinLockView.attachIndicatorDots(binding.indicatorDots)
        binding.pinLockView.setPinLockListener(mPinLockListener)
        binding.pinLockView.pinLength = 4
        binding.indicatorDots.indicatorType = IndicatorDots.IndicatorType.FILL_WITH_ANIMATION
    }
}