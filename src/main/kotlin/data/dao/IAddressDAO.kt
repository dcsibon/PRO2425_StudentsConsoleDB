package es.prog2425.students.data.dao

import es.prog2425.students.model.Address
import java.sql.Connection

interface IAddressDAO {
    fun getAll(): List<Address>
    fun getById(id: Int): Address?
    fun getByStudentId(studentId: Int): List<Address>
    fun add(address: Address)
    fun update(address: Address)
    fun delete(id: Int)

    fun getByStudentId(studentId: Int, conn: Connection): List<Address>
    fun delete(id: Int, conn: Connection)
}
