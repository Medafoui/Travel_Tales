package es.uc3m.android.traveltales

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import es.uc3m.android.traveltales.databinding.ActivityProfileBinding
class Profile : Activity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener {
            /*TODO*/
        }

    }
}