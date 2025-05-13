package es.prog2425.students.data.db

import java.sql.Connection

object DatabaseInitializer {

    private val ddlStatements = listOf(
        "SET REFERENTIAL_INTEGRITY TRUE;",
        "DROP TABLE IF EXISTS addresses;",
        "DROP TABLE IF EXISTS students;",
        "CREATE TABLE students (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL" +
                ");",
        "CREATE TABLE addresses (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "street VARCHAR(255) NOT NULL, " +
                "city VARCHAR(255) NOT NULL, " +
                "student_id INT, " +
                "FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE" +
                ");"
    )

    private val dmlStatements = listOf(
        "INSERT INTO students (name) VALUES ('Ana');",
        "INSERT INTO students (name) VALUES ('Luis');",
        "INSERT INTO students (name) VALUES ('Sofía');",
        "INSERT INTO students (name) VALUES ('Carlos');",
        "INSERT INTO students (name) VALUES ('María');",
        "INSERT INTO addresses (street, city, student_id) VALUES ('Ronda de Estero 5, 1A', 'San Fernando', 1);",
        "INSERT INTO addresses (street, city, student_id) VALUES ('Plaza de España, 2, Bajo C', 'Cádiz', 1);",
        "INSERT INTO addresses (street, city, student_id) VALUES ('Avenida de la Sanidad, 22, 4D', 'Cádiz', 2);"
    )

    private fun crearTablas(connection: Connection) {
        connection.createStatement().use { stmt ->
            ddlStatements.forEach { sql -> stmt.execute(sql) }
        }
    }

    private fun insertarDatosIniciales(connection: Connection) {
        connection.autoCommit = false
        try {
            connection.createStatement().use { stmt ->
                dmlStatements.forEach { sql -> stmt.execute(sql) }
            }
            connection.commit()
        } catch (e: Exception) {
            connection.rollback()
            throw e
        } finally {
            connection.autoCommit = true
        }
    }

    fun crearTablasYDatos(connection: Connection) {
        crearTablas(connection)
        insertarDatosIniciales(connection)
    }
}
