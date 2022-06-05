package ar.teamrocket.duelosmeli.ui.userProfileEdit

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ar.teamrocket.duelosmeli.R
import ar.teamrocket.duelosmeli.databinding.ActivityEditUserProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class EditUserProfile : AppCompatActivity() {

    private lateinit var binding: ActivityEditUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvChangeImage.setOnClickListener { view ->
            if (allPermissionsGranted()) {
                showDialog()
            } else {
                requestPermissions(view)
            }
        }
    }

    private fun showDialog() {
        val options = arrayOf("Tomar foto", "Seleccionar de la galería", "Eliminar foto")

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.cambiar_foto)
            .setItems(options) { dialog, which ->
                when (options[which]) {
                    "Tomar foto" -> takePhoto()
                    "Seleccionar de la galería" -> takeGalleryPhoto()
                    "Eliminar foto" -> deletePhoto()
                }
            }
            .show()
    }

    private fun deletePhoto() {
        Toast.makeText(this, "Eliminar Foto", Toast.LENGTH_SHORT).show()
    }

    private fun takeGalleryPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_GALLERY)
    }

    private fun requestPermissions(view: View) {
        when {
            REQUIRED_PERMISSIONS.all {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    it
                )
            } -> {
                Snackbar.make(
                    view,
                    "Estos permisos son necesarios para cambiar la foto de perfil",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("OK") {
                    ActivityCompat.requestPermissions(
                        this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
                    )
                }.show()
            }
            else -> ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CODE_PERMISSIONS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_PERMISSIONS -> {
                if (resultCode == Activity.RESULT_OK) {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    binding.ivUserProfile.setImageBitmap(bitmap)
                }
            }
            REQUEST_CODE_GALLERY -> {
                if (resultCode == Activity.RESULT_OK) {
                    val image = data?.data
                    binding.ivUserProfile.setImageURI(image)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                showDialog()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_GALLERY = 20
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).toTypedArray()
    }
}