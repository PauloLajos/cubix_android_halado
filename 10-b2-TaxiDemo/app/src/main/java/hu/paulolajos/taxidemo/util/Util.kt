package hu.paulolajos.taxidemo.util

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import hu.paulolajos.taxidemo.ui.fragments.RouteFragment

object Util {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun myApiKey(context: Context): String {
        /*
        val ai = context.packageManager.getApplicationInfo(
            context.packageName, PackageManager.ApplicationInfoFlags.of(0)
        )
        val bundle = ai.metaData
        return bundle.getString("com.google.android.geo.API_KEY").toString()       
         */
        return "AIzaSyBydEvBDuPpCqCRjDHewm_ZKS2PjPgkRmk"
    }

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