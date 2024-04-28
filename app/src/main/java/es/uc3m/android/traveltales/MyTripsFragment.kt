import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import es.uc3m.android.traveltales.R

class MyTripsFragment : BaseFragment() {

    private lateinit var tripAdapter: TripAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_trips, container, false)

        val recyclerViewTrips = view.findViewById<RecyclerView>(R.id.recyclerViewTrips)
        recyclerViewTrips.layoutManager = LinearLayoutManager(context)

        // Initialize the adapter with an empty list
        tripAdapter = TripAdapter(emptyList())
        recyclerViewTrips.adapter = tripAdapter

        FirebaseFirestore.getInstance().collection("trips")
            .get()
            .addOnSuccessListener { result ->
                val tripList = result.map { it.data }
                // Update the adapter with the fetched data
                tripAdapter.tripList = tripList
                // Notify the adapter that the data set has changed
                tripAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error getting trips: $e", Toast.LENGTH_SHORT).show()
            }

        return view
    }
}












