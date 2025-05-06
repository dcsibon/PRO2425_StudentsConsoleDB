package es.prog2425.students.data.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.h2.jdbcx.JdbcDataSource
import javax.sql.DataSource

object DataSourceFactory {

    private const val JDBC_URL = "jdbc:h2:./data/studentdb"
    private const val USER = "sa"
    private const val PASSWORD = ""

    enum class Mode {
        HIKARI, SIMPLE
    }

    fun getDataSource(mode: Mode = Mode.HIKARI): DataSource {
        return when (mode) {
            Mode.HIKARI -> {
                val config = HikariConfig().apply {
                    jdbcUrl = JDBC_URL
                    username = USER
                    password = PASSWORD
                    driverClassName = "org.h2.Driver"
                    maximumPoolSize = 10
                }
                HikariDataSource(config)
            }
            Mode.SIMPLE -> {
                JdbcDataSource().apply {
                    setURL(JDBC_URL)
                    user = USER
                    password = PASSWORD
                }
            }
        }
    }
}