package ar.teamrocket.duelosmeli.ui.userProfile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ar.teamrocket.duelosmeli.MyApplication.Companion.userPreferences
import ar.teamrocket.duelosmeli.databinding.ActivityUserProfileBinding
import ar.teamrocket.duelosmeli.ui.userProfileEdit.EditUserProfile

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setView()

        binding.ivEditProfile.setOnClickListener {
            startActivity(Intent(this, EditUserProfile::class.java))
        }
    }

    private fun setView() {
        binding.tvUserName.text = userPreferences.getName()
        binding.ivUserProfile.setImageURI(Uri.parse(userPreferences.getPhoto()))
    }

    override fun onResume() {
        super.onResume()
        setView()
    }
}