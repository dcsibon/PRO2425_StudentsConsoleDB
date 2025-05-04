package es.prog2425.students.data.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.h2.jdbcx.JdbcDataSource
import javax.sql.DataSource

object DataSourceFactory {

    enum class Mode {
        HIKARI, SIMPLE
    }

    fun getDataSource(mode: Mode = Mode.HIKARI): DataSource {
        return when (mode) {
            Mode.HIKARI -> {
                val config = HikariConfig().apply {
                    jdbcUrl = "jdbc:h2:./data/studentdb"
                    username = "sa"
                    password = ""
                    driverClassName = "org.h2.Driver"
                    maximumPoolSize = 5
                }
                HikariDataSource(config)
            }
            Mode.SIMPLE -> {
                JdbcDataSource().apply {
                    setURL("jdbc:h2:./data/studentdb")
                    user = "sa"
                    password = ""
                }
            }
        }
    }
}