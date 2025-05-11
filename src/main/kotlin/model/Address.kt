package es.prog2425.students.model

import es.prog2425.students.model.table.Addresses
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Address(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Address>(Addresses)

    var street by Addresses.street
    var city by Addresses.city
    var student by Student referencedOn Addresses.student
}