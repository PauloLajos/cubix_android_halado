package com.cubix.goodbuydemo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cubix.goodbuydemo.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class FragmentLogin: Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding
        get() = _binding!!

    companion object {
        const val TAG = "FragmentLogin"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {

            binding.btnLogin.isEnabled = false

            if (isFormValid()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    binding.etEmail.text.toString(), binding.etPassword.text.toString()

                ).addOnSuccessListener {
                    val mainActivity = activity as MainActivity
                    mainActivity.showFragmentList()

                }.addOnFailureListener{

                    val errorMessage = "Login error: ${it.message}"

                    Toast.makeText(
                        context,
                        errorMessage,
                        Toast.LENGTH_LONG
                    ).show()

                    binding.tvError.text = errorMessage
                    binding.tvError.visibility = View.VISIBLE
                }
            }
        }

        binding.btnRegister.setOnClickListener {

            binding.btnRegister.isEnabled = false

            if (isFormValid()) {

                var errorMessage: String

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.etEmail.text.toString(), binding.etPassword.text.toString()

                ).addOnSuccessListener {
                    errorMessage = "Registration OK"

                    Toast.makeText(
                        context,
                        errorMessage,
                        Toast.LENGTH_LONG
                    ).show()

                    binding.tvError.text = errorMessage
                    binding.tvError.visibility = View.VISIBLE

                }.addOnFailureListener{
                    errorMessage = "Error: ${it.message}"

                    Toast.makeText(
                        context,
                        errorMessage,
                        Toast.LENGTH_LONG
                    ).show()

                    binding.tvError.text = errorMessage
                    binding.tvError.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun isFormValid(): Boolean {
        return when {
            binding.etEmail.text.isEmpty() -> {
                binding.etEmail.error = "This field can not be empty"
                false
            }
            binding.etPassword.text.isEmpty() -> {
                binding.etPassword.error = "The password can not be empty"
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