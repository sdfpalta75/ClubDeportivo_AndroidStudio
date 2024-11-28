package com.example.club

import android.app.AlertDialog
import android.content.Context

class FuncionesComplementarias {

    fun validarClave (clave: String, largoMinimo: Int) : Boolean {
        val expresionRegular = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).*$")
        return clave.length >= largoMinimo && expresionRegular.matches(clave)
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