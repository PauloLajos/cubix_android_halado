package hu.paulolajos.currencydemo.util

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build

object Constant {
    const val CON_BASE_URL = "https://api.apilayer.com/exchangerates_data/"
    lateinit var CON_API_ACCESS_KEY: String
    lateinit var CON_AES_KEY: String
    lateinit var CON_AES_IV: String

    fun getKeys(context: Context) {
        // get CON_API_ACCESS_KEY from local.properties file
        try {
            val app: ApplicationInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getApplicationInfo(
                    context.packageName,
                    PackageManager.ApplicationInfoFlags.of(0)
                )
            } else {
                @Suppress("DEPRECATION")
                context.packageManager
                    .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            }

            CON_API_ACCESS_KEY = app.metaData.getString("CON_API_ACCESS_KEY").toString()
            CON_AES_KEY = app.metaData.getString("CON_AES_KEY").toString()
            CON_AES_IV = app.metaData.getString("CON_AES_IV").toString()

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
}