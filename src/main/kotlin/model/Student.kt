package es.prog2425.students.model

import es.prog2425.students.model.table.Addresses
import es.prog2425.students.model.table.Students
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Student(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Student>(Students)

    var name by Students.name
    val addresses by Address referrersOn Addresses.student
}
