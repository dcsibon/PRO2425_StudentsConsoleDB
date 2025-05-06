package es.prog2425.students.service

import es.prog2425.students.model.Student

interface IStudentService {
    fun listAll(): List<Student>
    fun getStudentById(id: Int): Student?
    fun addStudent(name: String)
    fun updateStudent(id: Int, name: String)
    fun deleteStudent(id: Int)
}