package es.prog2425.students.data.dao

import es.prog2425.students.model.Student
import org.jetbrains.exposed.sql.transactions.transaction

class StudentDAOExposed : IStudentDAO {

    override fun getAll(): List<Student> = transaction {
        Student.all().toList()
    }

    override fun getById(id: Int): Student? = transaction {
        Student.findById(id)
    }

    override fun add(name: String): Student = transaction {
        Student.new {
            this.name = name
        }
    }

    override fun update(id: Int, name: String): Boolean = transaction {
        val student = Student.findById(id)
        student?.let {
            it.name = name
            true
        } ?: false
    }

    override fun delete(id: Int): Boolean = transaction {
        Student.findById(id)?.delete() != null
    }
}