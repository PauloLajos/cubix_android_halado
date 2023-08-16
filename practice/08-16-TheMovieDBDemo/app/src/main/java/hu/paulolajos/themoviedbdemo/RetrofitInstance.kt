package hu.paulolajos.themoviedbdemo

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api : MovieApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApi::class.java)
    }
}

object ApiKey {

    var tmdbApiKey: String = ""

    // get TMDB_API_KEY from local.properties file
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
            tmdbApiKey = app.metaData.getString("TMDB_API_KEY").toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
}