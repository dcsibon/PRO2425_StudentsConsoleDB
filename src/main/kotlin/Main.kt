package es.prog2425.students

import es.prog2425.students.app.StudentsApp
import es.prog2425.students.data.dao.AddressDAOExposed
import es.prog2425.students.data.dao.StudentDAOExposed
import es.prog2425.students.data.db.SchemaInitializer
import es.prog2425.students.service.AddressService
import es.prog2425.students.service.StudentTransactionService
import es.prog2425.students.service.StudentService
import es.prog2425.students.ui.Console

fun main() {
    // Inicializa Exposed y conecta a la base de datos (no se usa DataSource directamente)
    SchemaInitializer.init()

    // Crea la instancia de consola
    val consola = Console()

    // Crea instancias DAO usando Exposed
    val studentsDAO = StudentDAOExposed()
    val addressDAO = AddressDAOExposed()

    // Crea instancias de servicios que retornan DTOs
    val studentsService = StudentService(studentsDAO)
    val addressService = AddressService(addressDAO)

    // Servicio orquestador (transaccional)
    val studentsTrans = StudentTransactionService(studentsDAO, addressDAO)

    // Crea la app que gestiona el flujo
    val app = StudentsApp(studentsService, addressService, studentsTrans, consola)
    app.menu()
}