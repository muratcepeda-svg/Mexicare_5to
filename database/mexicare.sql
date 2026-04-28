-- ============================================
-- BASE DE DATOS MEXICARE - CREACIÓN Y DATOS
-- ============================================

-- Crear base de datos
CREATE DATABASE IF NOT EXISTS proyecto_quinto;
USE proyecto_quinto;

-- ============================================
-- TABLA: USUARIOS ADMIN
-- ============================================
CREATE TABLE usuarios_admin (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    clave VARCHAR(255) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_usuario (usuario)
);

-- Insertar administrador por defecto
INSERT INTO usuarios_admin (usuario, clave, activo) VALUES
('admin', 'admin123', TRUE);

-- ============================================
-- TABLA: CATÁLOGO DE MEDICAMENTOS
-- ============================================
CREATE TABLE catalogo (
    id INT PRIMARY KEY AUTO_INCREMENT,
    sustancia_activa VARCHAR(100) NOT NULL,
    nombre_comercial VARCHAR(100) NOT NULL,
    presentacion VARCHAR(100) NOT NULL,
    descripcion TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_sustancia (sustancia_activa),
    INDEX idx_nombre (nombre_comercial)
);

-- Insertar medicamentos del catálogo
INSERT INTO catalogo (sustancia_activa, nombre_comercial, presentacion, descripcion) VALUES
('Paracetamol', 'Tempra', 'Tabletas 500mg', 'Analgésico y antipirético'),
('Paracetamol', 'Mexax', 'Tabletas 500mg', 'Analgésico y antipirético'),
('Ibuprofeno', 'Advil', 'Tabletas 400mg', 'Antiinflamatorio no esteroideo'),
('Ibuprofeno', 'Motrin', 'Tabletas 400mg', 'Antiinflamatorio no esteroideo'),
('Amoxicilina', 'Amoxil', 'Cápsulas 500mg', 'Antibiótico de amplio espectro'),
('Amoxicilina', 'Sumamed', 'Cápsulas 500mg', 'Antibiótico de amplio espectro'),
('Omeprazol', 'Losec', 'Cápsulas 20mg', 'Inhibidor de la bomba de protones'),
('Omeprazol', 'Omepraz', 'Cápsulas 20mg', 'Inhibidor de la bomba de protones'),
('Metformina', 'Glucophage', 'Tabletas 850mg', 'Antidiabético oral'),
('Metformina', 'Metformina', 'Tabletas 850mg', 'Antidiabético oral'),
('Losartán', 'Cozaar', 'Tabletas 50mg', 'Antihipertensivo'),
('Losartán', 'Losartan', 'Tabletas 50mg', 'Antihipertensivo'),
('Albuterol', 'Ventolin', 'Inhalador 100mcg', 'Broncodilatador'),
('Dipirona', 'Metamizol', 'Tabletas 500mg', 'Analgésico y antipirético'),
('Tramadol', 'Tramal', 'Cápsulas 50mg', 'Analgésico opioide'),
('Azitromicina', 'Zithromax', 'Cápsulas 500mg', 'Antibiótico macrólido'),
('Cetirizina', 'Zyrtec', 'Tabletas 10mg', 'Antihistamínico'),
('Loratadina', 'Claritin', 'Tabletas 10mg', 'Antihistamínico no sedante'),
('Ranitidina', 'Zantac', 'Tabletas 150mg', 'Antagonista H2'),
('Naproxeno', 'Naprosyn', 'Tabletas 500mg', 'Antiinflamatorio no esteroideo'),
('Diclofenaco', 'Voltaren', 'Tabletas 50mg', 'Antiinflamatorio no esteroideo'),
('Atorvastatina', 'Lipitor', 'Tabletas 20mg', 'Estatina para colesterol'),
('Sertralina', 'Zoloft', 'Tabletas 50mg', 'Antidepresivo ISRS'),
('Amlodipino', 'Norvasc', 'Tabletas 5mg', 'Bloqueador de canales de calcio');

