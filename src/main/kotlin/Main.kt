package es.prog2425.students

import com.zaxxer.hikari.HikariDataSource
import es.prog2425.students.app.StudentsManager
import es.prog2425.students.data.dao.StudentDAOH2
import es.prog2425.students.data.db.DataSourceFactory
import es.prog2425.students.service.StudentService
import es.prog2425.students.ui.Console
import java.sql.SQLException

fun main() {
    // Creo la instancia que gestiona la entrada/salida con el usuario...
    val consola = Console()

    // Obtengo la fuente de información que necesito...
    val dataSource = try {
        DataSourceFactory.getDataSource(DataSourceFactory.Mode.HIKARI)
    } catch (e: IllegalStateException) {
        consola.mostrarError("Problemas al crear el DataSource: ${e.message}")
        return // Se acaba el programa porque no puedo interactuar con la base de datos
    }

    // Creo la instancia del dao que gesiona el CRUD de estudiantes en la base de datos H2...
    val studentsDAO = StudentDAOH2(dataSource)

    // Creo la instancia del servicio que interactúa con las capas data (dao) y ui-app (StudentsManager)...
    val studentsService = StudentService(studentsDAO)

    // Creo la instancia que gestiona el flujo del programa...
    val app = StudentsManager(studentsService, consola)
    app.mostrarMenu()

    // Buenas prácticas en general... evita fugas si se reestructura el código por ejemplo.
    if (dataSource is HikariDataSource) {
        dataSource.close()
    }
}