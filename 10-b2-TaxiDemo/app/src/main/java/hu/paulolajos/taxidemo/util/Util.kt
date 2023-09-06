package hu.paulolajos.taxidemo.util

import android.app.AlertDialog
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import hu.paulolajos.taxidemo.ui.fragments.RouteFragment

object Util {
    fun isLocationEnabledOrNot(context: Context): Boolean {
        val locationManager: LocationManager? =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    fun showAlertLocation(context: Context, title: String, message: String, btnText: String) {
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setButton(Dialog.BUTTON_POSITIVE,btnText) { dialog, which ->
            dialog.dismiss()
            context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
        alertDialog.show()
    }
}

object ApiKey {
    var mapsApiKey: String = ""

    // get MAPS_API_KEY from local.properties file
    fun getApiKey(application: Application) {
        try {
            val app: ApplicationInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                application.packageManager.getApplicationInfo(
                    application.packageName,
                    PackageManager.ApplicationInfoFlags.of(0)
                )
            } else {
                @Suppress("DEPRECATION")
                application.packageManager
                    .getApplicationInfo(application.packageName, PackageManager.GET_META_DATA)
            }
            mapsApiKey = app.metaData.getString("com.google.android.geo.API_KEY").toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
}