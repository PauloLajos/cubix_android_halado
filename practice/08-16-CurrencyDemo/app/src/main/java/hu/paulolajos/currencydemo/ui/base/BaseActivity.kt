package hu.paulolajos.currencydemo.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hu.paulolajos.currencydemo.R

//@AndroidEntryPoint
class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }
}