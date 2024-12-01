package com.example.club

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MenuPpal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_ppal)

        // Capturar objetos de la activity
        val BTN_SOCIOS = findViewById<Button>(R.id.btnSocios)
        val BTN_NOSOCIOS = findViewById<Button>(R.id.btnNoSocios)
        val BTN_VENCIMIENTOS = findViewById<Button>(R.id.btnVencimientos)
        val BTN_SALIR = findViewById<Button>(R.id.btnSalir)

        // Ir a la activity MenuClientes con parámetros para socios
        BTN_SOCIOS.setOnClickListener {
            val INTENT = Intent(this, MenuClientes::class.java)
            val CLIENTE = "s"
            INTENT.putExtra("cliente", CLIENTE)
            startActivity(INTENT)
        }

        // Ir a la activity MenuClientes con parámetros para No Socios
        BTN_NOSOCIOS.setOnClickListener {
            val INTENT = Intent(this, MenuClientes::class.java)
            val CLIENTE = "ns"
            INTENT.putExtra("cliente", CLIENTE)
            startActivity(INTENT)
        }

        // Ir a la activity Vencimientos_ListView
        BTN_VENCIMIENTOS.setOnClickListener {
            val INTENT = Intent(this, Vencimientos::class.java)
            startActivity(INTENT)
        }

        // Cerrar la app
        BTN_SALIR.setOnClickListener {
            finishAffinity()
        }
    }
}