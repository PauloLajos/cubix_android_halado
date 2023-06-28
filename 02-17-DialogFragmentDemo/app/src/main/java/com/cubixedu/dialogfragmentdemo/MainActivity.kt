package com.cubixedu.dialogfragmentdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cubixedu.dialogfragmentdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), QueryFragment.OnQueryFragmentAnswer, SelectFruitFragment.OptionsFragmentInterface {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.btnDialogMessage.setOnClickListener {
            val queryFragment = QueryFragment()
            queryFragment.isCancelable = false

            val bundle = Bundle()
            bundle.putString(KEY_MSG, "Hello Advanced Android")
            queryFragment.arguments = bundle

            queryFragment.show(supportFragmentManager,
                "QueryFragment")
        }

        mainBinding.btnDialogList.setOnClickListener {
            SelectFruitFragment().show(supportFragmentManager, SelectFruitFragment.TAG)
        }
    }

    companion object {
        const val KEY_MSG= "KEY_MSG"
    }

    override fun onPositiveSelected(text: String) {
        mainBinding.tvData.text = "OK selected $text"
    }

    override fun onNegativeSelected() {
        mainBinding.tvData.text = "NOPE selected"
    }

    override fun onOptionsFragmentResult(fruit: String) {
        mainBinding.tvData.text = fruit
    }
}