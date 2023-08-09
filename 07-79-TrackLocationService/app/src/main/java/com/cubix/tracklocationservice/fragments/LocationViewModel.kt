package com.cubix.tracklocationservice.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationViewModel : ViewModel() {
    private val mutableButtonText = MutableLiveData<String>()
    val buttonText: LiveData<String> get() = mutableButtonText

    fun setButtonText(text: String) {
        mutableButtonText.value = text
    }

    private val mutableLogText = MutableLiveData<String>()
    val logText: LiveData<String> get() = mutableLogText

    fun setLogText(text: String) {
        mutableLogText.value = text
    }
}