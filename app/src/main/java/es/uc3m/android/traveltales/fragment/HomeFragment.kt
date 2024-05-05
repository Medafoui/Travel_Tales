package es.uc3m.android.traveltales.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import es.uc3m.android.traveltales.R
import es.uc3m.android.traveltales.activity.LoginActivity
import es.uc3m.android.traveltales.activity.SignUpActivity

class HomeFragment : BaseFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the login button by its ID and set a click listener
        view.findViewById<Button>(R.id.buttonLogin).setOnClickListener {
            // Start LoginActivity when the login button is clicked
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        // Find the signup button by its ID and set a click listener
        view.findViewById<Button>(R.id.buttonSignUp).setOnClickListener {
            // Start SignUpActivity when the signup button is clicked
            val intent = Intent(activity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}