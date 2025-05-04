package es.prog2425.students.app

import es.prog2425.students.service.IStudentService
import es.prog2425.students.ui.IConsoleUI

class StudentsManager(
    private val service: IStudentService,
    private val ui: IConsoleUI
) {
    private var running = true

    fun run() {
        while (running) {
            ui.mostrarTexto(
                """
                === MENÚ ===
                1. Mostrar estudiantes
                2. Agregar estudiante
                3. Editar estudiante
                4. Eliminar estudiante
                5. Salir
                """.trimIndent()
            )

            when (ui.leerTexto("Elige una opción: ")) {
                "1" -> mostrarEstudiantes()
                "2" -> agregarEstudiante()
                "3" -> editarEstudiante()
                "4" -> eliminarEstudiante()
                "5" -> salir()
                else -> ui.mostrarError("Opción no válida.")
            }

            ui.mostrarLineaVacia()
        }
    }

    private fun mostrarEstudiantes() {
        val students = service.listAll()
        if (students.isEmpty()) {
            ui.mostrarTexto("No hay estudiantes.")
        } else {
            students.forEach { ui.mostrarTexto("ID: ${it.id} - Nombre: ${it.name}") }
        }
    }

    private fun agregarEstudiante() {
        val name = ui.leerTexto("Nombre del nuevo estudiante: ")
        try {
            service.addStudent(name)
            ui.mostrarTexto("Estudiante añadido.")
        } catch (e: IllegalArgumentException) {
            ui.mostrarError(e.message ?: "Nombre inválido.")
        }
    }

    private fun editarEstudiante() {
        val id = ui.leerTexto("ID del estudiante a editar: ").toIntOrNull()
        if (id != null) {
            val newName = ui.leerTexto("Nuevo nombre: ")
            try {
                service.updateStudent(id, newName)
                ui.mostrarTexto("Estudiante actualizado.")
            } catch (e: IllegalArgumentException) {
                ui.mostrarError(e.message ?: "Error al actualizar.")
            }
        } else {
            ui.mostrarError("ID inválido.")
        }
    }

    private fun eliminarEstudiante() {
        val id = ui.leerTexto("ID del estudiante a eliminar: ").toIntOrNull()
        if (id != null) {
            try {
                service.deleteStudent(id)
                ui.mostrarTexto("Estudiante eliminado.")
            } catch (e: IllegalArgumentException) {
                ui.mostrarError(e.message ?: "Error al eliminar.")
            }
        } else {
            ui.mostrarError("ID inválido.")
        }
    }

    private fun salir() {
        ui.mostrarTexto("Saliendo...")
        running = false
    }
}