package es.prog2425.students.ui

interface IConsoleUI {
    fun mostrarTexto(texto: String)
    fun leerTexto(prompt: String = ""): String
    fun mostrarError(mensaje: String)
    fun mostrarLineaVacia()
}