package es.uc3m.android.traveltales

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.uc3m.android.traveltales.databinding.ActivityExploreBinding

class Explore : Activity() {
    private lateinit var binding: ActivityExploreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExploreBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}