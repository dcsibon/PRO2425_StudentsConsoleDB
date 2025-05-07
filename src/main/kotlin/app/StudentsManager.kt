package es.prog2425.students.app

import es.prog2425.students.service.IStudentService
import es.prog2425.students.ui.IConsoleUI
import java.sql.SQLException

class StudentsManager(
    private val service: IStudentService,
    private val ui: IConsoleUI
) {
    private var running = true

    fun mostrarMenu() {
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
                6. Salir
                """.trimIndent()
            )

            ui.saltoLinea()

            when (ui.leer("Elige una opción: ")) {
                "1" -> mostrarEstudiantes()
                "2" -> agregarEstudiante()
                "3" -> editarEstudiante()
                "4" -> eliminarEstudiante()
                "5" -> buscarPorId()
                "6" -> salir()
                else -> ui.mostrarError("Opción no válida.")
            }

            ui.pausar()
        }
    }

    private fun mostrarEstudiantes() {
        try {
            val students = service.listAll()
            if (students.isEmpty()) {
                ui.mostrar("No hay estudiantes.")
            } else {
                students.forEach { ui.mostrar("ID: ${it.id} - Nombre: ${it.name}") }
            }
        } catch (e: SQLException) {
            ui.mostrarError("Problemas con la BDD: ${e.message}")
        } catch (e: Exception) {
            ui.mostrarError("Se produjo un error: ${e.message}")
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

    private fun agregarEstudiante() {
        val name = ui.leer("Nombre del nuevo estudiante: ")
        ejecutarOperacion {
            service.addStudent(name)
            ui.mostrar("Estudiante añadido.")
        }
    }

    private fun editarEstudiante() {
        val id = ui.leer("ID del estudiante a editar: ").toIntOrNull()
        if (id != null) {
            val newName = ui.leer("Nuevo nombre: ")
            ejecutarOperacion {
                service.updateStudent(id, newName)
                ui.mostrar("Estudiante actualizado.")
            }
        } else {
            ui.mostrarError("Argumentos no válidos: El ID introducido no es un número entero válido.")
        }
    }

    private fun eliminarEstudiante() {
        val id = ui.leer("ID del estudiante a eliminar: ").toIntOrNull()
        if (id != null) {
            ejecutarOperacion {
                service.deleteStudent(id)
                ui.mostrar("Estudiante eliminado.")
            }
        } else {
            ui.mostrarError("Argumentos no válidos: El ID introducido no es un número entero válido.")
        }
    }

    private fun buscarPorId() {
        val id = ui.leer("Introduce el ID a buscar: ").toIntOrNull()
        if (id != null) {
            ejecutarOperacion {
                val student = service.getStudentById(id)
                if (student != null) {
                    ui.mostrar("ID: ${student.id} - Nombre: ${student.name}")
                } else {
                    ui.mostrar("No se encontró ningún estudiante con ese ID.")
                }
            }
        } else {
            ui.mostrarError("Argumentos no válidos: El ID introducido no es un número entero válido.")
        }
    }

    private fun salir() {
        ui.mostrar("Saliendo...")
        running = false
    }
}