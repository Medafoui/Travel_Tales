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
    // Declare the FirebaseAuth and FirebaseFirestore instances
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the FirebaseAuth and FirebaseFirestore instances
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

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

    override fun onResume() {
        super.onResume()

        // Fetch the user's data
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val docRef = db.collection("users").document(currentUser.uid)
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
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame, fragment)
        fragmentTransaction.commit()
    }
}

















