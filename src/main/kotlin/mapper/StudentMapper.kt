package es.prog2425.students.mapper

import es.prog2425.students.data.dto.StudentDTO
import es.prog2425.students.model.Student

fun Student.toDTO(): StudentDTO = StudentDTO(id.value, name)