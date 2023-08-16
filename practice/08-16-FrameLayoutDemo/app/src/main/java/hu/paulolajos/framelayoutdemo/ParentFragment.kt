package hu.paulolajos.framelayoutdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import hu.paulolajos.framelayoutdemo.databinding.FragmentParentBinding

class ParentFragment : Fragment() {

    private var _binding : FragmentParentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //val view: View = inflater.inflate(R.layout.fragment_parent, container, false)
        _binding = FragmentParentBinding.inflate(layoutInflater, container, false)

        // on below line creating a child fragment
        val childFragment: Fragment = ChildFragment()

        // on below line creating a fragment transaction and initializing it.
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()

        // on below line replacing the fragment in child container with child fragment.
        transaction.replace(R.id.childFragmentContainer, childFragment).commit()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnChange.setOnClickListener {
            changeFragment(AnotherChildFragment())
        }
    }

    private fun changeFragment(fragment: Fragment) {
        // on below line creating a fragment transaction and initializing it.
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()

        // on below line replacing the fragment in child container with child fragment.
        transaction.replace(R.id.childFragmentContainer, fragment).commit()
    }
}