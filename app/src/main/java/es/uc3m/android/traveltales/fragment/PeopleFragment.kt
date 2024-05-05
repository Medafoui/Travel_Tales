package es.uc3m.android.traveltales.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import es.uc3m.android.traveltales.R
import es.uc3m.android.traveltales.activity.PeopleProfileActivity
import es.uc3m.android.traveltales.adapter.PeopleAdapter
import es.uc3m.android.traveltales.data.PersonData

class PeopleFragment : BaseFragment() {
    private lateinit var peopleAdapter: PeopleAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_people, container, false)

        val recyclerViewPeople = view.findViewById<RecyclerView>(R.id.people_rv)
        recyclerViewPeople.layoutManager = LinearLayoutManager(context)

        peopleAdapter = PeopleAdapter(emptyList(), object : PeopleAdapter.OnPersonClickListener {
            override fun onPersonClick(position: Int) {
                val intent = Intent(activity, PeopleProfileActivity::class.java)
                intent.putExtra("username",peopleAdapter.peopleList[position].username)
                intent.putExtra("email",peopleAdapter.peopleList[position].email)
                intent.putExtra("userId",peopleAdapter.peopleList[position].userId)
                startActivity(intent)
            }
        })

        recyclerViewPeople.adapter = peopleAdapter
        val currentid = FirebaseAuth.getInstance().currentUser?.uid

        FirebaseFirestore.getInstance().collection("users")
            .get()
            .addOnSuccessListener { result ->
                val peopleList = mutableListOf<PersonData>()

                for (document in result) {
                    val email = document.getString("email")
                    val username = document.getString("username")
                    val userId = document.id

                    if (email != null && userId != currentid && username != null) {
                        peopleList.add(PersonData(username, email, userId))
                    }
                }

                // Update the adapter with the fetched data
                peopleAdapter.peopleList = peopleList
                // Notify the adapter that the data set has changed
                peopleAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error getting people: $e", Toast.LENGTH_SHORT).show()
            }
        return view
    }
}