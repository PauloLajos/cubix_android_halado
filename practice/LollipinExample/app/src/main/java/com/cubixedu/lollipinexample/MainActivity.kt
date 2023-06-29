package com.cubixedu.lollipinexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.cubixedu.lollipinexample.databinding.ActivityMainBinding
import com.cubixedu.lollipinexample.pincode.LockedActivity
import com.cubixedu.lollipinexample.pincode.PinCodeActivity
import com.github.omadahealth.lollipin.lib.managers.AppLock
import com.github.omadahealth.lollipin.lib.managers.LockManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var locker = LockManager.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Used to enable our locker first time
        binding.btnEnable.setOnClickListener {

            // We are fwding app to PinCodeKotlin
            // class for enabling the PinCode
            val intent = Intent(this@MainActivity, PinCodeActivity::class.java)

            // We add some extras which is provided by library
            intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK)
            startActivityForResult(intent, 101)
        }
        binding.btnChangePin.setOnClickListener {
            val intent = Intent(this@MainActivity, PinCodeActivity::class.java)

            // We are checking that is our passcode is already set or not
            // In simple terms if user is new and he didn't set the passcode
            // then we will send it to choose new passcode
            if (locker.isAppLockEnabled && locker.appLock.isPasscodeSet) {
                intent.putExtra(AppLock.EXTRA_TYPE, AppLock.CHANGE_PIN)
                startActivity(intent)
            } else {
                intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK)
                startActivityForResult(intent, 101)
            }
        }
        binding.btnGoToLocked.setOnClickListener {
            // This is simple intent for LockedActivity
            val intent = Intent(this@MainActivity, LockedActivity::class.java)
            startActivity(intent)
        }
        // This is to disable the PinCode
        binding.btnDisable.setOnClickListener {
            locker.disableAppLock()
            Toast.makeText(this,"Disabled pin code successfully", Toast.LENGTH_LONG).show()
        }
    }

    // This is used to get some external calls to our activity
    // as we are passing some result code for some operations
    // which will send the result and the data by this method
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            101 -> {
                Toast.makeText(this, "PinCode enabled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}