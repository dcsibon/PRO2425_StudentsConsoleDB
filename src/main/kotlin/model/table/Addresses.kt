package es.prog2425.students.model.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object Addresses : IntIdTable("ADDRESSES") {
    val street = varchar("STREET", 255)
    val city = varchar("CITY", 255)
    val student = reference("STUDENT_ID", Students, onDelete = ReferenceOption.CASCADE)
        .index() // Esto añade el índice sobre STUDENT_ID
}