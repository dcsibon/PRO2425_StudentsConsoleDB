package es.prog2425.students.data.dao

import es.prog2425.students.model.Student
import java.sql.Connection

interface IStudentDAO {
    fun getAll(): List<Student>
    fun getById(id: Int): Student?
    fun add(name: String)
    fun update(id: Int, name: String)
    fun delete(id: Int)

    fun delete(id: Int, conn: Connection)
}