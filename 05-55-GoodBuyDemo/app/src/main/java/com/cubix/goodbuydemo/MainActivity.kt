package com.cubix.goodbuydemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.cubix.goodbuydemo.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {

            binding.btnLogin.isEnabled = false

            if (isFormValid()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    binding.etEmail.text.toString(), binding.etPassword.text.toString()

                ).addOnSuccessListener {
                    startActivity(Intent(
                        this@MainActivity,
                        AdsScrollingActivity::class.java))

                }.addOnFailureListener{

                    val errorMessage = "Login error: ${it.message}"

                    Toast.makeText(
                        this@MainActivity,
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
                        this@MainActivity,
                        errorMessage,
                        Toast.LENGTH_LONG
                    ).show()

                    binding.tvError.text = errorMessage
                    binding.tvError.visibility = View.VISIBLE

                }.addOnFailureListener{
                    errorMessage = "Error: ${it.message}"

                    Toast.makeText(
                        this@MainActivity,
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
}