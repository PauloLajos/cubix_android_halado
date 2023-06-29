package com.cubixedu.lollipinexample.pincode

import android.os.Bundle
import com.cubixedu.lollipinexample.databinding.ActivityLockedBinding
import com.github.omadahealth.lollipin.lib.PinActivity

class LockedActivity : PinActivity() {

    private lateinit var binding: ActivityLockedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLockedBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}