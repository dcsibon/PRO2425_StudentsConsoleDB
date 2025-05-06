-- Elimina la tabla si ya existe (opcional en desarrollo o pruebas)
DROP TABLE IF EXISTS students;

-- Crea la tabla
CREATE TABLE students (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL
);

-- Insertar algunos datos de prueba
INSERT INTO students (name) VALUES ('Ana'), ('Luis'), ('Sofía'), ('Carlos'), ('María');