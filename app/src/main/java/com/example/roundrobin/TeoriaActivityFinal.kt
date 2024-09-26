package com.example.roundrobin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TeoriaActivityFinal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_teoria_final)

        val btnSig: Button = findViewById(R.id.btnSiguiente);
        val btnAtras: ImageButton = findViewById(R.id.btnAtras);

        btnSig.setOnClickListener {

            val intent = Intent(this, FuncionalesActivity::class.java)
            startActivity(intent)
        }
        btnAtras.setOnClickListener {

            val intent = Intent(this, TeoriaActivity3::class.java)
            startActivity(intent)
        }

    }
}