package ar.teamrocket.duelosmeli.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ar.teamrocket.duelosmeli.databinding.ActivityUserProfileBinding

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}