package com.example.club

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Pagos2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pagos2)

        // Capturar objetos locales
        val TV_RECORDATORIO = findViewById<TextView>(R.id.tvRecordatorio)
        val BTN_FINALIZAR = findViewById<Button>(R.id.btnFinalizar)

        // Capturar argumentos externos
        val CLIENTE = intent.getStringExtra("cliente")

        TV_RECORDATORIO.text = if (CLIENTE == "s") "Recordar al socio que el período vence al mes de efectuado el pago de cuota."
                               else "Recordar al cliente que el pago de actividad es para el uso en el día de la fecha."

        // Finalizar y al menú clientes con parámetros para socio / no socio
        BTN_FINALIZAR.setOnClickListener {
            val INTENT = Intent(this, MenuClientes::class.java)
            INTENT.putExtra("cliente", CLIENTE)
            startActivity(INTENT)
        }
    }
}