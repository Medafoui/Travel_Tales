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
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : Activity() {
    // Declare the FirebaseAuth instance
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize the FirebaseAuth instance
        auth = Firebase.auth

        val emailEditText = findViewById<EditText>(R.id.login_email)
        val passwordEditText = findViewById<EditText>(R.id.login_password)
        val loginButton = findViewById<Button>(R.id.login_button_login)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Check if the email and password fields are not empty
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email or password is incorrect", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        if (user != null) {
                            val db = FirebaseFirestore.getInstance()
                            val docRef = db.collection("users").document(user.uid)
                            docRef.get()
                                .addOnSuccessListener { document ->
                                    if (document != null) {
                                        val username = document.getString("username")
                                        if (username != null) {
                                            Log.d("LoginActivity", "Username: $username")

                                            // Store the username in SharedPreferences
                                            val sharedPref = getSharedPreferences("MyPref", android.content.Context.MODE_PRIVATE)
                                            with (sharedPref.edit()) {
                                                putString("username", username)
                                                apply()
                                            }

                                            // Redirect to Profile activity and pass the username
                                            val intent = Intent(this, Profile::class.java)
                                            intent.putExtra("username", username) // Pass the username to the Profile activity
                                            startActivity(intent)
                                            finish() // close LoginActivity
                                        } else {
                                            Log.d("LoginActivity", "No such field 'username'")
                                        }
                                    } else {
                                        Log.d("LoginActivity", "No such document")
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.d("LoginActivity", "get failed with ", exception)
                                }
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the error
                    Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                }
        }
    }
}

