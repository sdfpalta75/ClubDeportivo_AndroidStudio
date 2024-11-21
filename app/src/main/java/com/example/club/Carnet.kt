package com.example.club

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Carnet : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_carnet)

        val BTN_FIN = findViewById<Button>(R.id.btnFinalizar)

        BTN_FIN.setOnClickListener {
            val INTENT = Intent(this, MenuClientes::class.java)
            val CLIENTE = "s"
            INTENT.putExtra("cliente", CLIENTE)
            startActivity(INTENT)
        }
    }
}