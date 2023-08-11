package hu.paulolajos.databindingdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import hu.paulolajos.databindingdemo.databinding.ActivityMainBinding
import java.util.Random

class MainActivity : AppCompatActivity() {

    // Data binding !!!
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // databinding
        binding.dataBindingDemoString = "Hello data binding!"

        // viewbinding...
        binding.tvViewBinding.text = "Hello view binding!"

        // databinding with dataclass
        binding.car = Car("Suzuki Vitara", "2020.01.01.")

        // RecyclerView
        val recyclerViewNews = binding.rvCars
        val cars = DummyData.getDummyData()
        val carAdapter = CarAdapter(cars)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recyclerViewNews.adapter = carAdapter
        recyclerViewNews.layoutManager = layoutManager
        recyclerViewNews.setHasFixedSize(true)
        recyclerViewNews.addItemDecoration(
            DividerItemDecoration(
                this,
                layoutManager.orientation
            )
        )

        // imput value
        binding.btnDemo.setOnClickListener {
            Toast.makeText(this, binding.cityName,Toast.LENGTH_LONG).show()
        }

        // image change
        val rand = Random()
        changeImage(rand)

        binding.btnImage.setOnClickListener {
            changeImage(rand)
        }
    }

    private fun changeImage(rand: Random) {
        binding.imageUrl = "://picsum.photos/200/200/?image=${rand.nextInt(1000)}"
    }

    companion object {
        @JvmStatic
        @BindingAdapter(value = ["setImageUrl"])
        fun ImageView.bindImageUrl(url: String?) {
            if (!url.isNullOrBlank()) {
                Glide.with(this.context)
                    .load("https$url")
                    .error("http$url")
                    .into(this)
            }
        }
    }
}