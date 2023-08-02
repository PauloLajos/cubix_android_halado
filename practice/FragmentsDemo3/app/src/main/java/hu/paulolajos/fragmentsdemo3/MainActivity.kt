package hu.paulolajos.fragmentsdemo3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.paulolajos.fragmentsdemo3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)


    }
}