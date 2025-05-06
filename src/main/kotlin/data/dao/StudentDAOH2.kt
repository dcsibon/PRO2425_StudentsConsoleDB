package es.prog2425.students.data.dao

import es.prog2425.students.data.db.DataSourceFactory
import es.prog2425.students.model.Student
import javax.sql.DataSource

class StudentDAOH2(
    mode: DataSourceFactory.Mode = DataSourceFactory.Mode.HIKARI
) : IStudentDAO {

    private val dataSource: DataSource = DataSourceFactory.getDataSource(mode)

    override fun getAll(): List<Student> {
        val students = mutableListOf<Student>()
        dataSource.connection.use { conn ->
            conn.prepareStatement("SELECT * FROM students").use { stmt ->
                stmt.executeQuery().use { rs ->
                    while (rs.next()) {
                        students.add(Student(rs.getInt("id"), rs.getString("name")))
                    }
                }
            }
        }
        return students
    }

    override fun getById(id: Int): Student? {
        dataSource.connection.use { conn ->
            conn.prepareStatement("SELECT * FROM students WHERE id = ?").use { stmt ->
                stmt.setInt(1, id)
                stmt.executeQuery().use { rs ->
                    return if (rs.next()) Student(rs.getInt("id"), rs.getString("name")) else null
                }
            }
        }
    }

    override fun add(name: String) {
        dataSource.connection.use { conn ->
            conn.prepareStatement("INSERT INTO students (name) VALUES (?)").use { stmt ->
                stmt.setString(1, name)
                stmt.executeUpdate()
            }
        }
    }

    override fun update(id: Int, name: String) {
        dataSource.connection.use { conn ->
            conn.prepareStatement("UPDATE students SET name = ? WHERE id = ?").use { stmt ->
                stmt.setString(1, name)
                stmt.setInt(2, id)
                stmt.executeUpdate()
            }
        }
    }

    override fun delete(id: Int) {
        dataSource.connection.use { conn ->
            conn.prepareStatement("DELETE FROM students WHERE id = ?").use { stmt ->
                stmt.setInt(1, id)
                stmt.executeUpdate()
            }
        }
    }
}