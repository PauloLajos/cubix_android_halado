package hu.paulolajos.taxidemo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hu.paulolajos.taxidemo.databinding.ActivityDriveBinding

class DriveActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDriveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriveBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}