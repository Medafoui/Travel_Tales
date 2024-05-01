

package es.uc3m.android.traveltales

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class StatsActivity : Activity() {
    private lateinit var tvCountriesVisited: TextView
    private lateinit var tvContinentsVisited: TextView
    private lateinit var tvTripsDone: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        tvCountriesVisited = findViewById(R.id.tv_countries_visited)
        tvTripsDone = findViewById(R.id.tv_trips_done)

        loadStats()
    }

    private fun loadStats() {
        val db = FirebaseFirestore.getInstance()

        db.collection("trips")
            .get()
            .addOnSuccessListener { result ->
                val tripList = result.map { it.data }

                val countriesVisited = tripList.mapNotNull { it["tripCountry"] as? String }.distinct().size
                val tripsDone = tripList.size

                tvCountriesVisited.text = getString(R.string.countries_visited, countriesVisited)
                tvTripsDone.text = getString(R.string.trips_done, tripsDone)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error getting trips: $e", Toast.LENGTH_SHORT).show()
            }
    }
}





























//
//package es.uc3m.android.traveltales
//
//import android.app.Activity
//import android.os.Bundle
//import android.widget.TextView
//
//class StatsActivity : Activity() {
//    // Activity implementation
//    private lateinit var tvCountriesVisited: TextView
//    private lateinit var tvContinentsVisited: TextView
//    private lateinit var tvTripsDone: TextView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_stats)
//
//        // Initialize views
//        tvCountriesVisited = findViewById(R.id.tv_countries_visited)
//        tvTripsDone = findViewById(R.id.tv_trips_done)
//
//        // Simulate loading stats (In a real app, this data might come from a database or API)
//        loadStats()
//    }
//
//    private fun loadStats() {
//        // These are simulated statistics. Replace with real data fetching logic.
//        val countriesVisited = 24  // Example data
//        val continentsVisited = 5  // Example data
//        val tripsDone = 10
//
//        // Update UI with the fetched data
//        tvCountriesVisited.text = getString(R.string.countries_visited, countriesVisited)
//        tvContinentsVisited.text = getString(R.string.continents_visited, continentsVisited)
//        tvTripsDone.text = getString(R.string.trips_done, tripsDone)
//    }
//}






//package es.uc3m.android.traveltales
//
//import android.app.Activity
//import android.os.Bundle
//import android.widget.TextView
//
//class StatsActivity : Activity() {
//    // Activity implementation
//    private lateinit var tvCountriesVisited: TextView
//    private lateinit var tvContinentsVisited: TextView
//    private lateinit var tvTripsDone: TextView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_stats)
//
//        // Initialize views
//        tvCountriesVisited = findViewById(R.id.tv_countries_visited)
//        tvContinentsVisited = findViewById(R.id.tv_continents_visited)
//        tvTripsDone = findViewById(R.id.tv_trips_done)
//
//        // Simulate loading stats (In a real app, this data might come from a database or API)
//        loadStats()
//    }
//
//    private fun loadStats() {
//        // These are simulated statistics. Replace with real data fetching logic.
//        val countriesVisited = 24  // Example data
//        val continentsVisited = 5  // Example data
//        val tripsDone = 10
//
//        // Update UI with the fetched data
//        tvCountriesVisited.text = getString(R.string.countries_visited, countriesVisited)
//        tvContinentsVisited.text = getString(R.string.continents_visited, continentsVisited)
//        tvTripsDone.text = getString(R.string.trips_done, tripsDone)
//    }
//}