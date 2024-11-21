package com.example.club

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate

class Pagos1 : AppCompatActivity() {
    private lateinit var bdHelper: ClubDatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pagos1)

        bdHelper = ClubDatabaseHelper(this)

        // Capturar objetos locales
        val RB_EFECTIVO = findViewById<RadioButton>(R.id.rbEfectivo)
        val RB_1PAGO = findViewById<RadioButton>(R.id.rb1pago)
        val RB_3PAGOS = findViewById<RadioButton>(R.id.rb3pagos)
        val RB_6PAGOS = findViewById<RadioButton>(R.id.rb6pagos)
        val BTN_CONFIRMAR = findViewById<Button>(R.id.btnConfirmar)
        val BTN_VOLVER = findViewById<Button>(R.id.btnVolver)

        // Capturar argumentos y adaptar vista
        val CLIENTE = intent.getStringExtra("cliente")
        val ORIGEN = intent.getStringExtra("origen")
        val NRO_TIPO_CLIENTE = intent.getStringExtra("nroTipoCliente")

        // Ir a finalizar pago con parámetros socio / no socio
        BTN_CONFIRMAR.setOnClickListener {
            fun acceder(tipoCliente: String) {
                val INTENT = Intent(this, Pagos2::class.java)
                INTENT.putExtra("cliente", tipoCliente)
                startActivity(INTENT)
            }

            if (!RB_EFECTIVO.isChecked && !RB_1PAGO.isChecked && !RB_3PAGOS.isChecked && !RB_6PAGOS.isChecked) {
                Toast.makeText(this, "Debe seleccionar un medio de pago", Toast.LENGTH_SHORT).show()
            }
            else {
                val fechaActual: LocalDate = LocalDate.now()
                val seleccion = if (RB_EFECTIVO.isChecked) "Efectivo"
                                else if (RB_1PAGO.isChecked) "1 pago"
                                else if (RB_3PAGOS.isChecked) "3 pagos"
                                else "6 pagos"
                if (CLIENTE == "s") {
                    val nroCliente = bdHelper.buscarIdClientePorIdTipo("Socios", "id_socio", NRO_TIPO_CLIENTE.toString())
                    val pagoSocio = bdHelper.agregarPago("PagosSocios", nroCliente, fechaActual.toString(), seleccion)
                    if (pagoSocio > 0) acceder(CLIENTE.toString())
                    else Toast.makeText(this, "No se pudo registrar el pago", Toast.LENGTH_SHORT).show()
                }
                else {
                    val nroCliente = bdHelper.buscarIdClientePorIdTipo("NoSocios", "id_nosocio", NRO_TIPO_CLIENTE.toString())
                    val pagoNoSocio = bdHelper.agregarPago("PagosNoSocios", nroCliente, fechaActual.toString(), seleccion)
                    if (pagoNoSocio > 0) acceder(CLIENTE.toString())
                    else Toast.makeText(this, "No se pudo registrar el pago", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Volver al menú origen con parámetros socio / no socio
        BTN_VOLVER.setOnClickListener {
            val INTENT = if (ORIGEN == "pagos1") Intent(this, BuscarCliente::class.java) else Intent(this, RegistroPaso2::class.java)
            INTENT.putExtra("cliente", CLIENTE)
            INTENT.putExtra("nroTipoCliente", NRO_TIPO_CLIENTE)
            INTENT.putExtra("origen", "btnPagar")
            startActivity(INTENT)
        }
    }
}