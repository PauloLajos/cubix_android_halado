package hu.paulolajos.trackerlocation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.paulolajos.trackerlocation.databinding.ActivityMainBinding
import hu.paulolajos.trackerlocation.ui.login.LoginFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, LoginFragment.newInstance())
                .commitNow()
        }
    }
}