package es.prog2425.students.service

import java.sql.Connection

interface IStudentTransactionService {
    fun deleteStudentWithAddresses(studentId: Int, conn: Connection)
}