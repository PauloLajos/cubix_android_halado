package com.cubixedu.permissionsdemo

// NOT automatic import this: import android.Manifest
import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.cubixedu.permissionsdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    private companion object {
        private const val TAG = "PERMISSION_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        // handle singlePermissionBtn click, check and request single permission before
        // doing the task that requires it
        mainBinding.btnSinglePermission.setOnClickListener {
            // permission to be requested
            val permission = Manifest.permission.ACCESS_FINE_LOCATION
            // launcher permission request dialog
            permissionLauncherSingle.launch(permission)
        }

        mainBinding.btnMultiplePermission.setOnClickListener {
            // permissions to be requested
            val permissions = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CALL_PHONE
            )
            // launcher permissions request dialog
            permissionLauncherMultiple.launch(permissions)
        }
    }

    private val permissionLauncherSingle = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // here we will check if permission was now (from permission request dialog) or
        // already granted or not. the param isGranted contains value true/false
        Log.d(TAG, "onActivityResult: isGranted: $isGranted")

        if (isGranted) {
            // Permission granted now do the required task here or call the function for that
            singlePermissionGranted()
        } else {
            // Permission was denied so can't do the task that requires that permission
            Log.d(TAG, "onActivityResult: Permission denied...")
            Toast.makeText(
                this@MainActivity,
                "Permission denied...",
                Toast.LENGTH_SHORT
            ).show()
        }
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

    @SuppressLint("SetTextI18n")
    private fun singlePermissionGranted() {
        // Do the required task here, i'll just set the text to the TextView i.e. resultTv.
        // You can do whatever you want
        mainBinding.tvResult.text = "Single Permission granted. You can do your tasks..."
    }

    @SuppressLint("SetTextI18n")
    private fun multiplePermissionsGranted() {
        // Do the required task here, i'll just set the text to the TextView i.e. resultTv.
        // You can do whatever you want
        mainBinding.tvResult.text = "All Permissions granted. You can do your tasks..."
    }
}