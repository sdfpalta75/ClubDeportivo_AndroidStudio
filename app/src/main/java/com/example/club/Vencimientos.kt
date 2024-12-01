package com.example.club

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.time.LocalDate

class Vencimientos : AppCompatActivity() {
    private lateinit var dbHelper: ClubDatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vencimientos)

        dbHelper = ClubDatabaseHelper(this)

        val fechaActual = LocalDate.now()
        val unMesAtras = fechaActual.plusMonths(-1)

        // Capturo objetos
        val TV_TITULO = findViewById<TextView>(R.id.txtTituloVencimientos)
        val BTN_SOCIOS = findViewById<Button>(R.id.btnLstSocios)
        val BTN_NO_SOCIOS = findViewById<Button>(R.id.btnLstNoSocios)
        val TBL_DATOS = findViewById<TableLayout>(R.id.tblDatos)
        val TBL_CABECERA = findViewById<TextView>(R.id.cabeceraCelda1)
        val TBL_VENCIMIENTOS = findViewById<LinearLayout>(R.id.tblVencimientos)
        val BTN_VOLVER = findViewById<Button>(R.id.btnVolver)

        // Funcíón para borrar el TableLayout (queda solo el encabezado)
        fun limpiarTabla() {
            for (i in TBL_DATOS.childCount - 1 downTo 1) {
                TBL_DATOS.removeViewAt(i)
            }
        }

        // Función para agregar una fila al TableLayout
        fun agregarFila(numero: String, nombre: String) {
            val FILA = TableRow(this)
            val CELDA1 = TextView(this)
            val CELDA2 = TextView(this)
            CELDA1.text = numero
            CELDA1.textSize = 15f
            CELDA1.setPadding(0,2,0,0)
            CELDA1.setTextColor(Color.WHITE)
            CELDA1.textAlignment = View.TEXT_ALIGNMENT_CENTER
            CELDA2.text = nombre
            CELDA2.textSize = 15f
            CELDA2.setPadding(4,2,0,0)
            CELDA2.setTextColor(Color.WHITE)
            FILA.addView(CELDA1)
            FILA.addView(CELDA2)
            TBL_DATOS.addView(FILA)
        }

        // Presiona botón Lista de socios
        BTN_SOCIOS.setOnClickListener {
            val titulo = "Vencimientos de la fecha"
            TV_TITULO.text = titulo
            BTN_SOCIOS.backgroundTintList = ContextCompat.getColorStateList(this, R.color.btnVerdeEncendido)
            BTN_NO_SOCIOS.backgroundTintList = ContextCompat.getColorStateList(this, R.color.btnVerdeApagado)
            TBL_VENCIMIENTOS.visibility = View.VISIBLE
            val campoCabecera = "Socio N°"
            TBL_CABECERA.text = campoCabecera
            val VENCIMIENTOS = dbHelper.listarSociosConPagoEnUnaFecha(unMesAtras.toString())
            limpiarTabla()
            for (itemLista in VENCIMIENTOS) {
                agregarFila(itemLista.numero, itemLista.nombre)
            }
        }

        // Presiona botón Lista de No socios
        BTN_NO_SOCIOS.setOnClickListener {
            val titulo = "Activos de la fecha"
            TV_TITULO.text = titulo
            BTN_NO_SOCIOS.backgroundTintList = ContextCompat.getColorStateList(this, R.color.btnVerdeEncendido)
            BTN_SOCIOS.backgroundTintList = ContextCompat.getColorStateList(this, R.color.btnVerdeApagado)
            TBL_VENCIMIENTOS.visibility = View.VISIBLE
            TBL_CABECERA.text = "D.N.I."
            val VENCIMIENTOS = dbHelper.listarNoSociosConPagoEnUnaFecha(fechaActual.toString())
            limpiarTabla()
            for (itemLista in VENCIMIENTOS) {
                agregarFila(itemLista.numero, itemLista.nombre)
            }
        }

        // Regresa a la activity MenuPpal
        BTN_VOLVER.setOnClickListener {
            val INTENT = Intent(this, MenuPpal::class.java)
            startActivity(INTENT)
        }
    }
}