package com.example.club

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class ClubDatabaseHelper(context:Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        // Base de datos
        private val DATABASE_NAME = "CLUB.db"
        private val DATABASE_VERSION = 1

        // Tabla Usuarios
        private val TABLA_USUARIOS = "Usuarios"
        private val CAMPO_USUARIOS_ID = "id_usuario"
        private val CAMPO_USUARIOS_USUARIO = "usuario"
        private val CAMPO_USUARIOS_CLAVE = "clave"
        private val CAMPO_USUARIOS_AVAL = "aval"

        // Tabla Clientes
        private val TABLA_CLIENTES = "Clientes"
        private val CAMPO_CLIENTES_ID = "id_cliente"
        private val CAMPO_CLIENTES_NOMBRE = "nombre"
        private val CAMPO_CLIENTES_APELLIDO = "apellido"
        private val CAMPO_CLIENTES_DOCUMENTO = "documento"
        private val CAMPO_CLIENTES_NACIMIENTO = "nacimiento"
        private val CAMPO_CLIENTES_DOMICILIO = "domicilio"
        private val CAMPO_CLIENTES_TELEFONO = "telefono"
        private val CAMPO_CLIENTES_EMAIL = "email"
        private val CAMPO_CLIENTES_APTO_FISICO = "apto"

        // Tabla Socios
        private val TABLA_SOCIOS = "Socios"
        private val CAMPO_SOCIOS_ID = "id_socio"
        private val CAMPO_SOCIOS_ID_CLIENTE = "id_cliente"

        // Tabla NoSocios
        private val TABLA_NOSOCIOS = "NoSocios"
        private val CAMPO_NOSOCIOS_ID = "id_nosocio"
        private val CAMPO_NOSOCIOS_ID_CLIENTE = "id_cliente"

        // Tabla Pagos Socios
        private val TABLA_PAGOS_SOCIOS = "PagosSocios"
        private val CAMPO_PAGOS_SOCIOS_ID = "id_pago_socio"
        private val CAMPO_PAGOS_SOCIOS_ID_CLIENTE = "id_cliente"
        private val CAMPO_PAGOS_SOCIOS_FECHA = "fecha_pago"
        private val CAMPO_PAGOS_SOCIOS_MEDIO = "medio"

        // Tabla Pagos No Socios
        private val TABLA_PAGOS_NOSOCIOS = "PagosNoSocios"
        private val CAMPO_PAGOS_NOSOCIOS_ID = "id_pago_nosocio"
        private val CAMPO_PAGOS_NOSOCIOS_ID_CLIENTE = "id_cliente"
        private val CAMPO_PAGOS_NOSOCIOS_FECHA = "fecha_pago"
        private val CAMPO_PAGOS_NOSOCIOS_MEDIO = "medio"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val crearTablaUsuarios = ("CREATE TABLE " + TABLA_USUARIOS + " ("
                + CAMPO_USUARIOS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CAMPO_USUARIOS_USUARIO + " TEXT, "
                + CAMPO_USUARIOS_CLAVE + " TEXT, "
                + CAMPO_USUARIOS_AVAL + " INTEGER)")
        db.execSQL(crearTablaUsuarios)

        val agregarUsuarioAdmin = ("INSERT INTO " + TABLA_USUARIOS + " ("
                + CAMPO_USUARIOS_USUARIO + ", "
                + CAMPO_USUARIOS_CLAVE + ", "
                + CAMPO_USUARIOS_AVAL + ") "
                + "VALUES ('admin', 'admin', 0)")
        db.execSQL(agregarUsuarioAdmin)

        val crearTablaClientes = ("CREATE TABLE " + TABLA_CLIENTES + " ("
                + CAMPO_CLIENTES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CAMPO_CLIENTES_NOMBRE + " TEXT, "
                + CAMPO_CLIENTES_APELLIDO + " TEXT, "
                + CAMPO_CLIENTES_DOCUMENTO + " INTEGER, "
                + CAMPO_CLIENTES_NACIMIENTO + " TEXT, "
                + CAMPO_CLIENTES_DOMICILIO + " TEXT, "
                + CAMPO_CLIENTES_TELEFONO + " TEXT, "
                + CAMPO_CLIENTES_EMAIL + " TEXT, "
                + CAMPO_CLIENTES_APTO_FISICO + " INTEGER)")
        db.execSQL(crearTablaClientes)

        val crearTablaSocios = ("CREATE TABLE " + TABLA_SOCIOS + " ("
                + CAMPO_SOCIOS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CAMPO_SOCIOS_ID_CLIENTE + " INTEGER)")
        db.execSQL(crearTablaSocios)

        val crearTablaNoSocios = ("CREATE TABLE " + TABLA_NOSOCIOS + " ("
                + CAMPO_NOSOCIOS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CAMPO_NOSOCIOS_ID_CLIENTE + " INTEGER)")
        db.execSQL(crearTablaNoSocios)

        val crearTablaPagosSocios = ("CREATE TABLE " + TABLA_PAGOS_SOCIOS + " ("
                + CAMPO_PAGOS_SOCIOS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CAMPO_PAGOS_SOCIOS_ID_CLIENTE + " INTEGER, "
                + CAMPO_PAGOS_SOCIOS_FECHA + " TEXT, "
                + CAMPO_PAGOS_SOCIOS_MEDIO + " TEXT)")
        db.execSQL(crearTablaPagosSocios)

        val crearTablaPagosNoSocios = ("CREATE TABLE " + TABLA_PAGOS_NOSOCIOS + " ("
                + CAMPO_PAGOS_NOSOCIOS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CAMPO_PAGOS_NOSOCIOS_ID_CLIENTE + " INTEGER, "
                + CAMPO_PAGOS_NOSOCIOS_FECHA + " TEXT, "
                + CAMPO_PAGOS_NOSOCIOS_MEDIO + " TEXT)")
        db.execSQL(crearTablaPagosNoSocios)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLA_USUARIOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLA_CLIENTES")
        db.execSQL("DROP TABLE IF EXISTS $TABLA_SOCIOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLA_NOSOCIOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLA_PAGOS_SOCIOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLA_PAGOS_NOSOCIOS")
        onCreate(db)
    }

    // Funciones relacionadas a usuarios administradores
    fun validarUsuario(nombre: String, clave: String):Boolean {
        val bd = this.readableDatabase
        val sql = "SELECT clave FROM Usuarios WHERE usuario = ?"
        val cursor = bd.rawQuery(sql, arrayOf(nombre))

        if (cursor.moveToFirst()){
            val claveAlmacenada = cursor.getString(0)
            cursor.close()
            return claveAlmacenada == clave
        }
        else {
            cursor.close()
            return false
        }
    }

    // Funciones relacionadas a clientes (socios y no socios)
    fun agregarCliente(nombre: String, apellido: String, documento: String, nacimiento: String,
                       domicilio: String, telefono: String, mail: String, apto: String): Long {
        val db = this.writableDatabase
        val registro = ContentValues().apply {
            put(CAMPO_CLIENTES_NOMBRE, nombre)
            put(CAMPO_CLIENTES_APELLIDO, apellido)
            put(CAMPO_CLIENTES_DOCUMENTO, documento)
            put(CAMPO_CLIENTES_NACIMIENTO, nacimiento)
            put(CAMPO_CLIENTES_DOMICILIO, domicilio)
            put(CAMPO_CLIENTES_TELEFONO, telefono)
            put(CAMPO_CLIENTES_EMAIL, mail)
            put(CAMPO_CLIENTES_APTO_FISICO, apto)
        }
        val nuevoRegistro = db.insert(TABLA_CLIENTES, null, registro)
        return nuevoRegistro
    }

    // Registrar al cliente en la tabla de Socios o No Socios, según corresponda
    fun agregarTipoCliente(tabla: String, nroCliente: String): Long {
        val db = this.writableDatabase
        val registro = ContentValues().apply {
            put("id_cliente", nroCliente)
        }
        val nuevoRegistro = db.insert(tabla, null, registro)
        return nuevoRegistro
    }

    fun buscarClientePorDocumento(documento: String): String {
        val bd = this.readableDatabase
        val sql = "SELECT id_cliente FROM Clientes WHERE documento = ?"
        val cursor = bd.rawQuery(sql, arrayOf(documento))

        if (cursor.moveToFirst()) {
            val idAlmacenado = cursor.getString(0)
            cursor.close()
            return idAlmacenado
        }
        else {
            cursor.close()
            return "no"
        }
    }

    // Buscar si existe el cliente como socio o no socio, según corresponda
    fun buscarClienteEnAnexos(tablaDeBusqueda: String, idEsperado: String, idCliente: String): String {
        val bd = this.readableDatabase
        val sql = "SELECT $idEsperado FROM $tablaDeBusqueda WHERE id_cliente = ?"
        val cursor = bd.rawQuery(sql, arrayOf(idCliente))

        if (cursor.moveToFirst()) {
            val idAlmacenado = cursor.getString(0)
            cursor.close()
            return idAlmacenado
        }
        else {
            cursor.close()
            return "no"
        }
    }

    // Buscar el numero de cliente correspondiente al idTipo
    fun buscarIdClientePorIdTipo(tablaDeBusqueda: String, campoDeBusqueda: String, idABuscar: String): String {
        val bd = this.readableDatabase
        val sql = "SELECT id_cliente FROM $tablaDeBusqueda WHERE $campoDeBusqueda = ?"
        val cursor = bd.rawQuery(sql, arrayOf(idABuscar))

        if (cursor.moveToFirst()) {
            val idAlmacenado = cursor.getString(0)
            cursor.close()
            return idAlmacenado
        }
        else {
            cursor.close()
            return "no"
        }
    }

    // Funciones relacionadas a pagos
    fun agregarPago(tabla: String, idCliente: String, fecha: String, medio: String): Long {
        val db = this.writableDatabase
        val registro = ContentValues().apply {
            put(CAMPO_PAGOS_SOCIOS_ID_CLIENTE, idCliente)
            put(CAMPO_PAGOS_SOCIOS_FECHA, fecha)
            put(CAMPO_PAGOS_SOCIOS_MEDIO, medio)
        }
        val nuevoRegistro = db.insert(tabla, null, registro)
        return nuevoRegistro
    }

    fun buscarVencimientosSocios(fecha: String): List<String> {
        val listadoVencimientos = mutableListOf<String>()
        val bd = this.readableDatabase
        val sql = "SELECT Clientes.nombre, Clientes.apellido, Socios.id_socio " +
                  "FROM Clientes, PagosSocios, Socios " +
                  "WHERE (Clientes.id_cliente = PagosSocios.id_cliente " +
                         "AND Clientes.id_cliente = Socios.id_cliente " +
                         "AND PagosSocios.fecha_pago = '"+fecha+"')"

        val cursor = bd.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            do {
                val nroSocio = cursor.getString(cursor.getColumnIndex("id_socio"))
                val nombre = cursor.getString(cursor.getColumnIndex("nombre"))
                val apellido = cursor.getString(cursor.getColumnIndex("apellido"))
                listadoVencimientos.add("Socio $nroSocio, $nombre $apellido")
            } while (cursor.moveToNext())
            cursor.close()
            return listadoVencimientos
        }
        else {
            cursor.close()
            listadoVencimientos.add("Hoy no se registran vencimientos")
            return listadoVencimientos
        }
    }

    fun buscarPagoVigenteSocio(nroCliente: String, fecha: String): String {
        val bd = this.readableDatabase
        val sql = "SELECT id_cliente FROM PagosSocios " +
                  "WHERE (id_cliente = $nroCliente AND fecha_pago > '"+fecha+"')"

        val cursor = bd.rawQuery(sql, null)

        if (cursor.moveToFirst()) {
            cursor.close()
            return "si"
        }
        else {
            cursor.close()
            return "no"
        }
    }
}