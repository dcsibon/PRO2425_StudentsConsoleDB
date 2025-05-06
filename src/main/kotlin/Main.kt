package es.prog2425.students

import es.prog2425.students.app.StudentsManager
import es.prog2425.students.data.dao.StudentDAOH2
import es.prog2425.students.service.StudentService
import es.prog2425.students.ui.Console


fun main() {
    val dao = StudentDAOH2()
    val service = StudentService(dao)
    val consola = Console()
    val app = StudentsManager(service, consola)
    app.run()
}