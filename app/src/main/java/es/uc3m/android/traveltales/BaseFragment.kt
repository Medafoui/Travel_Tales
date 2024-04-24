import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import es.uc3m.android.traveltales.LoginActivity


// Base class for all fragments in the app to remember user when switching between fragments
open class BaseFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            // No user is signed in, redirect to LoginActivity
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}