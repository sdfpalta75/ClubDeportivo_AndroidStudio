package com.example.club

import android.app.AlertDialog
import android.content.Context

class FuncionesComplementarias {

    fun validarClave (clave: String) : Boolean {
        val expresionRegular = Regex("(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)")
        return clave.length >= 6 && expresionRegular.matches(clave)
    }


    fun cuadroDeDialogo (context: Context, titulo: String, mensaje: String) {
        val estructura = AlertDialog.Builder(context)
        estructura.setMessage(mensaje)
            .setTitle(titulo)
            .setPositiveButton("Continuar") {dialog, which ->
                dialog.dismiss()
            }

        val cuadro: AlertDialog = estructura.create()
        cuadro.show()
    }
}