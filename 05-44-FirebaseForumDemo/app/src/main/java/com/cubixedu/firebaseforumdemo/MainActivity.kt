package com.cubixedu.firebaseforumdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.cubixedu.firebaseforumdemo.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        FirebaseMessaging.getInstance().subscribeToTopic("forumpushes")
    }

    fun loginClick(v: View){
        if (!isFormValid()) {
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            mainBinding.etEmail.text.toString(), mainBinding.etPassword.text.toString()
        ).addOnSuccessListener {
            startActivity(Intent(this@MainActivity, ForumActivity::class.java))
        }.addOnFailureListener{
            Toast.makeText(
                this@MainActivity,
                "Login error: ${it.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun registerClick(v: View){
        if (!isFormValid()){
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            mainBinding.etEmail.text.toString(), mainBinding.etPassword.text.toString()
        ).addOnSuccessListener {
            Toast.makeText(
                this@MainActivity,
                "Registration OK",
                Toast.LENGTH_LONG
            ).show()
        }.addOnFailureListener{
            Toast.makeText(
                this@MainActivity,
                "Error: ${it.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun isFormValid(): Boolean {
        return when {
            mainBinding.etEmail.text.isEmpty() -> {
                mainBinding.etEmail.error = "This field can not be empty"
                false
            }
            mainBinding.etPassword.text.isEmpty() -> {
                mainBinding.etPassword.error = "The password can not be empty"
                false
            }
            else -> true
        }
    }
}