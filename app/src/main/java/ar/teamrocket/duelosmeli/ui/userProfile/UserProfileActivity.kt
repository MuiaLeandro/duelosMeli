package ar.teamrocket.duelosmeli.ui.userProfile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ar.teamrocket.duelosmeli.data.database.UserPreferences
import ar.teamrocket.duelosmeli.data.preferences.Prefs
import ar.teamrocket.duelosmeli.databinding.ActivityUserProfileBinding
import ar.teamrocket.duelosmeli.ui.HomeActivity
import ar.teamrocket.duelosmeli.ui.MainMenuActivity
import ar.teamrocket.duelosmeli.ui.userProfileEdit.EditUserProfile
import org.koin.android.ext.android.inject

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private val userPreferences: UserPreferences by inject()

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

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
}