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
import es.uc3m.android.traveltales.adapter.FriendTripAdapter
import es.uc3m.android.traveltales.adapter.PeopleAdapter
import es.uc3m.android.traveltales.data.FriendTripData
import es.uc3m.android.traveltales.data.PersonData

class ExploreFragment:  BaseFragment() {
    private lateinit var peopleAdapter: PeopleAdapter
    private lateinit var friendTripAdapter: FriendTripAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_explore, container, false)

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

        val recyclerViewTrip = view.findViewById<RecyclerView>(R.id.friends_rv)
        recyclerViewTrip.layoutManager = LinearLayoutManager(context)
        friendTripAdapter = FriendTripAdapter(emptyList())
        recyclerViewTrip.adapter = friendTripAdapter

        val currentid = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        val userList = mutableListOf<PersonData>()
        val friendList = mutableListOf<PersonData>()
        val tripList = mutableListOf<FriendTripData>()

        db.collection("users")
            .get()
            .addOnSuccessListener{ result ->
                for (r in result) {
                    val email = r.getString("email").toString()
                    val username = r.getString("username").toString()
                    val userId = r.id
                    userList.add(PersonData(username, email, userId))
                }

                db.collection("users")
                    .document(currentid!!)
                    .get()
                    .addOnSuccessListener { document ->
                        val followingList = document.get("following") as? List<String> ?: emptyList()
                        for(following in followingList) {
                            for (user in userList) {
                                if (user.userId == following){
                                    friendList.add(user)
                                }
                            }
                        }
                        // Update the adapter with the fetched data
                        peopleAdapter.peopleList = friendList
                        // Notify the adapter that the data set has changed
                        peopleAdapter.notifyDataSetChanged()

                        for (friend in friendList) {
                            db.collection("trips")
                                .whereEqualTo("userId", friend.userId)
                                .get()
                                .addOnSuccessListener { result ->
                                    val resultList = result.map { it.data }
                                    for (r in resultList) {
                                        tripList.add(FriendTripData(friend.username, friend.userId,r["tripName"].toString(), r["tripImageUri"].toString()))
                                    }
                                    friendTripAdapter.tripList = tripList
                                    friendTripAdapter.notifyDataSetChanged()

                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Error getting trips list: $e", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Error getting following list: $e", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error getting users list: $e", Toast.LENGTH_SHORT).show()
            }

        return view
    }
}