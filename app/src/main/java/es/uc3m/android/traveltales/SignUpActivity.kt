package es.uc3m.android.traveltales

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth

class SignUpActivity : Activity() {
    // Declare the FirebaseAuth instance
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize the FirebaseAuth instance
        auth = Firebase.auth

        findViewById<EditText>(R.id.signup_username)
        val emailEditText = findViewById<EditText>(R.id.signup_email)
        val passwordEditText = findViewById<EditText>(R.id.signup_password)
        findViewById<EditText>(R.id.signup_confirm_password)
        val signUpButton = findViewById<Button>(R.id.signup_button)

        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val username = findViewById<EditText>(R.id.signup_username).text.toString()
            Log.d("SignUpActivity", "Username: $username")

            if (password.length < 6) {
                Toast.makeText(this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser

                        // Show success message
                        Toast.makeText(this, "Sign up successful.", Toast.LENGTH_SHORT).show()

                        // Redirect to Profile activity
                        val intent = Intent(this, Profile::class.java)
                        intent.putExtra("username", username) // Pass the username to the Profile activity
                        startActivity(intent)
                        finish() // Finish SignUpActivity so user can't go back to it
                    } else {
                        // If sign in fails, display a message to the user.
                        if (task.exception is FirebaseAuthUserCollisionException) {
                            // Show message to the user that the email is already in use
                            Toast.makeText(this, "The email address is already in use by another account.", Toast.LENGTH_SHORT).show()
                        } else {
                            // Handle other exceptions
                            Toast.makeText(this, "Sign up failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
    }}