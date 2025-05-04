plugins {
    kotlin("jvm") version "2.0.20"
}

group = "es.prog2425.students"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    // Driver y base de datos H2
    implementation("com.h2database:h2:2.2.224")

    // HikariCP para pool de conexiones
    implementation("com.zaxxer:HikariCP:5.1.0")

    // implementación de SLF4J
    implementation("org.slf4j:slf4j-simple:2.0.12")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}