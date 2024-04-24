package es.uc3m.android.traveltales

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class Add_Trip_Activity: Activity() {

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_trip)

        val tripName = findViewById<EditText>(R.id.editTextTripName)
        val tripDescription = findViewById<EditText>(R.id.editTextDescription)
        val tripStartDate = findViewById<EditText>(R.id.editTextStartDate)
        val tripEndDate = findViewById<EditText>(R.id.editTextEndDate)
        val imageViewPhoto = findViewById<ImageView>(R.id.imageViewPhoto)

        // Set up DatePickerDialog for start date
        tripStartDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    tripStartDate.setText("$dayOfMonth/${month + 1}/$year")
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        // Set up DatePickerDialog for end date
        tripEndDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    tripEndDate.setText("$dayOfMonth/${month + 1}/$year")
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        // Handle photo upload
        val buttonUploadPhoto = findViewById<Button>(R.id.buttonUploadPhoto)
        buttonUploadPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Save button
        val buttonSave = findViewById<Button>(R.id.buttonSave)
        buttonSave.setOnClickListener {
            // Create a new Trip object
            val trip = hashMapOf(
                "tripName" to tripName.text.toString(),
                "tripDescription" to tripDescription.text.toString(),
                "tripStartDate" to tripStartDate.text.toString(),
                "tripEndDate" to tripEndDate.text.toString()
            )

            // Get an instance of FirebaseFirestore
            val db = FirebaseFirestore.getInstance()

            // Save the trip information to Firestore
            db.collection("trips")
                .add(trip)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this, "Trip succesfully saved!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error adding trip: $e", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val selectedImageUri = data.data
            val imageViewPhoto = findViewById<ImageView>(R.id.imageViewPhoto)
            imageViewPhoto.setImageURI(selectedImageUri)
        }
    }
}












