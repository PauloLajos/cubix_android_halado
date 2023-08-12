package hu.paulolajos.handlinglifecycles

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

class DemoOwner : LifecycleOwner {

    init {
        lifecycle.addObserver(DemoObserver())
    }

    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    fun startOwner() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    fun stopOwner() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }
}