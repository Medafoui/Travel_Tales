package es.uc3m.android.traveltales

import android.app.Activity
import android.os.Bundle
import es.uc3m.android.traveltales.databinding.ActivityProfileBinding

class Profile : Activity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}