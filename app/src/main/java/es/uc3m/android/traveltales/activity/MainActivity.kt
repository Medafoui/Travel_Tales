package es.uc3m.android.traveltales.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : Activity() {
    // Declare the FirebaseAuth and FirebaseFirestore instances
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    // Declare isFirstTime and initialize it to true
    private var isFirstTime = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the FirebaseAuth and FirebaseFirestore instances
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Check if a user is already signed in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is signed in, redirect to Profile activity
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish() // Finish MainActivity so user can't go back to it
        } else {
            // No user is signed in, show the login or profile screen
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}