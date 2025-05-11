package es.prog2425.students.service

import es.prog2425.students.data.dao.IAddressDAO
import es.prog2425.students.data.dao.IStudentDAO
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

class StudentTransactionService(private val studentDAO: IStudentDAO,private val addressDAO: IAddressDAO) : IStudentTransactionService {

    override fun deleteStudentWithAddresses(studentId: Int) {
        transaction {
            val addresses = addressDAO.getByStudentId(studentId)
            addresses.forEach { it.delete() }
            studentDAO.delete(studentId)
        }
    }

}
