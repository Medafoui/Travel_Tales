package es.uc3m.android.traveltales.activity

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import es.uc3m.android.traveltales.R
import es.uc3m.android.traveltales.adapter.TripAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var tripAdapter: TripAdapter
    private lateinit var tripCountryList: ArrayList<String>
    private lateinit var tripCityList: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(gm: GoogleMap) {
        tripAdapter = TripAdapter(emptyList())
        tripCityList = ArrayList()
        tripCountryList = ArrayList()

        val auth = FirebaseAuth.getInstance()

        FirebaseFirestore.getInstance().collection("trips")
            .whereEqualTo("userId", auth.currentUser?.uid)  // Filter by the user's ID
            .get()
            .addOnSuccessListener { result ->
                val tripList = result.map { it.data }
                // Update the adapter with the fetched data
                tripAdapter.tripList = tripList
                // Notify the adapter that the data set has changed
                tripAdapter.notifyDataSetChanged()

                for (trip in tripList) {
                    val city = trip["tripCity"] as String
                    mMap = gm
                    val geocoder = Geocoder(this)
                    GlobalScope.launch(Dispatchers.IO) {
                        try {
                            val addressList = geocoder.getFromLocationName(city, 1)
                            withContext(Dispatchers.Main) {
                                handleAddressList(addressList, city)
                            }
                        } catch (e: IOException) {
                            withContext(Dispatchers.Main) {
                                Log.e("Location", "$city is not found.")
                            }
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error getting trips: $e", Toast.LENGTH_SHORT).show()
        }
    }
    private fun handleAddressList(addressList: List<Address>?, city: String) {
        if (!addressList.isNullOrEmpty()) {
            val location = addressList[0]
            val cityLocation =
                com.google.android.gms.maps.model.LatLng(location.latitude, location.longitude)
            mMap.addMarker(MarkerOptions().position(cityLocation).title("$city"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(cityLocation))
            Log.d("Location", "Latitude: ${location.latitude}, Longitude: ${location.longitude}")
        } else {
            Log.e("Location", "Place is not found.")
        }
    }
}