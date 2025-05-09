-- Activar integridad referencial (por seguridad, aunque ya está activa por defecto)
SET REFERENTIAL_INTEGRITY TRUE;

-- Eliminar tablas si existen (en orden correcto por dependencias)
DROP TABLE IF EXISTS addresses;
DROP TABLE IF EXISTS students;

-- Crear tabla principal
CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Crear tabla relacionada con clave foránea y ON DELETE CASCADE (lo explicaré en clase)
-- Con esta configuración estamos delegando la gestión de la eliminación de registros
-- de direcciones relacionados con un estudiante desde la base de datos... por lo tanto
-- simplificaría de nuevo la eliminación del estudiante sin tener que realizar una transacción.
CREATE TABLE addresses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    street VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    student_id INT,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);

-- Insertar algunos datos de prueba
INSERT INTO students (name) VALUES ('Ana'), ('Luis'), ('Sofía'), ('Carlos'), ('María');

INSERT INTO addresses (street, city, student_id) VALUES
    ('Ronda de Estero 5, 1A', 'San Fernando', 1),
    ('Plaza de España, 2, Bajo C', 'Cádiz', 1),
    ('Avenida de la Sanidad, 22, 4D', 'Cádiz', 2);