package hu.paulolajos.framelayoutdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import hu.paulolajos.framelayoutdemo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // creating and initializing variable for fragment transaction.
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()

        // replacing the parent container with parent fragment.
        ft.replace(R.id.parentContainer, ParentFragment())

        // committing the transaction.
        ft.commit()
    }
}