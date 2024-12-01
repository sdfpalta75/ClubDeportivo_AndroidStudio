package com.example.club

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Space
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class RegistroDatosUsuario : AppCompatActivity() {
    private lateinit var dbHelper: ClubDatabaseHelper
    private lateinit var funHelper: FuncionesComplementarias
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_datos_usuario)

        dbHelper = ClubDatabaseHelper(this)
        funHelper = FuncionesComplementarias()

        // CAPTURAR ELEMENTOS PROPIOS
        val TV_TITULO = findViewById<TextView>(R.id.tvTitulo)
        val ET_NUEVO_USUARIO = findViewById<EditText>(R.id.etNuevoUsuario)
        val ET_CLAVE_NUEVO_USUARIO = findViewById<EditText>(R.id.etClaveNuevoUsuario)
        val ET_REPETIR_CLAVE = findViewById<EditText>(R.id.etRepetirClave)
        val ET_USUARIO_AVAL = findViewById<EditText>(R.id.etUsuarioAvalante)
        val SP_USUARIO_AVAL = findViewById<Space>(R.id.spUsuarioAvalante)
        val ET_CLAVE_USUARIO_AVAL = findViewById<EditText>(R.id.etClaveUsuarioAvalante)
        val BTN_REGISTRAR = findViewById<Button>(R.id.btnRegistrar)
        val BTN_CANCELAR = findViewById<Button>(R.id.btnCancelar)

        // CAPTURAR PARAMETROS EXTERNOS
        val ORIGEN = intent.getStringExtra("origen")

        // ACOMODAR VISTA EN RELACION AL ORIGEN CAMBIAR CONTRASEÑA
        if (ORIGEN == "cambiarClave"){
            val titulo = "Cambio de contraseña"
            TV_TITULO.text = titulo
            ET_USUARIO_AVAL.visibility = View.GONE
            SP_USUARIO_AVAL.visibility = View.GONE
            // aca asignar nombres de variable acordes, a los objetos a utilizar
            ET_NUEVO_USUARIO.hint = "Usuario"
            ET_CLAVE_NUEVO_USUARIO.hint = "Contraseña anterior"
            ET_REPETIR_CLAVE.hint = "Nueva contraseña"
            ET_CLAVE_USUARIO_AVAL.hint = "Repetir nueva contraseña"
        }

        // BTN REGISTRAR
        BTN_REGISTRAR.setOnClickListener {
            // Funciones propias del evento
            fun controlarCamposVacios(): Boolean {
                val respuesta = ET_NUEVO_USUARIO.text.isEmpty() ||
                    ET_CLAVE_NUEVO_USUARIO.text.isEmpty() ||
                    ET_REPETIR_CLAVE.text.isEmpty() ||
                    ET_CLAVE_USUARIO_AVAL.text.isEmpty()
                if (ORIGEN == "registrarUsuario") return respuesta || ET_USUARIO_AVAL.text.isEmpty()
                else return respuesta
            }

            fun controlarAval(usuario: String, clave: String): Boolean {
                if (dbHelper.contarUsuariosRegistrados()>0) {
                    if (dbHelper.validarAcceso(usuario, clave)) return true
                    else return false
                } else {
                    if (usuario == "admin" && clave == "admin") return true
                    else return false
                }
            }

            if (controlarCamposVacios()) { // CONTROL DE CAMPOS VACIOS
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            } else {
                if (ORIGEN == "registrarUsuario") {
                    // LOGICA PARA REGISTRAR USUARIO
                    val USUARIO = ET_NUEVO_USUARIO.text.toString()
                    val CLAVE_USUARIO = ET_CLAVE_NUEVO_USUARIO.text.toString()
                    val CLAVE_REPETIDA = ET_REPETIR_CLAVE.text.toString()
                    val AVAL = ET_USUARIO_AVAL.text.toString()
                    val CLAVE_AVAL = ET_CLAVE_USUARIO_AVAL.text.toString()
                    if (controlarAval(AVAL, CLAVE_AVAL)) {
                        // control disponibilidad usuario
                        if (dbHelper.informarDisponibilidadNombreUsuario(USUARIO)) {
                            // control largo y combinación clave
                            if (funHelper.validarClave(CLAVE_USUARIO, 6)) {
                                // control coincidencia clave
                                if (CLAVE_USUARIO == CLAVE_REPETIDA) {
                                    // registrar el usuario
                                    val AVAL_ID = if(dbHelper.contarUsuariosRegistrados()>0) dbHelper.informarIdUsuario(AVAL, CLAVE_AVAL) else 0
                                    val registroUsuario = dbHelper.registrarUsuario(USUARIO, CLAVE_USUARIO, AVAL_ID.toString())
                                    if(registroUsuario<0) {
                                        Toast.makeText(this, "Registro infructuoso", Toast.LENGTH_SHORT).show()
                                    } else {
                                        // retornar a Acceso
                                        Toast.makeText(this, "Registro correcto!", Toast.LENGTH_SHORT).show()
                                        val INTENT = Intent(this, Acceso::class.java)
                                        startActivity(INTENT)
                                    }
                                } else {
                                    Toast.makeText(this, "La contraseña propuesta debe coincidir en ambos campos", Toast.LENGTH_SHORT).show()
                                    ET_CLAVE_NUEVO_USUARIO.text.clear()
                                    ET_REPETIR_CLAVE.text.clear()
                                    ET_CLAVE_NUEVO_USUARIO.requestFocus()
                                }
                            } else {
                                Toast.makeText(this, "La contraseña no cumple los requisitos", Toast.LENGTH_SHORT).show()
                                ET_CLAVE_NUEVO_USUARIO.text.clear()
                                ET_REPETIR_CLAVE.text.clear()
                                ET_CLAVE_NUEVO_USUARIO.requestFocus()
                            }
                        } else {
                            Toast.makeText(this, "Nombre de usuario no disponible", Toast.LENGTH_SHORT).show()
                            ET_NUEVO_USUARIO.text.clear()
                            ET_NUEVO_USUARIO.requestFocus()
                        }
                    } else {
                        Toast.makeText(this, "Aval no autorizado", Toast.LENGTH_SHORT).show()
                        ET_USUARIO_AVAL.text.clear()
                        ET_CLAVE_USUARIO_AVAL.text.clear()
                        ET_USUARIO_AVAL.requestFocus()
                    }
                } else {
                    // LOGICA PARA CAMBIAR CLAVE
                    val USUARIO = ET_NUEVO_USUARIO.text.toString()
                    val CLAVE_ANTERIOR = ET_CLAVE_NUEVO_USUARIO.text.toString()
                    val CLAVE_NUEVA = ET_REPETIR_CLAVE.text.toString()
                    val REPETIR_NUEVA_CLAVE = ET_CLAVE_USUARIO_AVAL.text.toString()

                    // control usuario existente
                    if (dbHelper.validarAcceso(USUARIO, CLAVE_ANTERIOR)) {
                        // control largo y combinación clave
                        if (funHelper.validarClave(CLAVE_NUEVA, 6)) {
                            // control coincidencia clave
                            if (CLAVE_NUEVA == REPETIR_NUEVA_CLAVE) {
                                // registrar el usuario
                                val USUARIO_ID = dbHelper.informarIdUsuario(USUARIO, CLAVE_ANTERIOR).toString()
                                val registroActualizado = dbHelper.cambiarClaveUsuario(USUARIO_ID, CLAVE_NUEVA)
                                if(registroActualizado) {
                                    // retornar a Acceso
                                    Toast.makeText(this, "Cambio de contraseña exitoso!", Toast.LENGTH_SHORT).show()
                                    val INTENT = Intent(this, Acceso::class.java)
                                    startActivity(INTENT)
                                } else {
                                    Toast.makeText(this, "Cambio de contraseña infructuoso", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this, "La contraseña propuesta debe coincidir en ambos campos", Toast.LENGTH_SHORT).show()
                                ET_REPETIR_CLAVE.text.clear()
                                ET_CLAVE_USUARIO_AVAL.text.clear()
                                ET_REPETIR_CLAVE.requestFocus()
                            }
                        } else {
                            Toast.makeText(this, "La contraseña no cumple los requisitos", Toast.LENGTH_SHORT).show()
                            ET_REPETIR_CLAVE.text.clear()
                            ET_CLAVE_USUARIO_AVAL.text.clear()
                            ET_REPETIR_CLAVE.requestFocus()
                        }
                    } else {
                        Toast.makeText(this, "Usuario no registrado", Toast.LENGTH_SHORT).show()
                        ET_NUEVO_USUARIO.text.clear()
                        ET_CLAVE_NUEVO_USUARIO.text.clear()
                        ET_NUEVO_USUARIO.requestFocus()
                    }

                }
            }

        }

        // BTN CANCELAR
        BTN_CANCELAR.setOnClickListener {
            val INTENT = Intent(this, Acceso::class.java)
            startActivity(INTENT)
        }
    }
}