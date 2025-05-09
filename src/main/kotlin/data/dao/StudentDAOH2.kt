package es.prog2425.students.data.dao

import es.prog2425.students.model.Student
import javax.sql.DataSource

class StudentDAOH2(private val ds: DataSource) : IStudentDAO {

    override fun getAll(): List<Student> {
        val students = mutableListOf<Student>()
        ds.connection.use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeQuery("SELECT * FROM students").use { rs ->
                    while (rs.next()) {
                        students.add(Student(rs.getInt("id"), rs.getString("name")))
                    }
                }
            }
        }
        return students
    }

    override fun getById(id: Int): Student? {
        ds.connection.use { conn ->
            conn.prepareStatement("SELECT * FROM students WHERE id = ?").use { stmt ->
                stmt.setInt(1, id)
                stmt.executeQuery().use { rs ->
                    return if (rs.next()) Student(rs.getInt("id"), rs.getString("name")) else null
                }
            }
        }
    }

    override fun add(name: String) {
        ds.connection.use { conn ->
            conn.prepareStatement("INSERT INTO students (name) VALUES (?)").use { stmt ->
                stmt.setString(1, name)
                stmt.executeUpdate()
            }
        }
    }

    override fun update(id: Int, name: String) {
        ds.connection.use { conn ->
            conn.prepareStatement("UPDATE students SET name = ? WHERE id = ?").use { stmt ->
                stmt.setString(1, name)
                stmt.setInt(2, id)
                stmt.executeUpdate()
            }
        }
    }

    override fun delete(id: Int) {
        ds.connection.use { conn ->
            conn.prepareStatement("DELETE FROM students WHERE id = ?").use { stmt ->
                stmt.setInt(1, id)
                stmt.executeUpdate()
            }
        }
    }
}