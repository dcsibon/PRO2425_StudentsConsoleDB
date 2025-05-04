package es.prog2425.students.ui

class Console : IConsoleUI {
    override fun mostrarTexto(texto: String) {
        println(texto)
    }

    override fun leerTexto(prompt: String): String {
        if (prompt.isNotBlank()) print(prompt)
        return readln()
    }

    override fun mostrarError(mensaje: String) {
        println(mensaje)
    }

    override fun mostrarLineaVacia() {
        println()
    }
}