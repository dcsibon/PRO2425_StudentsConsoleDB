
# Conexión a la base de datos en Kotlin

En Kotlin (y Java), existen varias formas de gestionar una conexión a una base de datos utilizando JDBC. Las más comunes son:
   - DriverManager: método clásico y directo.
   - DataSource: interfaz más moderna y flexible, utilizada con o sin pool de conexiones.

Ambas formas permiten conectarse a motores como H2, PostgreSQL, MySQL, etc., pero tienen diferencias en escalabilidad, reutilización y eficiencia.

1. Usando DriverManager (forma clásica)

DriverManager es la forma tradicional de obtener una conexión JDBC. Cada vez que se invoca getConnection(...), se crea una nueva conexión. Es simple, pero no reutiliza conexiones.

Ejemplo completo:

DatabaseTienda.kt:

```kotlin
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object DatabaseTienda {

    private const val JDBC_URL = "jdbc:h2:file:./data/tiendadb"
    private const val USER = "sa"
    private const val PASSWORD = ""

    init {
        try {
            Class.forName("org.h2.Driver") // Opcional en JDBC 4.0+
        } catch (e: ClassNotFoundException) {
            throw IllegalStateException("No se encontró el driver H2", e)
        }
    }

    fun getConnection(): Connection {
        return try {
            DriverManager.getConnection(JDBC_URL, USER, PASSWORD)
        } catch (e: SQLException) {
            throw IllegalStateException("No se pudo establecer la conexión con la base de datos", e)
        }
    }

    fun closeConnection(conn: Connection?) {
        try {
            conn?.close()
        } catch (e: SQLException) {
            throw IllegalStateException("Error al cerrar la conexión", e)
        }
    }
}
```

El uso en el main:

```kotlin
fun main() {
    var conn: Connection? = null

    try {
        conn = DatabaseTienda.getConnection()
        val stmt = conn.prepareStatement("SELECT * FROM products")
        val rs = stmt.executeQuery()
        while (rs.next()) {
            println("ID: ${rs.getInt("id")}, Nombre: ${rs.getString("name")}")
        }
        rs.close()
        stmt.close()
    } catch (e: Exception) {
        println("Error: ${e.message}")
    } finally {
        DatabaseTienda.closeConnection(conn)
    }
}
```

---

2. Usando DataSource sin pool de conexiones

DataSource es una interfaz más moderna introducida en JDBC 2.0. Su implementación más simple es JdbcDataSource (como la de H2). Aunque no incluye un pool, es reutilizable y permite separar configuración de lógica.

Ejemplo completo (DataSourceTienda.kt):

```kotlin
import org.h2.jdbcx.JdbcDataSource
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

object DataSourceTienda {

    private const val URL = "jdbc:h2:file:./data/tiendadb"
    private const val USER = "sa"
    private const val PASSWORD = ""

    private val dataSource: JdbcDataSource = JdbcDataSource()

    init {
        try {
            Class.forName("org.h2.Driver") // Carga opcional
            dataSource.setURL(URL)
            dataSource.user = USER
            dataSource.password = PASSWORD
        } catch (e: ClassNotFoundException) {
            throw IllegalStateException("No se encontró el driver H2", e)
        }
    }

    fun getConnection(): Connection {
        return try {
            dataSource.connection
        } catch (e: SQLException) {
            throw IllegalStateException("No se pudo conectar", e)
        }
    }

    fun closeConnection(conn: Connection?) {
        try {
            conn?.close()
        } catch (e: SQLException) {
            throw IllegalStateException("Error al cerrar la conexión", e)
        }
    }
}
```

Uso:

```kotlin
fun main() {
    var conn: Connection? = null
    var stmt: PreparedStatement? = null
    var rs: ResultSet? = null

    try {
        conn = DataSourceTienda.getConnection()
        stmt = conn.prepareStatement("SELECT * FROM products")
        rs = stmt.executeQuery()
        while (rs.next()) {
            println("ID: ${rs.getInt("id")}, Nombre: ${rs.getString("name")}")
        }
    } catch (e: SQLException) {
        println("Error: ${e.message}")
    } finally {
        try { rs?.close() } catch (_: Exception) {}
        try { stmt?.close() } catch (_: Exception) {}
        DataSourceTienda.closeConnection(conn)
    }
}
```

---

3. Usando pool de conexiones con HikariCP

HikariCP es un pool de conexiones rápido, eficiente y recomendado. Permite tener múltiples conexiones abiertas reutilizables, reduciendo el coste de apertura/cierre.

Ejemplo simple:

```kotlin
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection

fun main() {
    val config = HikariConfig()
    config.jdbcUrl = "jdbc:h2:file:./data/tiendadb"
    config.username = "sa"
    config.password = ""
    config.maximumPoolSize = 10

    val dataSource = HikariDataSource(config)

    dataSource.connection.use { conn ->
        conn.prepareStatement("SELECT * FROM products").use { stmt ->
            stmt.executeQuery().use { rs ->
                while (rs.next()) {
                    println("ID: ${rs.getInt("id")}, Nombre: ${rs.getString("name")}")
                }
            }
        }
    }

    dataSource.close()
}
```

---

4. Usando un DataSourceFactory versátil (SIMPLE o HIKARI)

Ideal para aplicaciones que quieren elegir entre JdbcDataSource o HikariDataSource en función del entorno o configuración.

DataSourceType.kt

```kotlin
enum class DataSourceType {
    SIMPLE,
    HIKARI
}
```

DataSourceFactory.kt

```kotlin
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.h2.jdbcx.JdbcDataSource
import javax.sql.DataSource

object DataSourceFactory {

    private const val JDBC_URL = "jdbc:h2:file:./data/tiendadb;AUTO_SERVER=TRUE"
    private const val USER = "sa"
    private const val PASSWORD = ""
    private const val DRIVER = "org.h2.Driver"
    private const val MAX_POOL_SIZE = 10

    fun create(type: DataSourceType): DataSource {
        return when (type) {
            DataSourceType.SIMPLE -> {
                val ds = JdbcDataSource()
                ds.setURL(JDBC_URL)
                ds.user = USER
                ds.password = PASSWORD
                ds
            }
            DataSourceType.HIKARI -> {
                val config = HikariConfig()
                config.jdbcUrl = JDBC_URL
                config.username = USER
                config.password = PASSWORD
                config.driverClassName = DRIVER
                config.maximumPoolSize = MAX_POOL_SIZE
                HikariDataSource(config)
            }
        }
    }
}
```

Uso en Main.kt

```kotlin
fun main() {
    val dataSource = DataSourceFactory.create(DataSourceType.HIKARI)

    dataSource.connection.use { conn ->
        conn.prepareStatement("SELECT * FROM products").use { stmt ->
            stmt.executeQuery().use { rs ->
                while (rs.next()) {
                    println("ID: ${rs.getInt("id")}, Nombre: ${rs.getString("name")}")
                }
            }
        }
    }

    if (dataSource is HikariDataSource) {
        dataSource.close()
    }
}
```

---

Conclusión
   - DriverManager es válido para prácticas o pruebas.
   - DataSource básico separa configuración y es más reutilizable.
   - HikariCP es la opción óptima para producción o aplicaciones concurrentes.
   - DataSourceFactory permite cambiar fácilmente de motor, configuración o entorno.
