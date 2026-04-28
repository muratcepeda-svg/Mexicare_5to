-- =====================================================
-- DATOS COMPLETOS PARA MEXICARE - ORDEN CORRECTO
-- =====================================================

-- Primero insertar usuarios admin
INSERT INTO usuarios_admin (usuario, clave, activo) VALUES 
('admin', 'admin123', 1);

-- Segundo insertar organizaciones (necesarias para foreign keys)
INSERT INTO organizaciones (latitud, longitud, nombre, usuario, clave, activa) VALUES 
(19.4326, -99.1332, 'Cruz Roja CDMX', 'cruzroja', 'cruz123', 1),
(19.4285, -99.1277, 'Hospital General', 'hospital', 'hosp123', 1);

-- Tercero insertar catálogo de medicinas
INSERT INTO catalogo (sustancia_activa, nombre_comercial, presentacion, descripcion) VALUES 
('Paracetamol', 'Tempra', 'Tabletas 500mg', 'Analgesico y antipiretico para dolor y fiebre'),
('Ibuprofeno', 'Advil', 'Capsulas 400mg', 'Antiinflamatorio no esteroideo para dolor e inflamacion'),
('Amoxicilina', 'Amoxil', 'Capsulas 500mg', 'Antibiotico de amplio espectro para infecciones bacterianas'),
('Omeprazol', 'Prilosec', 'Capsulas 20mg', 'Inhibidor de bomba de protones para acidez estomacal'),
('Loratadina', 'Claritin', 'Tabletas 10mg', 'Antihistaminico para alergias y rinitis'),
('Metformina', 'Glucophage', 'Tabletas 850mg', 'Antidiabetico para control de glucosa en sangre'),
('Atorvastatina', 'Lipitor', 'Tabletas 20mg', 'Estatina para control de colesterol'),
('Losartan', 'Cozaar', 'Tabletas 50mg', 'Antihipertensivo para presion arterial alta'),
('Salbutamol', 'Ventolin', 'Inhalador 100mcg', 'Broncodilatador para asma y EPOC'),
('Diclofenaco', 'Voltaren', 'Gel topico 1%', 'Antiinflamatorio topico para dolor muscular');

-- Cuarto insertar inventario (ahora las organizaciones y medicinas existen)
INSERT INTO inventario (id_organizacion, id_medicina, cantidad, fecha_caducidad) VALUES 
(1, 1, 150, '2025-12-31'),
(1, 2, 80, '2025-08-15'),
(1, 3, 45, '2025-10-20'),
(1, 4, 60, '2025-09-30'),
(1, 5, 35, '2025-11-25'),
(2, 1, 200, '2025-11-30'),
(2, 6, 75, '2025-07-15'),
(2, 7, 50, '2025-06-30'),
(2, 8, 90, '2025-12-15'),
(2, 9, 25, '2025-05-20'),
(2, 10, 40, '2025-08-10');

-- Quinto insertar donaciones
INSERT INTO donaciones (id_organizacion, id_medicina, curp_donatario, cantidad, fecha_caducidad) VALUES 
(1, 1, 'GOMJ850315HDFNRL02', 50, '2025-12-31'),
(1, 2, 'MARS920708MDFRTL05', 30, '2025-08-15'),
(1, 3, 'LOPK780422HDFRPR08', 20, '2025-10-20'),
(2, 1, 'HERJ901205HDFRNL01', 75, '2025-11-30'),
(2, 6, 'GARMA851118MDFRCR03', 25, '2025-07-15'),
(2, 8, 'VAZL930614HDFRLS07', 35, '2025-12-15'),
(1, 4, 'ROMF880925MDFMRR04', 40, '2025-09-30'),
(2, 7, 'CRUD750830HDFRZV06', 20, '2025-06-30'),
(1, 5, 'SILP940312MDFLLR09', 15, '2025-11-25'),
(2, 9, 'MORR870719HDFRRB10', 10, '2025-05-20');

-- Sexto insertar entregas
INSERT INTO entregas (id_organizacion, id_medicina, curp_receptor, cantidad) VALUES 
(1, 1, 'PEAJ920815MDFRZN01', 10),
(1, 2, 'RAMK850603HDFMRL02', 5),
(1, 3, 'TORJ780920MDFRRM03', 8),
(1, 4, 'MEDL901127HDFNLS04', 12),
(1, 5, 'CASF940208MDFSTR05', 6),
(2, 1, 'RIVP870415MDFVRL06', 15),
(2, 6, 'GUTM930722HDFTRR07', 8),
(2, 7, 'FLOR851009MDFLJR08', 4),
(2, 8, 'SANC920531HDFNRL09', 10),
(2, 9, 'VELJ880216MDFLLR10', 3),
(1, 1, 'ORTD750912HDFRTV11', 8),
(2, 10, 'NAVS940625MDFVRR12', 5),
(1, 2, 'HERR870304HDFRNB13', 7),
(2, 1, 'JIMK910818MDFMRL14', 12),
(1, 3, 'AGUML881201HDFGRR15', 6);