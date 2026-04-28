-- =====================================================
-- ÍNDICES PARA OPTIMIZACIÓN DE CONSULTAS
-- =====================================================

-- Índices para login rápido
CREATE INDEX idx_admin_login ON usuarios_admin(usuario, clave);
CREATE INDEX idx_org_login ON organizaciones(usuario, clave);

-- Índices para búsquedas de inventario
CREATE INDEX idx_inventario_org ON inventario(id_organizacion, cantidad);
CREATE INDEX idx_inventario_medicina ON inventario(id_medicina, cantidad);
CREATE INDEX idx_inventario_caducidad ON inventario(fecha_caducidad);

-- Índices para búsquedas de catálogo
CREATE INDEX idx_catalogo_nombre ON catalogo(nombre_comercial);
CREATE INDEX idx_catalogo_sustancia ON catalogo(sustancia_activa);

-- Índices para donaciones y entregas
CREATE INDEX idx_donaciones_org_fecha ON donaciones(id_organizacion, fecha_donacion);
CREATE INDEX idx_entregas_org_fecha ON entregas(id_organizacion, fecha_entrega);

-- Índice compuesto para búsquedas complejas
CREATE INDEX idx_inventario_completo ON inventario(id_organizacion, id_medicina, cantidad, fecha_caducidad);

-- =====================================================
-- VISTA OPTIMIZADA PARA INVENTARIO COMPLETO
-- =====================================================

CREATE VIEW v_inventario_completo AS
SELECT 
    i.id,
    i.id_organizacion,
    i.id_medicina,
    i.cantidad,
    i.fecha_caducidad,
    o.nombre as nombre_organizacion,
    o.latitud,
    o.longitud,
    c.nombre_comercial as nombre_medicina,
    c.sustancia_activa,
    c.presentacion,
    DATEDIFF(i.fecha_caducidad, CURDATE()) as dias_caducidad
FROM inventario i
JOIN organizaciones o ON i.id_organizacion = o.id
JOIN catalogo c ON i.id_medicina = c.id
WHERE i.cantidad > 0 AND o.activa = 1;

-- =====================================================
-- PROCEDIMIENTO PARA BÚSQUEDA RÁPIDA DE MEDICINAS
-- =====================================================

DELIMITER //
CREATE PROCEDURE sp_buscar_medicinas_disponibles(IN p_nombre_medicina VARCHAR(100))
BEGIN
    SELECT 
        v.nombre_organizacion,
        v.nombre_medicina,
        v.sustancia_activa,
        v.cantidad,
        v.fecha_caducidad,
        v.latitud,
        v.longitud,
        v.dias_caducidad
    FROM v_inventario_completo v
    WHERE v.nombre_medicina LIKE CONCAT('%', p_nombre_medicina, '%')
       OR v.sustancia_activa LIKE CONCAT('%', p_nombre_medicina, '%')
    ORDER BY v.cantidad DESC, v.dias_caducidad DESC;
END //
DELIMITER ;

-- =====================================================
-- TRIGGER PARA ACTUALIZAR INVENTARIO AUTOMÁTICAMENTE
-- =====================================================

DELIMITER //
CREATE TRIGGER tr_donacion_actualiza_inventario
AFTER INSERT ON donaciones
FOR EACH ROW
BEGIN
    -- Verificar si ya existe el medicamento en inventario
    IF EXISTS (SELECT 1 FROM inventario 
               WHERE id_organizacion = NEW.id_organizacion 
               AND id_medicina = NEW.id_medicina 
               AND fecha_caducidad = NEW.fecha_caducidad) THEN
        -- Actualizar cantidad existente
        UPDATE inventario 
        SET cantidad = cantidad + NEW.cantidad,
            updated_at = CURRENT_TIMESTAMP
        WHERE id_organizacion = NEW.id_organizacion 
        AND id_medicina = NEW.id_medicina 
        AND fecha_caducidad = NEW.fecha_caducidad;
    ELSE
        -- Insertar nuevo registro
        INSERT INTO inventario (id_organizacion, id_medicina, cantidad, fecha_caducidad)
        VALUES (NEW.id_organizacion, NEW.id_medicina, NEW.cantidad, NEW.fecha_caducidad);
    END IF;
END //
DELIMITER ;

DELIMITER //
CREATE TRIGGER tr_entrega_actualiza_inventario
AFTER INSERT ON entregas
FOR EACH ROW
BEGIN
    -- Reducir cantidad del inventario (FIFO - primero en caducar)
    UPDATE inventario 
    SET cantidad = cantidad - NEW.cantidad,
        updated_at = CURRENT_TIMESTAMP
    WHERE id_organizacion = NEW.id_organizacion 
    AND id_medicina = NEW.id_medicina 
    AND cantidad >= NEW.cantidad
    ORDER BY fecha_caducidad ASC
    LIMIT 1;
    
    -- Eliminar registros con cantidad 0
    DELETE FROM inventario 
    WHERE cantidad <= 0;
END //
DELIMITER ;