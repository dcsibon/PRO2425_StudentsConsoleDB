package es.prog2425.students.data.dao

import es.prog2425.students.data.db.Database
import es.prog2425.students.model.Student
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class StudentDAOH2: IStudentDAO {

    override fun getAll(): List<Student> {
        val students = mutableListOf<Student>()
        var conn: Connection? = null
        var stmt: PreparedStatement? = null
        var rs: ResultSet? = null

        try {
            conn = Database.getConnection()
            stmt = conn.prepareStatement("SELECT * FROM students ORDER BY id")
            rs = stmt.executeQuery()
            while (rs.next()) {
                val id = rs.getInt("id")
                val name = rs.getString("name")
                students.add(Student(id, name))
            }
        } catch (e: SQLException) {
            throw IllegalStateException("Error al recuperar estudiantes de la base de datos", e)
        } catch (e: Exception) {
            throw IllegalStateException("Error inesperado al recuperar estudiantes", e)
        } finally {
            try { rs?.close() } catch (_: Exception) {}
            try { stmt?.close() } catch (_: Exception) {}
            Database.closeConnection(conn)
        }

        return students
    }

    override fun add(name: String) {
        var conn: Connection? = null
        var stmt: PreparedStatement? = null

        try {
            conn = Database.getConnection()
            stmt = conn.prepareStatement("INSERT INTO students (name) VALUES (?)")
            stmt.setString(1, name)
            stmt.executeUpdate()
        } catch (e: SQLException) {
            throw IllegalStateException("Error al insertar estudiante en la base de datos", e)
        } catch (e: Exception) {
            throw IllegalStateException("Error inesperado al insertar estudiante", e)
        } finally {
            try { stmt?.close() } catch (_: Exception) {}
            Database.closeConnection(conn)
        }
    }

    override fun update(id: Int, name: String) {
        var conn: Connection? = null
        var stmt: PreparedStatement? = null

        try {
            conn = Database.getConnection()
            stmt = conn.prepareStatement("UPDATE students SET name = ? WHERE id = ?")
            stmt.setString(1, name)
            stmt.setInt(2, id)
            stmt.executeUpdate()
        } catch (e: SQLException) {
            throw IllegalStateException("Error al actualizar estudiante en la base de datos", e)
        } catch (e: Exception) {
            throw IllegalStateException("Error inesperado al actualizar estudiante", e)
        } finally {
            try { stmt?.close() } catch (_: Exception) {}
            Database.closeConnection(conn)
        }
    }

    override fun delete(id: Int) {
        var conn: Connection? = null
        var stmt: PreparedStatement? = null

        try {
            conn = Database.getConnection()
            stmt = conn.prepareStatement("DELETE FROM students WHERE id = ?")
            stmt.setInt(1, id)
            stmt.executeUpdate()
        } catch (e: SQLException) {
            throw IllegalStateException("Error al eliminar estudiante en la base de datos", e)
        } catch (e: Exception) {
            throw IllegalStateException("Error inesperado al eliminar estudiante", e)
        } finally {
            try { stmt?.close() } catch (_: Exception) {}
            Database.closeConnection(conn)
        }
    }

    override fun getById(id: Int): Student? {
        var conn: Connection? = null
        var stmt: PreparedStatement? = null
        var rs: ResultSet? = null

        try {
            conn = Database.getConnection()
            stmt = conn.prepareStatement("SELECT * FROM students WHERE id = ?")
            stmt.setInt(1, id)
            rs = stmt.executeQuery()
            if (rs.next()) {
                return Student(rs.getInt("id"), rs.getString("name"))
            }
        } catch (e: SQLException) {
            throw IllegalStateException("Error al buscar estudiante en la base de datos", e)
        } catch (e: Exception) {
            throw IllegalStateException("Error inesperado al buscar estudiante", e)
        } finally {
            try { rs?.close() } catch (_: Exception) {}
            try { stmt?.close() } catch (_: Exception) {}
            Database.closeConnection(conn)
        }

        return null
    }
}