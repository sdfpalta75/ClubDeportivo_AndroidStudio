package com.example.club

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate

class BuscarCliente : AppCompatActivity() {
    private lateinit var dbHelper : ClubDatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_buscar_cliente)

        dbHelper = ClubDatabaseHelper(this)

        // Capturar objetos locales
        val TXT_TITULO = findViewById<TextView>(R.id.txtTituloBuscarCliente)
        val TXT_CLIENTE = findViewById<EditText>(R.id.txtNroCliente)
        val BTN_SIGUIENTE = findViewById<Button>(R.id.btnSiguiente)
        val BTN_VOLVER = findViewById<Button>(R.id.btnVolver)

        // Capturar argumentos y adaptar vista
        val CLIENTE = intent.getStringExtra("cliente")
        val ORIGEN = intent.getStringExtra("origen")
        TXT_TITULO.text = if (ORIGEN == "btnPagar") "Pagos" else "Impresión de Carnet"
        TXT_CLIENTE.hint = if (CLIENTE == "s") "Número de Socio" else "Número de Documento"
        BTN_SIGUIENTE.text = if (ORIGEN == "btnPagar") "Siguiente" else "Imprimir"

        // Ir a medios de pago o imprimir carnet, según origen
        BTN_SIGUIENTE.setOnClickListener {
            fun acceder(origen: String, tipoCliente: String, idCliente: String) {
                if (origen == "btnPagar") {
                    val INTENT = Intent(this, Pagos1::class.java)
                    INTENT.putExtra("cliente", tipoCliente)
                    INTENT.putExtra("nroTipoCliente", idCliente)
                    INTENT.putExtra("origen", "pagos1")
                    startActivity(INTENT)
                }
                else {
                    val INTENT = Intent(this, Carnet::class.java)
                    startActivity(INTENT)
                }
            }

            if (TXT_CLIENTE.text.isEmpty()) {
                Toast.makeText(this, "Debe ingresar el dato requerido", Toast.LENGTH_SHORT).show()
            }
            else {
                val datoIngresado = TXT_CLIENTE.text.toString()
                if (CLIENTE == "s") {
                    val resultadoBusqueda = dbHelper.buscarIdClientePorIdTipo("Socios", "id_socio", datoIngresado)
                    if (resultadoBusqueda != "no") {
                        val nroCliente = dbHelper.buscarIdClientePorIdTipo("Socios", "id_socio", datoIngresado)
                        val pagoVigente = dbHelper.buscarPagoVigenteSocio(nroCliente, LocalDate.now().plusMonths(-1).toString())
                        if (ORIGEN == "btnPagar" && pagoVigente == "si") {
                            Toast.makeText(this, "El socio tiene la cuota al día.", Toast.LENGTH_SHORT).show()
                            TXT_CLIENTE.text.clear()
                            TXT_CLIENTE.requestFocus()
                        }
                        else acceder(ORIGEN.toString(), CLIENTE.toString(), datoIngresado)
                    }
                    else Toast.makeText(this, "Número de socio no registrado", Toast.LENGTH_SHORT).show()
                }
                else {
                    val idCliente = dbHelper.buscarClientePorDocumento(datoIngresado)
                    val esSocio = dbHelper.buscarClienteEnAnexos("Socios", "id_socio", idCliente)
                    if (esSocio != "no") {
                        Toast.makeText(this, "Cliente registrado como socio nro. $esSocio", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        val idTipoCliente = dbHelper.buscarClienteEnAnexos("NoSocios", "id_nosocio", idCliente)
                        if (idTipoCliente != "no") acceder(ORIGEN.toString(), CLIENTE.toString(), idTipoCliente)
                        else Toast.makeText(this, "Número de documento no registrado", Toast.LENGTH_SHORT).show()
                    }
                }
                TXT_CLIENTE.text.clear()
                TXT_CLIENTE.requestFocus()
            }
        }

        // Volver al menú clientes con parámetros socio / no socio
        BTN_VOLVER.setOnClickListener {
            val INTENT = Intent(this, MenuClientes::class.java)
            INTENT.putExtra("cliente", CLIENTE)
            startActivity(INTENT)
        }
    }
}