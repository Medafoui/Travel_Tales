package es.uc3m.android.traveltales

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class StatsActivity : Activity() {
    // Activity implementation
    private lateinit var tvCountriesVisited: TextView
    private lateinit var tvContinentsVisited: TextView
    private lateinit var tvWorldWonders: TextView
    private lateinit var tvTripsDone: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        // Initialize views
        tvCountriesVisited = findViewById(R.id.tv_countries_visited)
        tvContinentsVisited = findViewById(R.id.tv_continents_visited)
        tvWorldWonders = findViewById(R.id.tv_world_wonders)
        tvTripsDone = findViewById(R.id.tv_trips_done)

        // Simulate loading stats (In a real app, this data might come from a database or API)
        loadStats()
    }

    private fun loadStats() {
        // These are simulated statistics. Replace with real data fetching logic.
        val countriesVisited = 24  // Example data
        val continentsVisited = 5  // Example data
        val worldWonders = 3
        val tripsDone = 40         // Example data

        // Update UI with the fetched data
        tvCountriesVisited.text = getString(R.string.countries_visited, countriesVisited)
        tvContinentsVisited.text = getString(R.string.continents_visited, continentsVisited)
        tvWorldWonders.text = getString(R.string.world_wonders, worldWonders)
        tvTripsDone.text = getString(R.string.trips_done, tripsDone)
    }
}