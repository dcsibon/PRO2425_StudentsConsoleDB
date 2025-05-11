package es.prog2425.students.service

import es.prog2425.students.data.dto.AddressDTO

interface IAddressService {
    fun getAll(): List<AddressDTO>
    fun getById(id: Int): AddressDTO?
    fun getByStudentId(studentId: Int): List<AddressDTO>
    fun add(address: AddressDTO): AddressDTO
    fun update(address: AddressDTO): Boolean
    fun delete(id: Int): Boolean
}
