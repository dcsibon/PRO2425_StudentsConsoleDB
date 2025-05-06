package es.prog2425.students.data.db

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object Database {

    // Parámetros de conexión
    private const val JDBC_URL = "jdbc:h2:file:./data/studentdb"
    private const val USER = "sa"
    private const val PASSWORD = ""

    init {
        try {
            Class.forName("org.h2.Driver")
        } catch (e: ClassNotFoundException) {
            throw IllegalStateException("No se pudo cargar el driver de H2", e)
        }
    }

    /**
     * Establece la conexión a la base de datos H2.
     * Lanza una excepción si no se puede conectar.
     */
    fun getConnection(): Connection {
        return try {
            DriverManager.getConnection(JDBC_URL, USER, PASSWORD)
        } catch (e: SQLException) {
            throw IllegalStateException("Error al conectar con la base de datos H2", e)
        }
    }

    /**
     * Cierra la conexión de forma segura.
     * Si hay error al cerrar, lanza una excepción controlada.
     */
    fun closeConnection(conn: Connection?) {
        try {
            conn?.close()
        } catch (e: SQLException) {
            throw IllegalStateException("Error al cerrar la conexión con la base de datos", e)
        }
    }
}