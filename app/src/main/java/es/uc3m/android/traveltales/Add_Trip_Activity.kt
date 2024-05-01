package es.uc3m.android.traveltales

import android.app.Activity
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.Calendar

class Add_Trip_Activity: Activity() {

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    private var selectedImageUri: Uri? = null
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_trip)

        // Initialize Firebase Auth
        var auth = FirebaseAuth.getInstance()


        val tripCity = findViewById<EditText>(R.id.editTextCity)
        val tripCountry = findViewById<EditText>(R.id.editTextCountry)
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

            // Get an instance of Firebase Storage
            val storageRef = Firebase.storage.reference
            val imageRef = storageRef.child("images/${selectedImageUri?.lastPathSegment}")

            val uploadTask = imageRef.putFile(selectedImageUri!!)
            uploadTask.continueWithTask { task: Task<UploadTask.TaskSnapshot> ->
                if (!task.isSuccessful) {
                    task.exception?.let { exception ->
                        throw exception
                    }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { task: Task<Uri> ->
                if (task.isSuccessful) {
                    val downloadUri = task.result

                    // Create a new Trip object with keys and values using HashMap
                    val trip = hashMapOf(
                        "tripCity" to tripCity.text.toString(),
                        "tripCountry" to tripCountry.text.toString(),
                        "tripName" to tripName.text.toString(),
                        "tripDescription" to tripDescription.text.toString(),
                        "tripStartDate" to tripStartDate.text.toString(),
                        "tripEndDate" to tripEndDate.text.toString(),
                        "tripImageUri" to downloadUri.toString(), // save the download URL
                        "userId" to auth.currentUser?.uid  // Add the user's ID to the trip

                    )

                    // Get an instance of FirebaseFirestore
                    val db = FirebaseFirestore.getInstance()

                    // Save the trip information to Firestore
                    db.collection("trips")
                        .add(trip)
                        .addOnSuccessListener { documentReference ->
                            Toast.makeText(this, "Trip succesfully saved!", Toast.LENGTH_SHORT).show()



                            // Fetch weather data and display notification
                            CoroutineScope(Dispatchers.IO).launch {
                                val city = tripCity.text.toString()
                                val (weather, temperature) = fetchWeatherData(this@Add_Trip_Activity,client, city)
                                withContext(Dispatchers.Main) {
                                    displayWeatherNotification(this@Add_Trip_Activity,city, weather, temperature)
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error adding trip: $e", Toast.LENGTH_SHORT).show()
                        }
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
                selectedImageUri = data.data
                val imageViewPhoto = findViewById<ImageView>(R.id.imageViewPhoto)
                imageViewPhoto.setImageURI(selectedImageUri)
            }
        }
    }


    suspend fun fetchWeatherData(context:Context, client: OkHttpClient, city: String): Pair<String, Double> {
        val open_weather_map_api_key = context.getString(R.string.open_weather_map_api_key)
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=${open_weather_map_api_key}"
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()
        val weatherData = Gson().fromJson(responseBody, WeatherData::class.java)
        val weatherDescription = weatherData.weather[0].description
        val temperature = weatherData.main.temp
        return Pair(weatherDescription, temperature)
    }

fun displayWeatherNotification(context: Context, city: String, weather: String, temperature: Double) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val notificationChannelId = "weather_channel"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            notificationChannelId,
            "Weather Notifications",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(notificationChannel)
    }
    val formattedTemperature = (temperature - 273.15).toInt()
    val notification = NotificationCompat.Builder(context, notificationChannelId)
        .setContentTitle("Weather in $city")
        .setContentText("Current weather: $weather and $formattedTemperature°C")
        .setSmallIcon(R.drawable.icon_weather) // Use the actual drawable here
        .build()
    notificationManager.notify(0, notification)
}


