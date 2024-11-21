package com.example.club

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MenuClientes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_clientes)

        // Capturar objetos locales
        val TITULO = findViewById<TextView>(R.id.tituloMenuClientes)
        val BTN_REGISTRAR = findViewById<Button>(R.id.btnRegistrar)
        val BTN_PAGAR = findViewById<Button>(R.id.btnPagarCuota)
        val BTN_CARNET = findViewById<Button>(R.id.btnImprimirCarnet)
        val BTN_VOLVER = findViewById<Button>(R.id.btnVolver)

        //Capturar los parámetros enviados del menú principal y adaptar la pantalla a los mismos
        val CLIENTE = intent.getStringExtra("cliente")
        TITULO.text = if (CLIENTE == "s") "Menú Socios" else "Menú No Socios"
        BTN_CARNET.visibility = if (CLIENTE == "s") View.VISIBLE else View.GONE

        // Ir a registrar con parámetros acordes a Socio / No Socio
        BTN_REGISTRAR.setOnClickListener {
            val INTENT = Intent(this, RegistroPaso1::class.java)
            INTENT.putExtra("cliente", CLIENTE)
            startActivity(INTENT)
        }

        //Ir a BuscarCliente para pagos con parámetros para socio / no socio
        BTN_PAGAR.setOnClickListener {
            val INTENT = Intent(this, BuscarCliente::class.java)
            val ORIGEN = "btnPagar"
            INTENT.putExtra("origen", ORIGEN)
            INTENT.putExtra("cliente", CLIENTE)
            startActivity(INTENT)
        }

        // Ir a BuscarCliente para imprimir carnet
        BTN_CARNET.setOnClickListener {
            val INTENT = Intent(this, BuscarCliente::class.java)
            val ORIGEN = "btnImprimirCarnet"
            INTENT.putExtra("origen", ORIGEN)
            INTENT.putExtra("cliente", CLIENTE)
            startActivity(INTENT)
        }

        // Regresa a la activity MenuPpal
        BTN_VOLVER.setOnClickListener {
            val INTENT = Intent(this, MenuPpal::class.java)
            startActivity(INTENT)
        }
    }
}