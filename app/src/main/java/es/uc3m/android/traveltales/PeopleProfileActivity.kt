package es.uc3m.android.traveltales

import TripAdapter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import es.uc3m.android.traveltales.databinding.ActivityPeopleProfileBinding

class PeopleProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPeopleProfileBinding
    // Declare the FirebaseAuth and FirebaseFirestore instances
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: TripAdapter
    private lateinit var person: Person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeopleProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the FirebaseAuth and FirebaseFirestore instances
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        person = Person(
            intent.getStringExtra("username").toString(),
            intent.getStringExtra("email").toString(),
            intent.getStringExtra("userId").toString()
        )
        binding.name.text = person.username

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewTrips)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = TripAdapter(emptyList())

        val currentUserId = auth.currentUser?.uid

        if (currentUserId != null) {
            db.collection("users").document(currentUserId)
                .get()
                .addOnSuccessListener { document ->
                    val followingList = document.get("following") as? List<String> ?: emptyList()
                    val isFollowing = followingList.contains(person.userId)

                    binding.btnFollow.text = if (isFollowing) "Unfollow" else "Follow"
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error getting user data: $e", Toast.LENGTH_SHORT).show()
                }
        }

        textUpdate()

        binding.btnFollow.setOnClickListener {
            if (currentUserId != null) {
                db.collection("users").document(currentUserId)
                    .get()
                    .addOnSuccessListener { document ->
                        val followingList =
                            document.get("following") as? List<String> ?: emptyList()

                        if (followingList.contains(person.userId)) {
                            val updatedFollowing = followingList - person.userId.toString()
                            updateList(currentUserId, updatedFollowing, "following")
                            updateFollowersList(updatedFollowing)
                            binding.btnFollow.text = "Follow"
                        } else {
                            val updatedFollowing = followingList + person.userId.toString()
                            updateList(currentUserId, updatedFollowing, "following")
                            updateFollowersList(updatedFollowing)
                            binding.btnFollow.text = "Unfollow"
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error getting user data: $e", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        }

        db.collection("trips")
            .whereEqualTo("userId", person.userId)
            .get()
            .addOnSuccessListener { result ->
                val tripList = result.map { it.data }
                val countriesVisited =
                    tripList.mapNotNull { it["tripCountry"] as? String }.distinct().size
                binding.countriesNum.text = countriesVisited.toString()

                adapter.tripList = tripList
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
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

    private fun updateList(id: String, updatedList: List<String?>, field: String) {
        db.collection("users").document(id)
            .update(field, updatedList)
            .addOnSuccessListener {
                //Toast.makeText(this, "Following list updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error updating following list: $e", Toast.LENGTH_SHORT).show()
            }
        textUpdate()
    }
    private fun updateFollowersList(followingList: List<String>) {
        db.collection("users").document(person.userId)
            .get()
            .addOnSuccessListener { document ->
                val followersList =
                    document.get("followers") as? List<String> ?: emptyList()
                if(followingList.contains(person.userId)){
                    val updatedFollowers = followersList + auth.currentUser?.uid
                    updateList(person.userId, updatedFollowers, "followers")
                }
                else {
                    val updatedFollowers = followersList - auth.currentUser?.uid
                    updateList(person.userId, updatedFollowers, "followers")
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error getting user data: $e", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun textUpdate(){
        if (person.userId != null) {
            db.collection("users").document(person.userId)
                .get()
                .addOnSuccessListener { document ->
                    val followingList = document.get("following") as? List<String> ?: emptyList()
                    val followersList = document.get("followers") as? List<String> ?: emptyList()

                    binding.followersNum.text = followersList.size.toString()
                    binding.followingNum.text = followingList.size.toString()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error getting user data: $e", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
