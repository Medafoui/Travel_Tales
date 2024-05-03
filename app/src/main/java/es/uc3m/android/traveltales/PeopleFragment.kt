package es.uc3m.android.traveltales

import BaseFragment
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
                startActivity(intent)
            }
        })
        //peopleAdapter = PeopleAdapter(emptyList())
        recyclerViewPeople.adapter = peopleAdapter

        val currentEmail = FirebaseAuth.getInstance().currentUser?.email

        FirebaseFirestore.getInstance().collection("users")
            .get()
            .addOnSuccessListener { result ->
                val peopleList = mutableListOf<Person>()

                for (document in result) {
                    val email = document.getString("email")
                    val username = document.getString("username")

                    if (email != null && email != currentEmail && username != null) {
                        peopleList.add(Person(username, email))
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