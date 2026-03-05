package mx.itson.practica5

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SeatsSelection : AppCompatActivity() {

    private var selectedSeat: String = ""
    private lateinit var movieName: String
    private lateinit var cliente: String
    private var movieImage: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seats_selection)
        // Obtener datos de la película
        val title: TextView = findViewById(R.id.titleSeats)
        val bundle = intent.extras

        if (bundle != null) {
            movieName = bundle.getString("name") ?: ""
            movieImage = bundle.getInt("id", -1)
            cliente = bundle.getString("cliente_nombre") ?: ""
            title.text = movieName
            }
        // Configurar RadioGroups
        val row1: RadioGroup = findViewById(R.id.row1)
        val row2: RadioGroup = findViewById(R.id.row2)
        val row3: RadioGroup = findViewById(R.id.row3)
        val row4: RadioGroup = findViewById(R.id.row4)

        // Listener para los asientos
        val seatSelectionListener = RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (checkedId > -1) {
                val radioButton = findViewById<RadioButton>(checkedId)
                selectedSeat = radioButton.text.toString()

                // Desmarcar otros grupos
                when (group.id) {
                    R.id.row1 -> { row2.clearCheck(); row3.clearCheck(); row4.clearCheck() }
                    R.id.row2 -> { row1.clearCheck(); row3.clearCheck(); row4.clearCheck() }
                    R.id.row3 -> { row1.clearCheck(); row2.clearCheck(); row4.clearCheck() }
                    R.id.row4 -> { row1.clearCheck(); row2.clearCheck(); row3.clearCheck() }
                }
            }
        }
    }
}