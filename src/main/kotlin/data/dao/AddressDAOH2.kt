package es.prog2425.students.data.dao

import es.prog2425.students.model.Address
import java.sql.Connection
import javax.sql.DataSource

class AddressDAOH2(private val ds: DataSource) : IAddressDAO {

    override fun getAll(): List<Address> {
        val addresses = mutableListOf<Address>()
        ds.connection.use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeQuery("SELECT * FROM addresses").use { rs ->
                    while (rs.next()) {
                        addresses.add(
                            Address(
                                id = rs.getInt("id"),
                                street = rs.getString("street"),
                                city = rs.getString("city"),
                                studentId = rs.getInt("student_id")
                            )
                        )
                    }
                }
            }
        }
        return addresses
    }

    override fun getById(id: Int): Address? {
        ds.connection.use { conn ->
            conn.prepareStatement("SELECT * FROM addresses WHERE id = ?").use { stmt ->
                stmt.setInt(1, id)
                stmt.executeQuery().use { rs ->
                    return if (rs.next()) {
                        Address(
                            id = rs.getInt("id"),
                            street = rs.getString("street"),
                            city = rs.getString("city"),
                            studentId = rs.getInt("student_id")
                        )
                    } else null
                }
            }
        }
    }

    override fun getByStudentId(studentId: Int): List<Address> {
        ds.connection.use { conn ->
            return getByStudentId(studentId, conn)
        }
    }

    override fun add(address: Address) {
        ds.connection.use { conn ->
            conn.prepareStatement(
                "INSERT INTO addresses (street, city, student_id) VALUES (?, ?, ?)"
            ).use { stmt ->
                stmt.setString(1, address.street)
                stmt.setString(2, address.city)
                stmt.setInt(3, address.studentId)
                stmt.executeUpdate()
            }
        }
    }

    override fun update(address: Address) {
        ds.connection.use { conn ->
            conn.prepareStatement(
                "UPDATE addresses SET street = ?, city = ?, student_id = ? WHERE id = ?"
            ).use { stmt ->
                stmt.setString(1, address.street)
                stmt.setString(2, address.city)
                stmt.setInt(3, address.studentId)
                stmt.setInt(4, address.id)
                stmt.executeUpdate()
            }
        }
    }

    override fun delete(id: Int) {
        ds.connection.use { conn ->
            delete(id, conn)
        }
    }

    override fun getByStudentId(studentId: Int, conn: Connection): List<Address> {
        val addresses = mutableListOf<Address>()
        conn.prepareStatement("SELECT * FROM addresses WHERE student_id = ?").use { stmt ->
            stmt.setInt(1, studentId)
            stmt.executeQuery().use { rs ->
                while (rs.next()) {
                    addresses.add(
                        Address(
                            id = rs.getInt("id"),
                            street = rs.getString("street"),
                            city = rs.getString("city"),
                            studentId = rs.getInt("student_id")
                        )
                    )
                }
            }
        }
        return addresses
    }

    override fun delete(id: Int, conn: Connection) {
        conn.prepareStatement("DELETE FROM addresses WHERE id = ?").use { stmt ->
            stmt.setInt(1, id)
            stmt.executeUpdate()
        }
    }

}
