package hu.paulolajos.currencydemo.util

import android.content.Context
import android.content.SharedPreferences
import hu.paulolajos.currencydemo.BuildConfig
import hu.paulolajos.currencydemo.main.ApplicationClass
import hu.paulolajos.currencydemo.util.Constant.CON_AES_KEY

class PrefManager(appClass: ApplicationClass) {
    private val pref: SharedPreferences =
        appClass.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

    fun getStringPref(key: String): String? {
        return try {
            val item = pref.getString(encPref(key), null)
            if (item == null) {
                null
            } else {
                val result = decPref(item)
                result.ifBlank { null }
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun encPref(value: Any): String {
        return MCryptAES(CON_AES_KEY).encrypt(value.toString())
    }

    private fun decPref(value: Any): String {
        return MCryptAES(CON_AES_KEY).decrypt(value.toString())
    }
}