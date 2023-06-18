package com.cubixedu.calllogger

import android.Manifest
import android.content.BroadcastReceiver
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.cubixedu.calllogger.adapter.CustomAdapter
import com.cubixedu.calllogger.data.AppDatabase
import com.cubixedu.calllogger.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    private lateinit var outCallAdapter: CustomAdapter

    private lateinit var mBroadcastReceiver: BroadcastReceiver

    private companion object {
        private const val TAG = "PERMISSION_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        // permissions to be requested
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CALL_PHONE
        )
        // launcher permissions request dialog
        permissionLauncherMultiple.launch(permissions)

        initDataBase()
    }

    private val permissionLauncherMultiple = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        // here we will check if permissions were now (from permission request dialog) or
        // already granted or not

        var allAreGranted = true
        for (isGranted in result.values) {
            Log.d(TAG, "onActivityResult: isGranted: $isGranted")
            allAreGranted = allAreGranted && isGranted
        }

        if (allAreGranted) {
            // All Permissions granted now do the required task here or call the function for that
            multiplePermissionsGranted()
        } else {
            // All or some Permissions were denied so can't do the task that requires that permission
            Log.d(TAG, "onActivityResult: All or some permissions denied...")
            Toast.makeText(
                this@MainActivity,
                "All or some permissions denied...",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun multiplePermissionsGranted() {
        // Do the required task here, you can do whatever you want
    }

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
}