-- ============================================
-- TABLA: ORGANIZACIONES
-- ============================================
CREATE TABLE organizaciones (
    id INT PRIMARY KEY AUTO_INCREMENT,
    latitud DECIMAL(10, 8) NOT NULL,
    longitud DECIMAL(11, 8) NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    clave VARCHAR(255) NOT NULL,
    activa BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_usuario (usuario),
    INDEX idx_ubicacion (latitud, longitud)
);

-- Insertar organizaciones de ejemplo
INSERT INTO organizaciones (latitud, longitud, nombre, usuario, clave, activa) VALUES
(19.432608, -99.133209, 'Cruz Roja Mexicana - Benito Juárez', 'cruzroja_bj', 'cruz123', TRUE),
(19.427045, -99.158234, 'Cruz Roja Mexicana - Cuauhtémoc', 'cruzroja_cua', 'cruz123', TRUE),
(19.410568, -99.199745, 'Hospital General de México', 'hospital_general', 'hospital123', TRUE),
(19.441245, -99.144567, 'Centro de Salud Cuauhtémoc', 'centro_salud', 'salud123', TRUE),
(19.423456, -99.167890, 'Farmacia del Pueblo', 'farmacia_pueblo', 'farma123', TRUE);

-- ============================================
-- TABLA: INVENTARIO (Stock de medicinas por organización)
-- ============================================
CREATE TABLE inventario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_organizacion INT NOT NULL,
    id_medicina INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 0,
    fecha_caducidad DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_organizacion) REFERENCES organizaciones(id) ON DELETE CASCADE,
    FOREIGN KEY (id_medicina) REFERENCES catalogo(id) ON DELETE CASCADE,
    INDEX idx_org_medicina (id_organizacion, id_medicina),
    INDEX idx_caducidad (fecha_caducidad),
    CONSTRAINT chk_cantidad_positiva CHECK (cantidad >= 0)
);

-- Insertar inventario ejemplo (Cruz Roja BJ - ID 1)
INSERT INTO inventario (id_organizacion, id_medicina, cantidad, fecha_caducidad) VALUES
(1, 1, 150, '2025-12-31'),
(1, 2, 80, '2026-06-15'),
(1, 3, 45, '2025-09-30'),
(1, 4, 175, '2026-03-01'),
(1, 5, 60, '2025-11-30'),
(1, 6, 40, '2026-01-15'),
(1, 7, 25, '2025-08-31'),
(1, 8, 90, '2026-04-30');

-- Insertar inventario ejemplo (Hospital General - ID 3)
INSERT INTO inventario (id_organizacion, id_medicina, cantidad, fecha_caducidad) VALUES
(3, 1, 100, '2025-12-15'),
(3, 9, 120, '2026-02-28'),
(3, 10, 200, '2026-05-15'),
(3, 11, 80, '2025-10-31'),
(3, 12, 50, '2026-01-20'),
(3, 4, 30, '2025-09-15');

-- Insertar inventario ejemplo (Centro de Salud - ID 4)
INSERT INTO inventario (id_organizacion, id_medicina, cantidad, fecha_caducidad) VALUES
(4, 1, 50, '2025-11-30'),
(4, 2, 35, '2026-03-15'),
(4, 3, 25, '2025-08-31'),
(4, 9, 45, '2026-04-30'),
(4, 17, 20, '2026-06-30');

-- ============================================
-- TABLA: DONACIONES (Registro de donaciones recibidas)
-- ============================================
CREATE TABLE donaciones (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_organizacion INT NOT NULL,
    id_medicina INT NOT NULL,
    curp_donatario VARCHAR(18) NOT NULL,
    cantidad INT NOT NULL,
    fecha_donacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_caducidad DATE NOT NULL,
    FOREIGN KEY (id_organizacion) REFERENCES organizaciones(id) ON DELETE CASCADE,
    FOREIGN KEY (id_medicina) REFERENCES catalogo(id) ON DELETE CASCADE,
    INDEX idx_org_fecha (id_organizacion, fecha_donacion),
    INDEX idx_curp (curp_donatario),
    CONSTRAINT chk_donacion_cantidad CHECK (cantidad > 0)
);

