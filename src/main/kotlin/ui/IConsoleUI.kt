package es.prog2425.students.ui

interface IConsoleUI {
    fun mostrar(texto: String, saltoLinea: Boolean = true)
    fun leer(prompt: String = "", saltoLinea: Boolean = false): String
    fun mostrarError(mensaje: String, saltoLinea: Boolean = true)
    fun saltoLinea()
    fun limpiar(lineas: Int = 20)
    fun pausar(msj: String = "Pulsa ENTER para continuar...")
}