package com.cubixedu.calllogger

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.cubixedu.calllogger.adapter.CustomAdapter
import com.cubixedu.calllogger.data.AppDatabase
import com.cubixedu.calllogger.data.OutCallEntity
import com.cubixedu.calllogger.databinding.ActivityMainBinding
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    private lateinit var outCallAdapter: CustomAdapter

    private lateinit var mBroadcastReceiver: BroadcastReceiver

    companion object {
        const val RECEIVER_INTENT = "RECEIVE_INTENT"
        const val RECEIVER_PHONENUMBER = "RECEIVER_PHONENUMBER"
        const val RECEIVER_DATE = "RECEIVER_DATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        initDataBase()
    }

        /**
        mBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val callPhoneNumber = intent.getStringExtra(RECEIVER_PHONENUMBER)
                val callDate = intent.getStringExtra(RECEIVER_DATE)

                // call any method you want here
                thread {
                    AppDatabase.getInstance(context).outCallDAO().addCall(
                        OutCallEntity(
                            null,
                            callPhoneNumber!!,
                            callDate!!
                        )
                    )

                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            mBroadcastReceiver,
            IntentFilter(RECEIVER_INTENT)
        )
    }

    override fun onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver)
        super.onStop()
    }
    **/

    private fun initDataBase() {
        outCallAdapter = CustomAdapter(this)
        findViewById<RecyclerView>(R.id.recyclerCalls).adapter = outCallAdapter

        AppDatabase.getInstance(this).outCallDAO().getAllCalls()
            .observe(this, Observer
                { items ->
                    outCallAdapter.submitList(items)
                }
            )
    }

    private fun requestNeededPermission() {
        /*if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.PROCESS_OUTGOING_CALLS),
                101)
        } else {
        }

         */
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        /*
        when (requestCode) {
            101 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@MainActivity, "perm granted", Toast.LENGTH_SHORT).show()


                } else {
                    Toast.makeText(this@MainActivity,
                        "perm not granted", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }

         */
    }
}