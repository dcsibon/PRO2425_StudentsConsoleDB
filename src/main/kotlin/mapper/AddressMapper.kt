package es.prog2425.students.mapper

import es.prog2425.students.data.dto.AddressDTO
import es.prog2425.students.model.Address

fun Address.toDTO(): AddressDTO =
    AddressDTO(id.value, street, city, student.id.value)