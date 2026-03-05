package mx.itson.practica5

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button : Button = findViewById(R.id.botonP)

        button.setOnClickListener() {
            var intent: Intent = Intent(this, catalogo::class.java)
            startActivity(intent)
        }
    }
}