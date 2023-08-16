package hu.paulolajos.currencydemo.main

import android.app.Application

//@HiltAndroidApp
class ApplicationClass : Application() {
    /*
    @Inject
    lateinit var prefManager: PrefManager

    @Inject
    lateinit var recyclerViewAnimation: LayoutAnimationController

    @Inject
    lateinit var appSetting: AppSetting
     */

    companion object {
        @Volatile
        private lateinit var instance: ApplicationClass
        fun getInstance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        //appSetting.init()
    }
}