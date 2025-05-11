package es.prog2425.students.data.dao

import es.prog2425.students.data.dto.AddressDTO
import es.prog2425.students.model.Address
import es.prog2425.students.model.Student
import org.jetbrains.exposed.sql.transactions.transaction

class AddressDAOExposed : IAddressDAO {

    override fun getAll(): List<Address> = transaction {
        Address.all().toList()
    }

    override fun getById(id: Int): Address? = transaction {
        Address.findById(id)
    }

    override fun getByStudentId(studentId: Int): List<Address> = transaction {
        Address.find { es.prog2425.students.model.table.Addresses.student eq studentId }.toList()
    }

    override fun add(address: AddressDTO, student: Student): Address = transaction {
        Address.new {
            street = address.street
            city = address.city
            this.student = student
        }
    }

    override fun update(address: AddressDTO, student: Student): Boolean = transaction {
        val entity = Address.findById(address.id) ?: return@transaction false
        entity.street = address.street
        entity.city = address.city
        entity.student = student
        true
    }

    override fun delete(id: Int): Boolean = transaction {
        val entity = Address.findById(id) ?: return@transaction false
        entity.delete()
        true
    }
}