package es.prog2425.students.service

import es.prog2425.students.data.dao.IStudentDAO
import es.prog2425.students.model.Student

class StudentService(private val dao: IStudentDAO) : IStudentService {

    override fun listAll(): List<Student> = dao.getAll()

    override fun getStudentById(id: Int): Student? {
        require(id > 0) { "ID inválido." }
        return dao.getById(id)
    }

    override fun addStudent(name: String) {
        require(name.isNotBlank()) { "El nombre no puede estar vacío." }
        dao.add(name.trim())
    }

    override fun updateStudent(id: Int, name: String) {
        require(id > 0) { "El ID debe ser mayor que 0." }
        require(name.isNotBlank()) { "El nombre no puede estar vacío." }
        dao.update(id, name.trim())
    }

    override fun deleteStudent(id: Int) {
        require(id > 0) { "El ID debe ser mayor que 0." }
        dao.delete(id)
    }
}