package hu.paulolajos.taxidemo.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import hu.paulolajos.taxidemo.databinding.ActivityRegisterBinding
import hu.paulolajos.taxidemo.models.User

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    var roleselection = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.spinnerRole.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                //
            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                roleselection = pos
            }

        }
        binding.registerButtonConfirm.setOnClickListener {
            performRegister()
        }
    }

    private fun performRegister(){

        val login = binding.emailTextRegister.text.toString()
        val password = binding.passwordTextRegister.text.toString()
        val repeatpassword = binding.passwordRepeatTextRegister.text.toString()
        val phone = binding.phoneTextView.text.toString()

        if(login.isEmpty() || password.isEmpty() || repeatpassword.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this,"Complete all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if(repeatpassword != password){
            Toast.makeText(this,"The passwords are not equal", Toast.LENGTH_SHORT).show()
            return
        }

        if(phone.length != 9){
            Toast.makeText(this,"Wrong phone number length", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth
            .getInstance()
            .createUserWithEmailAndPassword(login,password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                saveUserToDatabase()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to Create User (${it.message})", Toast.LENGTH_SHORT).show()
            }

    }

    private fun saveUserToDatabase(){
        val username = binding.usernameText.text.toString()
        val phone = binding.phoneTextView.text.toString()
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase
            .getInstance("https://taxidemo-638fe-default-rtdb.europe-west1.firebasedatabase.app")
            .reference.child("users")
        val user = User(
            uid = uid,
            username = username,
            isDriver = roleselection,
            isOnline = false,
            status = false,
            phone = phone
        )

        ref.child(uid).setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "New User Added", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}