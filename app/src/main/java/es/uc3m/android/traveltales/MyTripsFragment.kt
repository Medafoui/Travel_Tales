import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        FirebaseFirestore.getInstance().collection("trips")
            .get()
            .addOnSuccessListener { result ->
                val tripList = result.map { it.data }
                tripAdapter = TripAdapter(tripList)
                recyclerViewTrips.adapter = tripAdapter
            }

        return view
    }
}





//package es.uc3m.android.traveltales
//
//import BaseFragment
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//
//class MyTripsFragment : BaseFragment() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_my_trips, container, false)
//    }
//}