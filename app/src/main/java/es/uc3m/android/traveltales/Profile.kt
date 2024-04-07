package es.uc3m.android.traveltales

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import es.uc3m.android.traveltales.databinding.ActivityProfileBinding

class Profile : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the FirebaseAuth and FirebaseFirestore instances
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        val user = auth.currentUser

        if (user != null) {
            val docRef = db.collection("users").document(user.uid)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val username = document.getString("username")
                        if (username != null) {
                            Log.d("ProfileActivity", "Username: $username")

                            // Create a new instance of ProfileFragment and pass the username to it
                            val profileFragment = ProfileFragment.newInstance(username)
                            replaceFragment(profileFragment)
                        } else {
                            Log.d("ProfileActivity", "No such field 'username'")
                        }
                    } else {
                        Log.d("ProfileActivity", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("ProfileActivity", "get failed with ", exception)
                }
        }

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



//package es.uc3m.android.traveltales
//
//import android.os.Bundle
//import android.util.Log
//import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.Fragment
//import es.uc3m.android.traveltales.databinding.ActivityProfileBinding
//
//class Profile : AppCompatActivity() {
//    private lateinit var binding: ActivityProfileBinding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityProfileBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Get the username from the Intent
//        val username = intent.getStringExtra("username") ?: ""
//        Log.d("ProfileActivity", "Username: $username")
//
//        // Create a new instance of ProfileFragment and pass the username to it
//        val profileFragment = ProfileFragment.newInstance(username)
//        replaceFragment(profileFragment)
//
//        binding.bottomNav.setOnItemSelectedListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.home -> replaceFragment(HomeFragment())
//                R.id.navigation_me -> replaceFragment(profileFragment)
//                R.id.navigation_explore -> replaceFragment(ExploreFragment())
//                R.id.navigation_trips -> replaceFragment(MyTripsFragment())
//                R.id.navigation_notifications -> replaceFragment(NotificationsFragment())
//            }
//            true
//        }
//    }
//
//    private fun replaceFragment(fragment: Fragment) {
//        val fragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.frame, fragment)
//        fragmentTransaction.commit()
//    }
//}
////class Profile : AppCompatActivity() {
////    private lateinit var binding: ActivityProfileBinding
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        binding = ActivityProfileBinding.inflate(layoutInflater)
////        setContentView(binding.root)
////        replaceFragment(ProfileFragment())
////
////        binding.bottomNav.setOnItemSelectedListener { menuItem ->
////            when (menuItem.itemId) {
////
////                R.id.home -> replaceFragment(HomeFragment())
////                R.id.navigation_me -> replaceFragment(ProfileFragment())
////                R.id.navigation_explore -> replaceFragment(ExploreFragment())
////                R.id.navigation_trips -> replaceFragment(MyTripsFragment())
////                R.id.navigation_notifications -> replaceFragment(NotificationsFragment())
////            }
////            true
////        }
////
////
////
////    }
////
////
////    private fun replaceFragment(fragment: Fragment) {
////        val fragmentManager = supportFragmentManager
////        val fragmentTransaction = fragmentManager.beginTransaction()
////        fragmentTransaction.replace(R.id.frame, fragment)
////        fragmentTransaction.commit()
////    }
////
////
////    }
//
