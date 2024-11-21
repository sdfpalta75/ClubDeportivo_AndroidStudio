package com.example.club

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.time.LocalDate

class Vencimientos : AppCompatActivity() {
    private lateinit var lvListado: ListView
    private lateinit var bdHelper: ClubDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vencimientos)

        lvListado = findViewById(R.id.lvVencimientos)
        bdHelper = ClubDatabaseHelper(this)

        val fechaActual = LocalDate.now()
        val unMesAtras = fechaActual.plusMonths(-1)

        val vencimientos = bdHelper.buscarVencimientosSocios(unMesAtras.toString())

        val adaptadorDeLista = ArrayAdapter(this, android.R.layout.simple_list_item_1, vencimientos)
        lvListado.adapter = adaptadorDeLista

        // Capturo objetos
        val BTN_VOLVER = findViewById<Button>(R.id.btnVolver)

        // Regresa a la activity MenuPpal
        BTN_VOLVER.setOnClickListener {
            val INTENT = Intent(this, MenuPpal::class.java)
            startActivity(INTENT)
        }
    }
}