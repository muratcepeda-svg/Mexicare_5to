-- =====================================================
-- DATOS DE PRUEBA PARA MEXICARE
-- =====================================================

-- Insertar usuarios admin
INSERT INTO usuarios_admin (usuario, clave) VALUES 
('admin', 'admin'),
('supervisor', 'super123');

-- Insertar organizaciones
INSERT INTO organizaciones (latitud, longitud, nombre, usuario, clave, activa) VALUES 
(19.432608, -99.133209, 'Cruz Roja Mexicana CDMX', 'cruzroja_cdmx', 'cruz123', 1),
(19.428500, -99.127700, 'DIF Nacional', 'dif_nacional', 'dif123', 1),
(19.420000, -99.150000, 'Cáritas Diocesana', 'caritas_mx', 'caritas123', 1),
(19.435000, -99.140000, 'Hospital General', 'hospital_gral', 'hosp123', 1),
(19.425000, -99.135000, 'Centro de Salud Benito Juárez', 'cs_bjuarez', 'salud123', 1);

-- Insertar catálogo de medicinas
INSERT INTO catalogo (sustancia_activa, nombre_comercial, presentacion, descripcion) VALUES 
('Paracetamol', 'Tempra', 'Tabletas 500mg', 'Analgésico y antipirético'),
('Ibuprofeno', 'Advil', 'Cápsulas 400mg', 'Antiinflamatorio no esteroideo'),
('Amoxicilina', 'Amoxil', 'Suspensión 250mg/5ml', 'Antibiótico de amplio espectro'),
('Loratadina', 'Clarityne', 'Tabletas 10mg', 'Antihistamínico para alergias'),
('Omeprazol', 'Prilosec', 'Cápsulas 20mg', 'Inhibidor de bomba de protones'),
('Metformina', 'Glucophage', 'Tabletas 850mg', 'Antidiabético oral'),
('Enalapril', 'Renitec', 'Tabletas 10mg', 'Antihipertensivo IECA'),
('Salbutamol', 'Ventolin', 'Inhalador 100mcg', 'Broncodilatador para asma'),
('Diclofenaco', 'Voltaren', 'Gel tópico 1%', 'Antiinflamatorio tópico'),
('Cetirizina', 'Zyrtec', 'Jarabe 5mg/5ml', 'Antihistamínico pediátrico');

-- Insertar inventario inicial
INSERT INTO inventario (id_organizacion, id_medicina, cantidad, fecha_caducidad) VALUES 
-- Cruz Roja CDMX
(1, 1, 150, '2025-06-15'), -- Paracetamol
(1, 2, 80, '2025-08-20'), -- Ibuprofeno
(1, 3, 45, '2025-04-10'), -- Amoxicilina
(1, 8, 25, '2025-12-31'), -- Salbutamol
-- DIF Nacional
(2, 1, 200, '2025-07-30'), -- Paracetamol
(2, 4, 60, '2025-09-15'), -- Loratadina
(2, 10, 35, '2025-05-25'), -- Cetirizina
-- Cáritas Diocesana
(3, 2, 90, '2025-06-08'), -- Ibuprofeno
(3, 5, 40, '2025-11-12'), -- Omeprazol
(3, 6, 55, '2025-10-20'), -- Metformina
-- Hospital General
(4, 3, 75, '2025-03-18'), -- Amoxicilina
(4, 7, 30, '2025-08-05'), -- Enalapril
(4, 9, 20, '2025-07-22'), -- Diclofenaco
-- Centro de Salud
(5, 1, 120, '2025-09-30'), -- Paracetamol
(5, 4, 45, '2025-06-12'), -- Loratadina
(5, 8, 15, '2025-11-08'); -- Salbutamol

-- Insertar algunas donaciones de ejemplo
INSERT INTO donaciones (id_organizacion, id_medicina, curp_donatario, cantidad, fecha_caducidad) VALUES 
(1, 1, 'ABCD123456HDFGHI01', 50, '2025-06-15'),
(2, 4, 'EFGH789012MDFGHI02', 30, '2025-09-15'),
(3, 2, 'IJKL345678HDFGHI03', 40, '2025-06-08');

-- Insertar algunas entregas de ejemplo
INSERT INTO entregas (id_organizacion, id_medicina, curp_receptor, cantidad) VALUES 
(1, 1, 'MNOP901234HDFGHI04', 10),
(2, 4, 'QRST567890MDFGHI05', 5),
(3, 5, 'UVWX123456HDFGHI06', 8);

-- =====================================================
-- CONSULTAS ÚTILES PARA VERIFICAR DATOS
-- =====================================================

-- Ver inventario completo con nombres
SELECT 
    o.nombre as organizacion,
    c.nombre_comercial as medicina,
    c.sustancia_activa,
    i.cantidad,
    i.fecha_caducidad
FROM inventario i
JOIN organizaciones o ON i.id_organizacion = o.id
JOIN catalogo c ON i.id_medicina = c.id
WHERE i.cantidad > 0
ORDER BY o.nombre, c.nombre_comercial;

-- Ver organizaciones con ubicación
SELECT 
    nombre,
    usuario,
    CONCAT(latitud, ', ', longitud) as coordenadas,
    activa
FROM organizaciones
ORDER BY nombre;

-- Ver medicinas disponibles por organización
SELECT 
    o.nombre as organizacion,
    COUNT(i.id) as total_medicinas,
    SUM(i.cantidad) as total_unidades
FROM organizaciones o
LEFT JOIN inventario i ON o.id = i.id_organizacion AND i.cantidad > 0
GROUP BY o.id, o.nombre
ORDER BY total_unidades DESC;