package hu.paulolajos.handlinglifecycles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 *  https://www.answertopia.com/android-studio/an-android-lifecycle-awareness-tutorial/
 */
class MainActivity : AppCompatActivity() {

    private lateinit var demoOwner: DemoOwner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //lifecycle.addObserver(DemoObserver())

        demoOwner = DemoOwner()
        demoOwner.startOwner()
        demoOwner.stopOwner()
    }
}