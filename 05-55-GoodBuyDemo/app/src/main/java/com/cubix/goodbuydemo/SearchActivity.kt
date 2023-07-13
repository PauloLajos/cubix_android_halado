package com.cubix.goodbuydemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cubix.goodbuydemo.databinding.ActivityMainBinding
import com.cubix.goodbuydemo.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSearch.setOnClickListener {

        }
    }
}