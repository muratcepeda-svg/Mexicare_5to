-- Insert test data for login testing
-- Based on the database structure from plan_basedatos.md

-- Insert admin user
INSERT INTO usuarios_admin (usuario, clave, activo) VALUES 
('admin', 'admin', 1)
ON DUPLICATE KEY UPDATE clave = 'admin', activo = 1;

-- Insert test organization
INSERT INTO organizaciones (latitud, longitud, nombre, usuario, clave, activa) VALUES 
(19.432608, -99.133209, 'Cruz Roja Mexicana CDMX', 'cruzroja_cdmx', 'cruz123', 1)
ON DUPLICATE KEY UPDATE 
    clave = 'cruz123', 
    activa = 1,
    nombre = 'Cruz Roja Mexicana CDMX',
    latitud = 19.432608,
    longitud = -99.133209;

-- Insert some sample medicines in catalog
INSERT INTO catalogo (sustancia_activa, nombre_comercial, presentacion, descripcion) VALUES 
('Paracetamol', 'Tempra', 'Tabletas 500mg', 'Analgésico y antipirético'),
('Ibuprofeno', 'Advil', 'Cápsulas 400mg', 'Antiinflamatorio no esteroideo'),
('Amoxicilina', 'Amoxil', 'Cápsulas 500mg', 'Antibiótico de amplio espectro')
ON DUPLICATE KEY UPDATE 
    sustancia_activa = VALUES(sustancia_activa),
    nombre_comercial = VALUES(nombre_comercial),
    presentacion = VALUES(presentacion),
    descripcion = VALUES(descripcion);

-- Verify data was inserted
SELECT 'ADMIN USERS:' as info;
SELECT id, usuario, clave, activo FROM usuarios_admin;

SELECT 'ORGANIZATIONS:' as info;
SELECT id, usuario, clave, nombre, activa FROM organizaciones;

SELECT 'CATALOG:' as info;
SELECT id, sustancia_activa, nombre_comercial FROM catalogo LIMIT 5;