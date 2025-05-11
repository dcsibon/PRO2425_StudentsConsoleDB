package es.prog2425.students.data.db

import es.prog2425.students.model.table.Addresses
import es.prog2425.students.model.table.Students
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object SchemaInitializer {
    fun init() {
        Database.connect(
            url = "jdbc:h2:./data/studentdb",
            driver = "org.h2.Driver",
            user = "sa",
            password = ""
        )

        transaction {
            // Crea las tablas y columnas si no existen (no borra nada)
            SchemaUtils.createMissingTablesAndColumns(Students, Addresses)

            // Solo insertar datos si no existen (ej. si la tabla está vacía)
            if (Students.selectAll().empty()) {
                val anaId = Students.insertAndGetId {
                    it[name] = "Ana"
                }.value

                val luisId = Students.insertAndGetId {
                    it[name] = "Luis"
                }.value

                Students.insert {
                    it[name] = "Sofía"
                }

                Students.insert {
                    it[name] = "Carlos"
                }

                Students.insert {
                    it[name] = "María"
                }

                Addresses.insert {
                    it[street] = "Ronda de Estero 5, 1A"
                    it[city] = "San Fernando"
                    it[student] = anaId
                }

                Addresses.insert {
                    it[street] = "Plaza de España, 2, Bajo C"
                    it[city] = "Cádiz"
                    it[student] = anaId
                }

                Addresses.insert {
                    it[street] = "Avenida de la Sanidad, 22, 4D"
                    it[city] = "Cádiz"
                    it[student] = luisId
                }
            }
        }
    }
}