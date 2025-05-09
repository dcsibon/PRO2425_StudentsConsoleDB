package es.prog2425.students.service

import java.sql.Connection

interface IStudentManagerService {
    fun deleteStudentWithAddresses(studentId: Int, conn: Connection)
}