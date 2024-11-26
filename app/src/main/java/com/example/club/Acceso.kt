package com.example.club

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Acceso : AppCompatActivity() {
    private lateinit var dbHelper : ClubDatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_acceso)

        dbHelper = ClubDatabaseHelper(this)

        // Capturar elementos locales
        val TXT_USUARIO = findViewById<EditText>(R.id.txtUsuario)
        val TXT_CLAVE = findViewById<EditText>(R.id.txtClave)
        val TXT_REGISTRAR_USUARIO = findViewById<TextView>(R.id.txtRegistrarUsuario)
        val TXT_CAMBIAR_CLAVE = findViewById<TextView>(R.id.txtCambiarClave)
        val BTN_ACCEDER = findViewById<Button>(R.id.btnAcceder)
        val BTN_SALIR = findViewById<Button>(R.id.btnSalir)

        if (dbHelper.contarUsuariosRegistrados() == 0) {
            TXT_USUARIO.isEnabled = false
            TXT_CLAVE.isEnabled = false
            BTN_ACCEDER.isEnabled = false
            TXT_CAMBIAR_CLAVE.isEnabled = false
        }

        // Registrar Usuario
        TXT_REGISTRAR_USUARIO.setOnClickListener {
            val INTENT = Intent(this, RegistroDatosUsuario::class.java)
            INTENT.putExtra("origen", "registrarUsuario")
            startActivity(INTENT)
        }

        // Cambiar contraseña
        TXT_CAMBIAR_CLAVE.setOnClickListener {
            val INTENT = Intent(this, RegistroDatosUsuario::class.java)
            INTENT.putExtra("origen", "cambiarClave")
            startActivity(INTENT)
            //Toast.makeText(this, "Funcionalidad en desarrollo", Toast.LENGTH_SHORT).show()
        }

        // Acceder a la activity MenuPpal
        BTN_ACCEDER.setOnClickListener {
            if (TXT_USUARIO.text.isEmpty() || TXT_CLAVE.text.isEmpty()) {
                Toast.makeText(this, "Los campos en rojo son obligatorios", Toast.LENGTH_SHORT).show()
            }
            else {
                val usuario = TXT_USUARIO.text.toString()
                val clave = TXT_CLAVE.text.toString()

                val validacion = dbHelper.validarAcceso(usuario, clave)
                if (validacion) {
                    val INTENT = Intent(this, MenuPpal::class.java)
                    startActivity(INTENT)
                }
                else {
                    TXT_USUARIO.text.clear()
                    TXT_CLAVE.text.clear()
                    Toast.makeText(this, "Usuario o Contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Cerrar la app
        BTN_SALIR.setOnClickListener {
            finishAffinity()
        }
    }
}