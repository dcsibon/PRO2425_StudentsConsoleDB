package es.prog2425.students.data.dao

import es.prog2425.students.data.dto.AddressDTO
import es.prog2425.students.model.Address
import es.prog2425.students.model.Student

interface IAddressDAO {
    fun getAll(): List<Address>
    fun getById(id: Int): Address?
    fun getByStudentId(studentId: Int): List<Address>
    fun add(address: AddressDTO, student: Student): Address
    fun update(address: AddressDTO, student: Student): Boolean
    fun delete(id: Int): Boolean
}
