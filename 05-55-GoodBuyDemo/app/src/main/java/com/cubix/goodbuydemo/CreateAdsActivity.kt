package com.cubix.goodbuydemo

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cubix.goodbuydemo.data.AdsData
import com.cubix.goodbuydemo.databinding.ActivityCreateAdsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.net.URLEncoder
import java.util.UUID

class CreateAdsActivity : AppCompatActivity() {

    companion object {
        final const val COLLECTION_ADS = "ads"
        final const val PERMISSION_REQUEST_CODE = 1001
        final const val CAMERA_REQUEST_CODE = 1002
    }

    lateinit var binding: ActivityCreateAdsBinding

    private var uploadBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSend.setOnClickListener {
            if (uploadBitmap == null) {
                uploadAds()
            } else {
                try {
                    uploadAdsWithImage()
                } catch (e: java.lang.Exception){
                    e.printStackTrace()
                }
            }
        }

        binding.btnAttach.setOnClickListener {
            openCamera()
        }

        requestNeededPermission()
    }

    private fun requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CAMERA)) {
                Toast.makeText(this,
                    "I need it for camera", Toast.LENGTH_SHORT).show()
            }

            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.CAMERA),
                PERMISSION_REQUEST_CODE)
        } else {
            // we already have permission
        }
    }

    private fun uploadAdsWithImage() {
        val baos = ByteArrayOutputStream()
        uploadBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageInBytes = baos.toByteArray()

        val storageRef = FirebaseStorage.getInstance().reference
        val newImage = URLEncoder.encode(UUID.randomUUID().toString(), "UTF-8") + ".jpg"
        val newImagesRef = storageRef.child("images/$newImage")

        newImagesRef.putBytes(imageInBytes)
            .addOnFailureListener { exception ->
                Toast.makeText(this@CreateAdsActivity, exception.message, Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener { taskSnapshot ->
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

                newImagesRef.downloadUrl.addOnCompleteListener { task ->
                    uploadAds(task.result.toString()) }
            }
    }

    private fun uploadAds(imageUrl: String = "") {
        val ads = AdsData(
            FirebaseAuth.getInstance().currentUser!!.uid,
            FirebaseAuth.getInstance().currentUser!!.email!!,
            binding.etTitle.text.toString(),
            binding.etBody.text.toString(),
            imageUrl
        )

        val adsCollection = FirebaseFirestore.getInstance().collection(
            COLLECTION_ADS)

        adsCollection.add(ads)
            .addOnSuccessListener {
                Toast.makeText(this@CreateAdsActivity,
                    "Ads SAVED", Toast.LENGTH_LONG).show()

                finish()
            }
            .addOnFailureListener{
                Toast.makeText(this@CreateAdsActivity,
                    "Error ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    //@RequiresApi(Build.VERSION_CODES.TIRAMISU)
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->

        if (result.resultCode == Activity.RESULT_OK){
            val data: Intent? = result.data
            uploadBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                data!!.extras!!.getParcelable("data",Bitmap::class.java)
            else
                data!!.extras!!.get("data") as Bitmap
            binding.imgAttach.setImageBitmap(uploadBitmap)
            binding.imgAttach.visibility = View.VISIBLE
        }
    }

    private fun openCamera() {
        val intentPhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(intentPhoto)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "CAMERA perm granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "CAMERA perm NOT granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}