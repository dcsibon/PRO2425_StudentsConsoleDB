package es.prog2425.students.data.dao

import es.prog2425.students.data.db.Database
import es.prog2425.students.model.Student
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

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
        } catch (e: Exception) {
            println("Error al recuperar estudiantes: ${e.message}")
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
        } catch (e: Exception) {
            println("Error al añadir estudiante: ${e.message}")
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
        } catch (e: Exception) {
            println("Error al actualizar estudiante: ${e.message}")
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
        } catch (e: Exception) {
            println("Error al eliminar estudiante: ${e.message}")
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
        } catch (e: Exception) {
            println("Error al buscar estudiante por ID: ${e.message}")
        } finally {
            try { rs?.close() } catch (_: Exception) {}
            try { stmt?.close() } catch (_: Exception) {}
            Database.closeConnection(conn)
        }

        return null
    }
}