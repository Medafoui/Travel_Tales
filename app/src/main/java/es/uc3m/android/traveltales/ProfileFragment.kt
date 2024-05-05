
package es.uc3m.android.traveltales

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import es.uc3m.android.traveltales.databinding.FragmentProfileBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ProfileFragment : BaseFragment() {
    private var username: String? = null
    private lateinit var binding: FragmentProfileBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieve the username from SharedPreferences
        val sharedPref = activity?.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        username = sharedPref?.getString("username", null)
        Log.d("ProfileFragment", "Username: $username")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        // Find the TextView by its ID
        val usernameTextView = view.findViewById<TextView>(R.id.name)

        if (username == null) {
            // User is not logged in, display a message asking the user to log in
            usernameTextView.text = "Please, login"
            Toast.makeText(context, "Please, login!", Toast.LENGTH_SHORT).show()

        } else {
            // User is logged in, display the welcome message
            Toast.makeText(context, "Welcome, $username!", Toast.LENGTH_SHORT).show()
            usernameTextView.text = username
        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the add trip button by its ID and set a click listener
        binding.btnAdd.setOnClickListener {
            // Start Add_Trip_Activity when the button is clicked
            val intent = Intent(activity, AddTripActivity::class.java)
            startActivity(intent)
        }

        // Find the travel statistics button by its ID and set a click listener
        binding.btnStats.setOnClickListener {
            // Start StatsActivity when the button is clicked
            val intent = Intent(activity, StatsActivity::class.java)
            startActivity(intent)
        }


        // Find the map button by its ID and set a click listener
        binding.btnMap.setOnClickListener {
            // Start MapsActivity when the button is clicked
            val intent = Intent(activity, MapsActivity::class.java)
            startActivity(intent)
        }


        // Find the logout button by its ID and set a click listener
        binding.btnLogout.setOnClickListener {
            // Sign out the user
            FirebaseAuth.getInstance().signOut()

            // Clear the username from SharedPreferences
            val sharedPref = activity?.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
            with (sharedPref?.edit()) {
                this?.remove("username")
                this?.apply()
            }

            // Disable the bottom navigation view
            (activity as Profile).binding.bottomNav.isEnabled = false

            // Redirect to HomeFragment
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.home, HomeFragment())
            transaction?.commit()
        }


        updateCountriesVisited()
        loadMostRecentTripImage()
    }

    private fun updateCountriesVisited() {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        db.collection("trips")
            .whereEqualTo("userId", auth.currentUser?.uid)  // Filter by the user's ID
            .get()
            .addOnSuccessListener { result ->
                val tripList = result.map { it.data }
                val countriesVisited =
                    tripList.mapNotNull { it["tripCountry"] as? String }.distinct().size
                // Find the TextView by its ID and update the number of countries visited
                binding.countriesNum.text = countriesVisited.toString()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error getting trips: $e", Toast.LENGTH_SHORT).show()
            }

        auth.currentUser?.uid?.let {
            db.collection("users").document(it)
                .get()
                .addOnSuccessListener { document ->
                    val followingList = document.get("following") as? List<String> ?: emptyList()
                    val followersList = document.get("followers") as? List<String> ?: emptyList()

                    binding.followersNum.text = followersList.size.toString()
                    binding.followingNum.text = followingList.size.toString()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error getting user data: $e", Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun loadMostRecentTripImage() {
        val db = FirebaseFirestore.getInstance()

        db.collection("trips")
            .whereEqualTo("userId", FirebaseAuth.getInstance().currentUser?.uid)
            .get()
            .addOnSuccessListener { result ->
                val trips = result.documents.mapNotNull { document ->
                    val tripStartDateString = document.getString("tripStartDate")
                    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val tripStartDate = simpleDateFormat.parse(tripStartDateString)
                    if (tripStartDate != null && !tripStartDate.after(Calendar.getInstance().time)) {
                        Pair(com.google.firebase.Timestamp(tripStartDate), document.getString("tripImageUri"))
                    } else {
                        null
                    }
                }
                val mostRecentTrip = trips.maxByOrNull { it.first }
                val tripImageUri = mostRecentTrip?.second
                val imageView = view?.findViewById<ImageView>(R.id.iv_last_trip)
                if (imageView != null) {
                    if (tripImageUri != null) {
                        Glide.with(this)
                            .load(tripImageUri)
                            .into(imageView)
                    } else {
                        // Load the default image when the user has no trips
                        Glide.with(this)
                            .load(R.mipmap.ic_spain_foreground)  // Replace with your default image resource
                            .into(imageView)
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error getting trips: $e", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        fun newInstance(username: String) = ProfileFragment().apply {
            arguments = Bundle().apply {
                putString("username", username)
            }
        }
    }
}





























//
//package es.uc3m.android.traveltales
//
//import BaseFragment
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.TextView
//import android.widget.Toast
//import com.bumptech.glide.Glide
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.Query
//
//class ProfileFragment : BaseFragment() {
//    private var username: String? = null
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Retrieve the username from SharedPreferences
//        val sharedPref = activity?.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
//        username = sharedPref?.getString("username", null)
//        Log.d("ProfileFragment", "Username: $username")
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_profile, container, false)
//
//        // Display the welcome message
//        Toast.makeText(context, "Welcome, $username!", Toast.LENGTH_SHORT).show()
//
//        // Find the TextView by its ID and set the username
//        val usernameTextView = view.findViewById<TextView>(R.id.name)
//        Log.d("ProfileFragment", "Username: $username")
//        usernameTextView.text = username
//
//
//
//
//        return view
//    }
//
//
//
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Find the add trip button by its ID and set a click listener
//        view.findViewById<Button>(R.id.btn_add).setOnClickListener {
//            // Start Add_Trip_Activity when the button is clicked
//            val intent = Intent(activity, Add_Trip_Activity::class.java)
//            startActivity(intent)
//        }
//
//        // Find the travel statistics button by its ID and set a click listener
//        view.findViewById<Button>(R.id.btn_stats).setOnClickListener {
//            // Start StatsActivity when the button is clicked
//            val intent = Intent(activity, StatsActivity::class.java)
//            startActivity(intent)
//        }
//
//
//        // Find the map button by its ID and set a click listener
//        view.findViewById<Button>(R.id.btn_map).setOnClickListener {
//            // Start MapsActivity when the button is clicked
//            val intent = Intent(activity, MapsActivity::class.java)
//            startActivity(intent)
//        }
//
//
//
//
//
//        // Find the logout button by its ID and set a click listener
//        view.findViewById<Button>(R.id.btn_logout).setOnClickListener {
//            // Sign out the user
//            FirebaseAuth.getInstance().signOut()
//
//            // Clear the username from SharedPreferences
//            val sharedPref = activity?.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
//            with (sharedPref?.edit()) {
//                this?.remove("username")
//                this?.apply()
//            }
//
//        // Redirect to HomeFragment
//            val transaction = activity?.supportFragmentManager?.beginTransaction()
//            transaction?.replace(R.id.home, HomeFragment())
//            transaction?.commit()
//
//        }
//
//
//
//        loadMostRecentTripImage()
//    }
//
//
//    private fun loadMostRecentTripImage() {
//        val db = FirebaseFirestore.getInstance()
//
//        db.collection("trips")
//            .orderBy("tripStartDate", Query.Direction.DESCENDING)
//            .limit(1)
//            .get()
//            .addOnSuccessListener { result ->
//                val mostRecentTrip = result.documents.firstOrNull()
//                val tripImageUri = mostRecentTrip?.getString("tripImageUri")
//                if (tripImageUri != null) {
//                    val imageView = view?.findViewById<ImageView>(R.id.iv_last_trip)
//                    if (imageView != null) {
//                        Glide.with(this)
//                            .load(tripImageUri)
//                            .into(imageView)
//                    }
//                }
//            }
//            .addOnFailureListener { e ->
//                Toast.makeText(context, "Error getting trips: $e", Toast.LENGTH_SHORT).show()
//            }
//    }
//
//    companion object {
//        fun newInstance(username: String) = ProfileFragment().apply {
//            arguments = Bundle().apply {
//                putString("username", username)
//            }
//        }
//    }
//}
//
//













