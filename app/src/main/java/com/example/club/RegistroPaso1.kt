package com.example.club

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class RegistroPaso1 : AppCompatActivity() {
    private lateinit var dbHelper : ClubDatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_paso1)

        dbHelper = ClubDatabaseHelper(this)

        // Capturar objetos locales
        val TITULO = findViewById<TextView>(R.id.txtTituloRegistro)
        val TXT_NOMBRE = findViewById<EditText>(R.id.txtNombre)
        val TXT_APELLIDO = findViewById<EditText>(R.id.txtApellido)
        val TXT_DOCUMENTO = findViewById<EditText>(R.id.txtDocumento)
        val TXT_NACIMIENTO = findViewById<EditText>(R.id.txtNacimiento)
        val TXT_DOMICILIO = findViewById<EditText>(R.id.txtDomicilio)
        val TXT_TELEFONO = findViewById<EditText>(R.id.txtTelefono)
        val TXT_MAIL = findViewById<EditText>(R.id.txtMail)
        val CHK_APTO = findViewById<CheckBox>(R.id.chkApto)
        val BTN_REGISTRAR = findViewById<Button>(R.id.btnRegistrar)
        val BTN_CANCELAR = findViewById<Button>(R.id.btnCancelar)

        // Capturar parámetros externos y asignar al local
        val CLIENTE = intent.getStringExtra("cliente")
        TITULO.text = if (CLIENTE == "s") "Registro de Socio" else "Registro de No Socio"

        // Registrar el cliente enviando parámetros para Socio / No Socio
        BTN_REGISTRAR.setOnClickListener {
            if (TXT_NOMBRE.text.isEmpty() || TXT_APELLIDO.text.isEmpty() || TXT_DOCUMENTO.text.isEmpty()) {
                Toast.makeText(this, "Los campos en rojo son obligatorios", Toast.LENGTH_SHORT)
                    .show()
            } else if (CHK_APTO.isChecked == false) {
                Toast.makeText(this, "Debe presentar Apto Físico", Toast.LENGTH_SHORT).show()
            }
            else {
                val nombre = TXT_NOMBRE.text.toString()
                val apellido = TXT_APELLIDO.text.toString()
                val documento = TXT_DOCUMENTO.text.toString()
                val nacimiento = TXT_NACIMIENTO.text.toString()
                val domicilio = TXT_DOMICILIO.text.toString()
                val telefono = TXT_TELEFONO.text.toString()
                val mail = TXT_MAIL.text.toString()
                val apto = "1"   // Se pasa directamente el valor, ya que la lógica lo controla previamente
                                 // Se mantiene el campo en la base de datos previendo modificaciones futuras

                val clienteExistente = dbHelper.buscarClientePorDocumento(documento)
                if (clienteExistente == "no") {
                    val nuevoCliente = dbHelper.agregarCliente(nombre, apellido, documento, nacimiento,
                        domicilio, telefono, mail, apto)
                    if (nuevoCliente < 0) {
                        Toast.makeText(this, "No se registró el cliente", Toast.LENGTH_SHORT).show()
                        TXT_NOMBRE.text.clear()
                        TXT_APELLIDO.text.clear()
                        TXT_DOCUMENTO.text.clear()
                        TXT_NACIMIENTO.text.clear()
                        TXT_DOMICILIO.text.clear()
                        TXT_TELEFONO.text.clear()
                        TXT_MAIL.text.clear()
                        CHK_APTO.isChecked = false
                        TXT_NOMBRE.requestFocus()
                    } else {
                        val nuevoTipoCliente = if (CLIENTE == "s") dbHelper.agregarTipoCliente("Socios", nuevoCliente.toString())
                        else dbHelper.agregarTipoCliente("NoSocios", nuevoCliente.toString())
                        if (nuevoTipoCliente < 0) {
                            Toast.makeText(this, "No se registró el tipo de cliente", Toast.LENGTH_SHORT).show()
                        } else {
                            val INTENT = Intent(this, RegistroPaso2::class.java)
                            INTENT.putExtra("cliente", CLIENTE)
                            INTENT.putExtra("nroTipoCliente", nuevoTipoCliente.toString())
                            startActivity(INTENT)
                        }
                    }
                }
                else {
                    val socioExistente = dbHelper.buscarClienteEnAnexos("Socios", "id_socio", clienteExistente)
                    if (socioExistente != "no") {
                        Toast.makeText(this, "Socio existente (Nro. $socioExistente)", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        if (CLIENTE == "s") {
                            val nuevoTipoCliente = dbHelper.agregarTipoCliente("Socios", clienteExistente)
                            if (nuevoTipoCliente < 0) {
                                Toast.makeText(this, "No se registró el tipo de cliente", Toast.LENGTH_SHORT).show()
                            } else {
                                val INTENT = Intent(this, RegistroPaso2::class.java)
                                INTENT.putExtra("cliente", CLIENTE)
                                INTENT.putExtra("nroTipoCliente", nuevoTipoCliente.toString())
                                startActivity(INTENT)
                            }
                        }
                        else {
                            val noSocioExistente = dbHelper.buscarClienteEnAnexos("NoSocios", "id_nosocio", clienteExistente)
                            if (noSocioExistente != "no") {
                                Toast.makeText(this, "Cliente existente", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                val nuevoTipoCliente = dbHelper.agregarTipoCliente("NoSocios", clienteExistente)
                                if (nuevoTipoCliente < 0) {
                                    Toast.makeText(this, "No se registró el tipo de cliente", Toast.LENGTH_SHORT).show()
                                } else {
                                    val INTENT = Intent(this, RegistroPaso2::class.java)
                                    INTENT.putExtra("cliente", CLIENTE)
                                    INTENT.putExtra("nroTipoCliente", nuevoTipoCliente.toString())
                                    startActivity(INTENT)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Cancelar el registro y volver al menú clientes con parámetros correspondientes
        BTN_CANCELAR.setOnClickListener {
            val INTENT = Intent(this, MenuClientes::class.java)
            INTENT.putExtra("cliente", CLIENTE)
            startActivity(INTENT)
        }
    }
}