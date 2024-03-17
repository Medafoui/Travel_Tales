package es.uc3m.android.traveltales
import android.app.Activity
import android.view.View

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import es.uc3m.android.traveltales.ui.theme.TravelTalesTheme
import android.content.Intent
import android.widget.Button

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        // Find the Sign Up button by its ID
        findViewById<Button>(R.id.buttonSignUp).setOnClickListener {
            // Create an Intent to start SignUpActivity
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)

        // Find the Login button by its ID
        findViewById<Button>(R.id.buttonLogin).setOnClickListener {
            // Create an Intent to start SignUpActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)





    }
}}}







//
//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//            text = "Hello $name!",
//            modifier = modifier
//    )
//}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    TravelTalesTheme {
//        Greeting("Android")
//    }
//}}