-- Insertar donaciones de ejemplo
INSERT INTO donaciones (id_organizacion, id_medicina, curp_donatario, cantidad, fecha_caducidad, fecha_donacion) VALUES
(1, 1, 'GAML800101HNTRRR01', 25, '2025-12-31', '2026-02-20 10:30:00'),
(1, 4, 'MAPL750202MNTLLN02', 10, '2026-06-15', '2026-02-19 14:15:00'),
(3, 9, 'ROPL820303HGRNTN03', 50, '2026-02-28', '2026-02-18 09:00:00'),
(1, 5, 'LOAA910404MNLPB04', 20, '2025-11-30', '2026-02-17 16:45:00'),
(4, 1, 'HEGM720505HDFNT05', 15, '2025-11-30', '2026-02-16 11:20:00'),
(3, 10, 'SAFC880606HNTRR06', 30, '2026-05-15', '2026-02-15 13:30:00');

-- ============================================
-- TABLA: ENTREGAS (Registro de entregas realizadas)
-- ============================================
CREATE TABLE entregas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_organizacion INT NOT NULL,
    id_medicina INT NOT NULL,
    curp_receptor VARCHAR(18) NOT NULL,
    cantidad INT NOT NULL,
    fecha_entrega TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_organizacion) REFERENCES organizaciones(id) ON DELETE CASCADE,
    FOREIGN KEY (id_medicina) REFERENCES catalogo(id) ON DELETE CASCADE,
    INDEX idx_org_fecha (id_organizacion, fecha_entrega),
    INDEX idx_curp (curp_receptor),
    CONSTRAINT chk_entrega_cantidad CHECK (cantidad > 0)
);

-- Insertar entregas de ejemplo
INSERT INTO entregas (id_organizacion, id_medicina, cantidad, curp_receptor, fecha_entrega) VALUES
(1, 1, 5, 'SNNR900101HNTRN01', '2026-02-21 09:30:00'),
(1, 2, 3, 'MARN850202HNTRN02', '2026-02-20 15:00:00'),
(3, 1, 2, 'ALVR920303HNTRN03', '2026-02-20 10:15:00'),
(1, 4, 4, 'GOVL780404MNTRN04', '2026-02-19 14:30:00'),
(4, 1, 1, 'PSLM950505HNTRN05', '2026-02-18 11:00:00');

-- ============================================
-- CONSULTAS DE VERIFICACIÓN
-- ============================================
SELECT '===== USUARIOS ADMIN =====' AS '';
SELECT * FROM usuarios_admin;

SELECT '===== CATÁLOGO =====' AS '';
SELECT COUNT(*) AS total_medicamentos FROM catalogo;
SELECT * FROM catalogo;

SELECT '===== ORGANIZACIONES =====' AS '';
SELECT * FROM organizaciones;

SELECT '===== INVENTARIO =====' AS '';
SELECT o.nombre AS organizacion, c.nombre_comercial, i.cantidad, i.fecha_caducidad 
FROM inventario i 
JOIN organizaciones o ON i.id_organizacion = o.id 
JOIN catalogo c ON i.id_medicina = c.id
ORDER BY o.nombre, c.nombre_comercial;

SELECT '===== DONACIONES =====' AS '';
SELECT o.nombre AS organizacion, c.nombre_comercial, d.cantidad, d.curp_donatario, d.fecha_donacion
FROM donaciones d 
JOIN organizaciones o ON d.id_organizacion = o.id 
JOIN catalogo c ON d.id_medicina = c.id
ORDER BY d.fecha_donacion DESC;

SELECT '===== ENTREGAS =====' AS '';
SELECT o.nombre AS organizacion, c.nombre_comercial, e.cantidad, e.curp_receptor, e.fecha_entrega
FROM entregas e 
JOIN organizaciones o ON e.id_organizacion = o.id 
JOIN catalogo c ON e.id_medicina = c.id
ORDER BY e.fecha_entrega DESC;

-- ============================================
-- CREDENCIALES DE ACCESO
-- ============================================
SELECT '===== CREDENCIALES DE ACCESO =====' AS '';
SELECT 'Admin' AS tipo, usuario, clave FROM usuarios_admin
UNION ALL
SELECT 'Organizacion', usuario, clave FROM organizaciones;