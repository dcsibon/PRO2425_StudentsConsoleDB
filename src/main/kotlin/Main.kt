package es.prog2425.students

import com.zaxxer.hikari.HikariDataSource
import es.prog2425.students.app.StudentsApp
import es.prog2425.students.data.dao.AddressDAOH2
import es.prog2425.students.data.dao.StudentDAOH2
import es.prog2425.students.data.db.DataSourceFactory
import es.prog2425.students.data.db.Mode
import es.prog2425.students.service.AddressService
import es.prog2425.students.service.StudentTransactionService
import es.prog2425.students.service.StudentService
import es.prog2425.students.ui.Console

fun main() {
    // Creo la instancia que gestiona la entrada/salida con el usuario...
    val consola = Console()

    // Obtengo la fuente de datos que necesito...
    val dataSource = try {
        DataSourceFactory.getDataSource(Mode.HIKARI)
    } catch (e: IllegalStateException) {
        consola.mostrarError("Problemas al crear el DataSource: ${e.message}")
        return // Finaliza el programa porque no se puede interactuar con la base de datos
    }

    // Creo las instancias dao que gesionan el CRUD de estudiantes y direccinoes en la base de datos H2...
    val studentsDAO = StudentDAOH2(dataSource)
    val addressDAO = AddressDAOH2(dataSource)

    // Creo las instancias de los servicios que interactúan con las capas data (dao) y ui-app (StudentsManager)...
    val studentsService = StudentService(studentsDAO)
    val addreessService = AddressService(addressDAO)

    // Creo la instancia del servicio orquestador (gestor transaccional)
    val studentsManag = StudentTransactionService(studentsDAO, addressDAO)

    // Creo la instancia que gestiona el flujo principal del programa (menú, lógica de control)
    val app = StudentsApp(studentsService, addreessService, studentsManag, consola, dataSource)
    app.menu()

    // Buenas prácticas en general... cerrar el pool si se usa HikariCP evita fugas si se reestructura el código por ejemplo.
    if (dataSource is HikariDataSource) {
        dataSource.close()
    }
}