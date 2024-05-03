package es.uc3m.android.traveltales

import MyTripsFragment
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.api.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import es.uc3m.android.traveltales.databinding.ActivityPeopleProfileBinding
import es.uc3m.android.traveltales.databinding.ActivityProfileBinding
import java.text.SimpleDateFormat
import java.util.Locale


class PeopleProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPeopleProfileBinding
    // Declare the FirebaseAuth and FirebaseFirestore instances
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: PeopleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeopleProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the FirebaseAuth and FirebaseFirestore instances
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val username = intent.getStringExtra("username")
        val email = intent.getStringExtra("email")
        binding.name.text = username

        db.collection("trips")
            .whereEqualTo("email", email)  // Filter by the user's ID
            .get()
            .addOnSuccessListener { result ->
                val tripList = result.map { it.data }
                val countriesVisited =
                    tripList.mapNotNull { it["tripCountry"] as? String }.distinct().size

                binding.countriesNum.text = countriesVisited.toString()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error getting trips: $e", Toast.LENGTH_SHORT).show()
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
}
