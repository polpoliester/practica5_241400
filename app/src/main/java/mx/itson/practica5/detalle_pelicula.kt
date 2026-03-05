package mx.itson.practica5

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class detalle_pelicula : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_pelicula)

        val iv_pelicula_image: ImageView = findViewById(R.id.iv_pelicula_imagen)
        val tv_nombre_pelicula: TextView = findViewById(R.id.tv_nombre_pelicula)
        val tv_pelicula_desc: TextView = findViewById(R.id.tv_pelicula_desc)
        val btnBuyTickets: Button = findViewById(R.id.buyTickets)
        val tvSeatLeft: TextView = findViewById(R.id.seatLeft)

        tv_pelicula_desc.movementMethod = ScrollingMovementMethod()

        var ns = 0
        var id = -1
        var title = ""

        val bundle = intent.extras
        if (bundle != null) {
            // Asignar los datos a la vista
            iv_pelicula_image.setImageResource(bundle.getInt("header"))
            tv_nombre_pelicula.text = bundle.getString("titulo")
            tv_pelicula_desc.text = bundle.getString("sinopsis")

            // Obtener variables para el botón
            ns = bundle.getInt("numberSeats")
            title = bundle.getString("titulo") ?: ""
            id = bundle.getInt("pos")

            tvSeatLeft.text = "$ns seats available"
        }

        // Lógica del botón de compra
        if (ns == 0) {
            btnBuyTickets.isEnabled = false // Deshabilita el botón si no hay asientos
        } else {
            btnBuyTickets.isEnabled = true
            btnBuyTickets.setOnClickListener {
                val intent = Intent(this, SeatsSelection::class.java)
                intent.putExtra("id", id)
                intent.putExtra("name", title)
                startActivity(intent)
            }
        }
    }
}