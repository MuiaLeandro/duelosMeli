package ar.teamrocket.duelosmeli.ui.userProfileEdit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ar.teamrocket.duelosmeli.databinding.ActivityEditUserProfileBinding
import ar.teamrocket.duelosmeli.ui.camera.CameraActivity

class EditUserProfile : AppCompatActivity() {

    private lateinit var binding: ActivityEditUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGotoToCamera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
    }
}