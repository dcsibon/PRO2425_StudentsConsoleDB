package es.prog2425.students.service

import es.prog2425.students.model.Address

interface IAddressService {
    fun getAll(): List<Address>
    fun getById(id: Int): Address?
    fun getByStudentId(studentId: Int): List<Address>
    fun add(address: Address)
    fun update(address: Address)
    fun delete(id: Int)
}
