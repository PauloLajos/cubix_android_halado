package com.cubix.firebaseforumdemo

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cubix.firebaseforumdemo.databinding.ActivityCreatePostBinding

class CreatePostActivity : AppCompatActivity() {
    companion object {
        final const val COLLECTION_POSTS = "posts"
        final const val PERMISSION_REQUEST_CODE = 1001
        final const val CAMERA_REQUEST_CODE = 1002
    }

    lateinit var binding: ActivityCreatePostBinding

    private var uploadBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}