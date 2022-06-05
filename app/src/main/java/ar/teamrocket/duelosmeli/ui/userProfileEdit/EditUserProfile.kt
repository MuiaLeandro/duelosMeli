package ar.teamrocket.duelosmeli.ui.userProfileEdit

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ar.teamrocket.duelosmeli.databinding.ActivityEditUserProfileBinding
import com.google.android.material.snackbar.Snackbar

class EditUserProfile : AppCompatActivity() {

    private lateinit var binding: ActivityEditUserProfileBinding
    private val REQUEST_CODE_CAMERA = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvChangeImage.setOnClickListener { view ->
            requestPermission(view)
        }
    }

    private fun requestPermission(view: View) {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                takePhoto()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> {
                Snackbar.make(
                    view,
                    "Este permiso es necesario para tomar una foto",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("OK") {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                }.show()
            }
            else -> requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CODE_CAMERA)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { takePhoto() }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_CAMERA -> {
                if (resultCode == Activity.RESULT_OK) {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    binding.ivUserProfile.setImageBitmap(bitmap)
                }
            }
        }
    }
}