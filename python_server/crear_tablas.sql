-- Crear tablas adicionales para Mexicare

CREATE TABLE IF NOT EXISTS catalogo (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    categoria VARCHAR(100),
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS inventario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    organizacion_id INT NOT NULL,
    catalogo_id INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 0,
    fecha_vencimiento DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (organizacion_id) REFERENCES organizaciones(id),
    FOREIGN KEY (catalogo_id) REFERENCES catalogo(id)
);

CREATE TABLE IF NOT EXISTS donaciones (
    id INT PRIMARY KEY AUTO_INCREMENT,
    organizacion_id INT NOT NULL,
    catalogo_id INT NOT NULL,
    cantidad INT NOT NULL,
    donante VARCHAR(200),
    fecha_donacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (organizacion_id) REFERENCES organizaciones(id),
    FOREIGN KEY (catalogo_id) REFERENCES catalogo(id)
);

CREATE TABLE IF NOT EXISTS entregas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    organizacion_id INT NOT NULL,
    catalogo_id INT NOT NULL,
    cantidad INT NOT NULL,
    beneficiario VARCHAR(200),
    fecha_entrega TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (organizacion_id) REFERENCES organizaciones(id),
    FOREIGN KEY (catalogo_id) REFERENCES catalogo(id)
);

-- Datos de prueba para catálogo
INSERT INTO catalogo (nombre, descripcion, categoria, activo) VALUES 
('Paracetamol 500mg', 'Analgésico y antipirético', 'Analgésicos', 1),
('Ibuprofeno 400mg', 'Antiinflamatorio no esteroideo', 'Antiinflamatorios', 1),
('Amoxicilina 500mg', 'Antibiótico de amplio espectro', 'Antibióticos', 1),
('Omeprazol 20mg', 'Inhibidor de la bomba de protones', 'Gastroenterología', 1),
('Loratadina 10mg', 'Antihistamínico', 'Alergias', 1);

-- Datos de prueba para inventario
INSERT INTO inventario (organizacion_id, catalogo_id, cantidad, fecha_vencimiento) VALUES 
(1, 1, 100, '2025-12-31'),
(1, 2, 50, '2025-06-30'),
(2, 1, 75, '2025-11-15'),
(2, 3, 30, '2025-08-20');

-- Datos de prueba para donaciones
INSERT INTO donaciones (organizacion_id, catalogo_id, cantidad, donante) VALUES 
(1, 1, 50, 'Farmacia San Pablo'),
(2, 2, 25, 'Hospital Privado'),
(1, 3, 20, 'Donante Anónimo');

-- Datos de prueba para entregas
INSERT INTO entregas (organizacion_id, catalogo_id, cantidad, beneficiario) VALUES 
(1, 1, 10, 'María González'),
(2, 2, 5, 'Juan Pérez'),
(1, 3, 8, 'Ana López');