package es.uc3m.android.traveltales
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : Activity() {
    // Declare the FirebaseFirestore instance
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the FirebaseFirestore instance
        db = FirebaseFirestore.getInstance()

        // Now you can use 'db' to interact with your Firestore database
        val user: MutableMap<String, Any> = HashMap()
        user["name"] = "John Doe"
        user["email"] = "johndoe@example.com"

        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                println("DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { _ ->
                println("Error adding document")
            }

        val intent = Intent(this, Profile::class.java)
        startActivity(intent)
    }
}


