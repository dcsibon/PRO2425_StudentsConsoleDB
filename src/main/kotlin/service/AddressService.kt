package es.prog2425.students.service

import es.prog2425.students.data.dao.IAddressDAO
import es.prog2425.students.model.Address

class AddressService(private val addressDAO: IAddressDAO) : IAddressService {

    override fun getAll(): List<Address> = addressDAO.getAll()

    override fun getById(id: Int): Address? {
        require(id > 0) { "ID inválido." }
        return addressDAO.getById(id)
    }

    override fun getByStudentId(studentId: Int): List<Address> {
        require(studentId > 0) { "ID de estudiante inválido." }
        return addressDAO.getByStudentId(studentId)
    }

    override fun add(address: Address) {
        require(address.street.isNotBlank()) { "La calle no puede estar vacía." }
        require(address.city.isNotBlank()) { "La ciudad no puede estar vacía." }
        require(address.studentId > 0) { "ID de estudiante inválido." }

        addressDAO.add(
            address.copy(
                street = address.street.trim(),
                city = address.city.trim()
            )
        )
    }

    override fun update(address: Address) {
        require(address.id > 0) { "ID inválido para actualización." }
        require(address.street.isNotBlank()) { "La calle no puede estar vacía." }
        require(address.city.isNotBlank()) { "La ciudad no puede estar vacía." }
        require(address.studentId > 0) { "ID de estudiante inválido." }

        addressDAO.update(
            address.copy(
                street = address.street.trim(),
                city = address.city.trim()
            )
        )
    }

    override fun delete(id: Int) {
        require(id > 0) { "ID inválido." }
        addressDAO.delete(id)
    }
}
