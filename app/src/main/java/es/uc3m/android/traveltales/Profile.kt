package es.uc3m.android.traveltales

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import es.uc3m.android.traveltales.databinding.ActivityProfileBinding

class Profile : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(ProfileFragment())

        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.home -> replaceFragment(HomeFragment())
                R.id.navigation_me -> replaceFragment(ProfileFragment())
                R.id.navigation_explore -> replaceFragment(ExploreFragment())
                R.id.navigation_trips -> replaceFragment(MyTripsFragment())
                R.id.navigation_notifications -> replaceFragment(NotificationsFragment())
            }
            true
        }



    }


    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame, fragment)
        fragmentTransaction.commit()
    }


    }

