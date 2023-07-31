package hu.paulolajos.musicplayer.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hu.paulolajos.musicplayer.R
import hu.paulolajos.musicplayer.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_MusicPlayer)
        setContentView(binding.root)
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.linkedin.setOnClickListener {
            openLinkPage("https://www.linkedin.com/")
        }
        binding.github.setOnClickListener {
            openLinkPage("https://github.com/")
        }
        binding.instagram.setOnClickListener {
            openLinkPage("https://www.instagram.com/")
        }
        binding.gmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:") // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("paulolajos@gmail.com"))
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
    }

    private fun openLinkPage(id: String) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(id)
        )
        startActivity(intent)
    }
}