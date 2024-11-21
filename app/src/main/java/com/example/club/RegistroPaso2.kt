package com.example.club

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class RegistroPaso2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_paso2)

        //Capturar objetos locales
        val TITULO = findViewById<TextView>(R.id.txtTituloRegistro)
        val CARTEL_EXITO = findViewById<TextView>(R.id.txtCartelExito)
        val BTN_PAGAR = findViewById<Button>(R.id.btnPagarCuota)
        val BTN_FIN = findViewById<Button>(R.id.btnFinalizar)

        // Capturar parámetros externos y asignar al local
        val CLIENTE = intent.getStringExtra("cliente")
        val NRO_TIPO_CLIENTE = intent.getStringExtra("nroTipoCliente")
        TITULO.text = if (CLIENTE == "s") "Registro de Socio" else "Registro de No Socio"

        // Modificar cartel de éxito
        CARTEL_EXITO.text = if (CLIENTE == "s") "Registro exitoso!\n\nSocio N° $NRO_TIPO_CLIENTE" else "Registro exitoso!"

        // Ir a pagar cuota con parámetros para Socio / No Socio
        BTN_PAGAR.setOnClickListener {
            val INTENT = Intent(this, Pagos1::class.java)
            val ORIGEN = "registro2"
            INTENT.putExtra("cliente", CLIENTE)
            INTENT.putExtra("nroTipoCliente", NRO_TIPO_CLIENTE)
            INTENT.putExtra("origen", ORIGEN)
            startActivity(INTENT)
        }

        // Finalizar volviendo al menu Clientes con parámetros para Socio / No Socio
        BTN_FIN.setOnClickListener {
            val INTENT = Intent(this, MenuClientes::class.java)
            INTENT.putExtra("cliente", CLIENTE)
            startActivity(INTENT)
        }
    }
}