package es.prog2425.students.app

import es.prog2425.students.data.dto.AddressDTO
import es.prog2425.students.model.Address
import es.prog2425.students.service.IAddressService
import es.prog2425.students.service.IStudentTransactionService
import es.prog2425.students.service.IStudentService
import es.prog2425.students.ui.IConsoleUI
import java.sql.SQLException
import javax.sql.DataSource

class StudentsApp(
    private val studentService: IStudentService,
    private val addressService: IAddressService,
    private val studentTransactionService: IStudentTransactionService,
    private val ui: IConsoleUI
) {
    private var running = true

    fun menu() {
        while (running) {
            ui.limpiar()
            ui.mostrar(
                """
                === MENÚ ===
                1. Mostrar estudiantes
                2. Agregar estudiante
                3. Editar estudiante
                4. Eliminar estudiante
                5. Buscar estudiante por ID
                6. Agregar dirección
                7. Modificar dirección
                8. Eliminar dirección
                9. Mostrar direcciones de un estudiante
                10. Salir
                """.trimIndent()
            )

            ui.saltoLinea()

            when (ui.leer("Elige una opción: ")) {
                "1" -> mostrarEstudiantes()
                "2" -> agregarEstudiante()
                "3" -> editarEstudiante()
                "4" -> eliminarEstudiante()
                "5" -> buscarPorId()
                "6" -> agregarDireccion()
                "7" -> modificarDireccion()
                "8" -> eliminarDireccion()
                "9" -> mostrarDireccionesDeEstudiante()
                "10" -> salir()
                else -> ui.mostrarError("Opción no válida.")
            }

            ui.pausar()
        }
    }

    private fun ejecutarOperacion(bloque: () -> Unit) {
        try {
            bloque()
        } catch (e: IllegalArgumentException) {
            ui.mostrarError("Argumentos no válidos: ${e.message}")
        } catch (e: SQLException) {
            ui.mostrarError("Problemas con la BDD: ${e.message}")
        } catch (e: Exception) {
            ui.mostrarError("Se produjo un error: ${e.message}")
        }
    }

    private fun ejecutarOperacionConId(pedirId: String = "ID del estudiante: ", bloque: (Int) -> Unit) {
        val id = ui.leer(pedirId).toIntOrNull()
        if (id != null) {
            ejecutarOperacion { bloque(id) }
        } else {
            ui.mostrarError("Argumentos no válidos: El ID introducido no es un número entero válido.")
        }
    }

    private fun mostrarEstudiantes() {
        ejecutarOperacion {
            val students = studentService.listAll()
            if (students.isEmpty()) {
                ui.mostrar("No hay estudiantes.")
            } else {
                students.forEach { ui.mostrar("ID: ${it.id} - Nombre: ${it.name}") }
            }
        }
    }

    private fun agregarEstudiante() {
        val name = ui.leer("Nombre del nuevo estudiante: ")
        ejecutarOperacion {
            studentService.addStudent(name)
            ui.mostrar("Estudiante añadido.")
        }
    }

    private fun editarEstudiante() {
        ejecutarOperacionConId("ID del estudiante a editar: ") { id ->
            val newName = ui.leer("Nuevo nombre: ")
            studentService.updateStudent(id, newName)
            ui.mostrar("Estudiante actualizado.")
        }
    }

    private fun eliminarEstudiante() {
        ejecutarOperacionConId("ID del estudiante a eliminar: ") { id ->
            studentTransactionService.deleteStudentWithAddresses(id)
            ui.mostrar("Estudiante y sus direcciones eliminados correctamente.")
        }
    }

    private fun buscarPorId() {
        ejecutarOperacionConId("Introduce el ID a buscar: ") { id ->
            val student = studentService.getStudentById(id)
            if (student != null) {
                ui.mostrar("ID: ${student.id} - Nombre: ${student.name}")
            } else {
                ui.mostrar("No se encontró ningún estudiante con ese ID.")
            }
        }
    }

    private fun agregarDireccion() {
        val street = ui.leer("Calle: ")
        val city = ui.leer("Ciudad: ")
        val studentId = ui.leer("ID del estudiante: ").toIntOrNull()

        if (studentId != null) {
            ejecutarOperacion {
                addressService.add(AddressDTO(id = 0, street = street, city = city, studentId = studentId))
                ui.mostrar("Dirección añadida.")
            }
        } else {
            ui.mostrarError("El ID del estudiante no es válido.")
        }
    }

    private fun modificarDireccion() {
        val id = ui.leer("ID de la dirección a modificar: ").toIntOrNull()
        if (id != null) {
            val street = ui.leer("Nueva calle: ")
            val city = ui.leer("Nueva ciudad: ")
            val studentId = ui.leer("Nuevo ID de estudiante: ").toIntOrNull()

            if (studentId != null) {
                ejecutarOperacion {
                    addressService.update(AddressDTO(id = id, street = street, city = city, studentId = studentId))
                    ui.mostrar("Dirección actualizada.")
                }
            } else {
                ui.mostrarError("El ID del estudiante no es válido.")
            }
        } else {
            ui.mostrarError("El ID de la dirección no es válido.")
        }
    }

    private fun eliminarDireccion() {
        ejecutarOperacionConId("ID de la dirección a eliminar:") { id ->
            addressService.delete(id)
            ui.mostrar("Dirección eliminada.")
        }
    }

    private fun mostrarDireccionesDeEstudiante() {
        ejecutarOperacionConId { id ->
            val direcciones = addressService.getByStudentId(id)
            if (direcciones.isEmpty()) {
                ui.mostrar("Este estudiante no tiene direcciones registradas.")
            } else {
                direcciones.forEach { dir ->
                    ui.mostrar("ID: ${dir.id} - ${dir.street}, ${dir.city}")
                }
            }
        }
    }

    private fun salir() {
        ui.mostrar("Saliendo...")
        running = false
    }
}