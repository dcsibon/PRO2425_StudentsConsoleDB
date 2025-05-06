package es.prog2425.students.app

import es.prog2425.students.service.IStudentService
import es.prog2425.students.ui.IConsoleUI

class StudentsManager(
    private val service: IStudentService,
    private val ui: IConsoleUI
) {
    private var terminarPrograma = false

    fun run() {
        do {
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

            val opcion = ui.leer("Elige una opción: ").toIntOrNull()
            when (opcion) {
                1 -> mostrarEstudiantes()
                2 -> agregarEstudiante()
                3 -> editarEstudiante()
                4 -> eliminarEstudiante()
                5 -> buscarPorId()
                6 -> salir()
                else -> ui.mostrarError("Opción no válida.")
            }

            if (opcion != 6) ui.pausar()
        } while (!terminarPrograma)
    }

    private fun mostrarEstudiantes() {
        try {
            val students = service.listAll()
            if (students.isEmpty()) {
                ui.mostrar("No hay estudiantes.")
            } else {
                students.forEach { ui.mostrar("ID: ${it.id} - Nombre: ${it.name}") }
            }
        } catch (e: IllegalStateException) {
            ui.mostrarError("Error de acceso a la base de datos: ${e.message}")
        } catch (e: Exception) {
            ui.mostrarError("Error inesperado: ${e.message}")
        }
    }

    private fun agregarEstudiante() {
        val name = ui.leer("Nombre del nuevo estudiante: ")
        try {
            service.addStudent(name)
            ui.mostrar("Estudiante añadido.")
        } catch (e: IllegalArgumentException) {
            ui.mostrarError("Nombre inválido: ${e.message}")
        } catch (e: IllegalStateException) {
            ui.mostrarError("Error de base de datos: ${e.message}")
        } catch (e: Exception) {
            ui.mostrarError("Error inesperado: ${e.message}")
        }
    }

    private fun editarEstudiante() {
        val id = ui.leer("ID del estudiante a editar: ").toIntOrNull()
        if (id != null) {
            val newName = ui.leer("Nuevo nombre: ")
            try {
                service.updateStudent(id, newName)
                ui.mostrar("Estudiante actualizado.")
            } catch (e: IllegalArgumentException) {
                ui.mostrarError("Datos inválidos: ${e.message}")
            } catch (e: IllegalStateException) {
                ui.mostrarError("Error de base de datos: ${e.message}")
            } catch (e: Exception) {
                ui.mostrarError("Error inesperado: ${e.message}")
            }
        } else {
            ui.mostrarError("ID inválido.")
        }
    }

    private fun eliminarEstudiante() {
        val id = ui.leer("ID del estudiante a eliminar: ").toIntOrNull()
        if (id != null) {
            try {
                service.deleteStudent(id)
                ui.mostrar("Estudiante eliminado.")
            } catch (e: IllegalArgumentException) {
                ui.mostrarError("ID inválido: ${e.message}")
            } catch (e: IllegalStateException) {
                ui.mostrarError("Error de base de datos: ${e.message}")
            } catch (e: Exception) {
                ui.mostrarError("Error inesperado: ${e.message}")
            }
        } else {
            ui.mostrarError("ID inválido.")
        }
    }

    private fun buscarPorId() {
        val id = ui.leer("Introduce el ID a buscar: ").toIntOrNull()
        if (id != null) {
            try {
                val student = service.getStudentById(id)
                if (student != null) {
                    ui.mostrar("ID: ${student.id} - Nombre: ${student.name}")
                } else {
                    ui.mostrar("No se encontró ningún estudiante con ese ID.")
                }
            } catch (e: IllegalArgumentException) {
                ui.mostrarError("ID inválido: ${e.message}")
            } catch (e: IllegalStateException) {
                ui.mostrarError("Error de base de datos: ${e.message}")
            } catch (e: Exception) {
                ui.mostrarError("Error inesperado: ${e.message}")
            }
        } else {
            ui.mostrarError("ID no válido.")
        }
    }

    private fun salir() {
        ui.mostrar("Saliendo...")
        terminarPrograma = true
    }
}