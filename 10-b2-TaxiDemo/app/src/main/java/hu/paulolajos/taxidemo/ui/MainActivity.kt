package hu.paulolajos.taxidemo.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hu.paulolajos.taxidemo.R
import hu.paulolajos.taxidemo.databinding.ActivityMainBinding
import hu.paulolajos.taxidemo.util.Util

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var activity: Activity

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this@MainActivity

        if (!Util.isLocationEnabledOrNot(activity)) {
            Util.showAlertLocation(
                activity,
                getString(R.string.gps_enable),
                getString(R.string.please_turn_on_gps),
                getString(R.string.ok)
            )
        }

        requestPermissionsSafely(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 200
        )

        binding.loginButton.setOnClickListener {
            performLogin()
        }

        binding.registerButtonGo.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun requestPermissionsSafely(
        permissions: Array<String>,
        requestCode: Int
    ) {
        requestPermissions(permissions, requestCode)
    }

    private fun performLogin(){
        val login = binding.emailTextRegister.text.toString()
        val password = binding.passwordTextRegister.text.toString()

        if(login.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,"Please enter email/password", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(login,password)

            .addOnCompleteListener {
                if (!it.isSuccessful)
                    return@addOnCompleteListener

                val uid = FirebaseAuth.getInstance().uid ?: ""
                val ref = FirebaseDatabase
                    .getInstance("https://taxidemo-638fe-default-rtdb.europe-west1.firebasedatabase.app")
                    .getReference("/users/$uid/isDriver")

                ref.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onCancelled(databaseError: DatabaseError) {
                        //
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val loggedUser = dataSnapshot.getValue(Int::class.java)
                        userTypeDirecting(loggedUser!!)
                    }
                })
            }

            .addOnFailureListener {
                Toast.makeText(this,"Bad login email/password", Toast.LENGTH_SHORT).show()
            }
    }

    fun userTypeDirecting(loggedUser:Int){

        when (loggedUser) {
            0 -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                Log.d(TAG,loggedUser.toString())
                finish()
            }
            1 -> {
                val intent = Intent(this, DriveActivity::class.java)
                startActivity(intent)
                Log.d(TAG,loggedUser.toString())
                finish()
            }
            else -> {
                Toast.makeText(this,"Bad type of user", Toast.LENGTH_SHORT).show()
            }
        }
    }
}