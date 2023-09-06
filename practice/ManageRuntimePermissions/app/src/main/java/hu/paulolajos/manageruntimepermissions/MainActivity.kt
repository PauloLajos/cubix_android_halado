package hu.paulolajos.manageruntimepermissions

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import hu.paulolajos.manageruntimepermissions.databinding.ActivityMainBinding
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val permissionManager = PermissionManager.from(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkDetailedPermissionsAndAccessFeature()

        binding.tvText.text = StringBuilder().append("Hello world!")
    }

    private fun checkPermissionsAndAccessFeature() {
        val intentWhenDeniedPermanently = Intent()
        permissionManager
            .request(Permissions.ImgVidCamPerm)
            .rationale(description = "Please approve permission to access this feature", title ="Permission required")
            .permissionPermanentlyDeniedIntent(intentWhenDeniedPermanently)
            .permissionPermanentlyDeniedContent(description= "To access this feature we need permission please provide access to app from app settings")
            .checkAndRequestPermission {
                if (it)
                    //openNewImagePicker()
                    binding.tvText.text = StringBuilder().append("Permission granted")
                else
                    //Utils.showToast(mContext,ResourceHelper.getString(R.string.need_this_permission_msg))
                    binding.tvText.text = StringBuilder().append(R.string.need_this_permission_msg)
            }
    }

    private fun checkDetailedPermissionsAndAccessFeature() {
        val intentWhenDeniedPermanently = Intent()
        permissionManager
            .request(Permissions.ImgVidCamPerm)
            .rationale(
                description = "Please approve permission to access this feature",
                title = "Permission required" )
            .permissionPermanentlyDeniedIntent(intentWhenDeniedPermanently)
            .permissionPermanentlyDeniedContent(
                description = "To access this feature we need permission please provide access to app from app settings" )
            .checkAndRequestDetailedPermission {
                it.entries.forEach { entry ->
                    Log.e("LOG_TAG", "${entry.key} = ${entry.value}")
                }
            }
    }
}