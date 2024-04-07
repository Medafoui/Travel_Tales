package es.uc3m.android.traveltales

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString("username")
            Log.d("ProfileFragment", "Username: $username")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Display the welcome message
        Toast.makeText(context, "Welcome, $username!", Toast.LENGTH_SHORT).show()

        // Find the TextView by its ID and set the username
        val usernameTextView = view.findViewById<TextView>(R.id.name)
        Log.d("ProfileFragment", "Username: $username")
        usernameTextView.text = username

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the add trip button by its ID and set a click listener
        view.findViewById<Button>(R.id.btn_add).setOnClickListener {
            // Start Add_Trip_Activity when the button is clicked
            val intent = Intent(activity, Add_Trip_Activity::class.java)
            startActivity(intent)
        }

        // Find the travel statistics button by its ID and set a click listener
        view.findViewById<Button>(R.id.btn_stats).setOnClickListener {
            // Start StatsActivity when the button is clicked
            val intent = Intent(activity, StatsActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        fun newInstance(username: String) = ProfileFragment().apply {
            arguments = Bundle().apply {
                putString("username", username)
            }
        }
    }
}