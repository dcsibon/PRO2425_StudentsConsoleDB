package es.prog2425.students.model.table

import org.jetbrains.exposed.dao.id.IntIdTable

object Students : IntIdTable("STUDENTS") {
    val name = varchar("NAME", 255)
}