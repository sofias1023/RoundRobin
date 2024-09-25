package com.example.roundrobin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val btn: Button = findViewById(R.id.btnSiguiente);

        btn.setOnClickListener {

            val intent = Intent(this, TeoriaActivity1::class.java)
            startActivity(intent)
        }
    }
}