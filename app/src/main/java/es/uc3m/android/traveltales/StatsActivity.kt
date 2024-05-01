package es.uc3m.android.traveltales

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat

class StatsActivity : Activity() {
    private lateinit var tvCountriesVisited: TextView
    private lateinit var tvAverageTripDuration: TextView
    private lateinit var tvLongestTrip: TextView
    private lateinit var tvTripsDone: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)



        tvCountriesVisited = findViewById(R.id.tv_countries_visited)
        tvAverageTripDuration = findViewById(R.id.tv_average_trip_duration)
        tvLongestTrip = findViewById(R.id.tv_longest_trip)
        tvTripsDone = findViewById(R.id.tv_trips_done)

        loadStats()
    }

    private fun loadStats() {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()


        db.collection("trips")
            .whereEqualTo("userId", auth.currentUser?.uid)  // Filter by the user's ID
            .get()
            .addOnSuccessListener { result ->
                val tripList = result.map { it.data }

                val countriesVisited = tripList.mapNotNull { it["tripCountry"] as? String }.distinct().size
                val tripsDone = tripList.size

                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", java.util.Locale("en", "US"))
                // Calculate the duration in days for each trip

                val tripDurationsInDays = tripList.mapNotNull {
                    val endDateString = it["tripEndDate"] as? String
                    val startDateString = it["tripStartDate"] as? String
                    if (endDateString != null && startDateString != null) {
                        val endDate = simpleDateFormat.parse(endDateString)
                        val startDate = simpleDateFormat.parse(startDateString)
                        if (endDate != null && startDate != null) {
                            val diffInMillies = endDate.time - startDate.time
                            val diffInDays = diffInMillies.toDouble() / (24 * 60 * 60 * 1000)
                            diffInDays
                        } else {
                            null
                        }
                    } else {
                        null
                    }
                }

                // Calculate the average and longest trip duration in days
                val averageTripDuration = tripDurationsInDays.average()
                val longestTripDuration = tripDurationsInDays.maxOrNull()

                tvCountriesVisited.text = getString(R.string.countries_visited, countriesVisited)
                tvTripsDone.text = getString(R.string.trips_done, tripsDone)

                // Update the text of the corresponding TextViews
                tvAverageTripDuration.text = getString(R.string.average_trip_duration, averageTripDuration.toInt())
                tvLongestTrip.text = getString(R.string.longest_trip, longestTripDuration?.toInt() ?: 0)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error getting trips: $e", Toast.LENGTH_SHORT).show()
            }
    }
}
