package es.prog2425.students.service

import es.prog2425.students.data.dao.IAddressDAO
import es.prog2425.students.data.dao.IStudentDAO
import java.sql.Connection

class StudentManagerService(
    private val studentDAO: IStudentDAO,
    private val addressDAO: IAddressDAO
) : IStudentManagerService {

    override fun deleteStudentWithAddresses(studentId: Int, conn: Connection) {
        require(studentId > 0) { "ID de estudiante inválido." }

        val addresses = addressDAO.getByStudentId(studentId, conn)
        for (address in addresses) {
            addressDAO.delete(address.id, conn)
        }

        studentDAO.delete(studentId, conn)
    }
}
