package com.cabamancode.fragmentmanagerdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cabamancode.fragmentmanagerdemo.databinding.FragmentLoginBinding
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvFragment.text = StringBuilder()
            .append("Login ")
            .append("- Fragment")

        binding.btnLogin.setOnClickListener {
            if (
                isFormValid(
                    binding.etName,
                    binding.etPassword,
                    binding.tilPassword
                )
            ) {
                val authSuccessful: Boolean =
                    userAuthenticate(binding.etName.text.toString(), binding.etPassword.text.toString())

                if (authSuccessful) {
                    // Navigate to next screen
                    val helloString = "Hello"
                    val bundle = bundleOf("helloString" to helloString)

                    findNavController().navigate(R.id.mainFragment, bundle)
                }
            }
        }
    }

    private fun userAuthenticate(userName: String, userPassword: String): Boolean {
        // insert user auth...
        return if (userName != "" && userPassword != "") {
            (activity as MainActivity).userName = userName
            true
        } else
            false
    }

    private fun isFormValid(etName: EditText, etPassword: EditText, tilPassword: TextInputLayout): Boolean {
        return when {
            etName.text.isEmpty() -> {
                etName.error = "Name field can not be empty"
                etName.requestFocus()
                false
            }
            etPassword.text.isEmpty() -> {
                etPassword.error = "The password can not be empty"
                etPassword.requestFocus()
                tilPassword.endIconMode = TextInputLayout.END_ICON_NONE
                false
            }
            else -> true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}