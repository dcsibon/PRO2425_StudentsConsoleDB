package es.prog2425.students.data.dao

import es.prog2425.students.model.Student

interface IStudentDAO {
    fun getAll(): List<Student>
    fun add(name: String)
    fun update(id: Int, name: String)
    fun delete(id: Int)
}