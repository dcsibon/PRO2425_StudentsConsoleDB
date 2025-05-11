package es.prog2425.students.service

import es.prog2425.students.data.dao.IAddressDAO
import es.prog2425.students.data.dto.AddressDTO
import es.prog2425.students.mapper.toDTO
import es.prog2425.students.model.Student
import org.jetbrains.exposed.sql.transactions.transaction

class AddressService(private val dao: IAddressDAO) : IAddressService {

    override fun getAll(): List<AddressDTO> = dao.getAll().map { it.toDTO() }

    override fun getById(id: Int): AddressDTO? {
        require(id > 0) { "ID inválido." }
        return dao.getById(id)?.toDTO()
    }

    override fun getByStudentId(studentId: Int): List<AddressDTO> {
        require(studentId > 0) { "ID de estudiante inválido." }
        return transaction {
            dao.getByStudentId(studentId).map { it.toDTO() }
        }
    }

    override fun add(address: AddressDTO): AddressDTO {
        require(address.street.isNotBlank()) { "La calle no puede estar vacía." }
        require(address.city.isNotBlank()) { "La ciudad no puede estar vacía." }
        require(address.studentId > 0) { "ID de estudiante inválido." }

        return transaction {
            val student = Student.findById(address.studentId)
                ?: throw IllegalArgumentException("Estudiante no encontrado con ID ${address.studentId}")
            dao.add(address, student).toDTO()
        }
    }

    override fun update(address: AddressDTO): Boolean {
        require(address.id > 0) { "ID inválido para actualización." }
        require(address.street.isNotBlank()) { "La calle no puede estar vacía." }
        require(address.city.isNotBlank()) { "La ciudad no puede estar vacía." }
        require(address.studentId > 0) { "ID de estudiante inválido." }

        return transaction {
            val student = Student.findById(address.studentId)
                ?: throw IllegalArgumentException("Estudiante no encontrado con ID ${address.studentId}")
            dao.update(address, student)
        }
    }

    override fun delete(id: Int): Boolean {
        require(id > 0) { "ID inválido." }
        return dao.delete(id)
    }
}
