<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST');
header('Access-Control-Allow-Headers: Content-Type');

require_once '../config/database.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Registrar nueva organización
    $input = json_decode(file_get_contents('php://input'), true);
    
    $nombre = $input['nombre'] ?? '';
    $usuario = $input['usuario'] ?? '';
    $clave = $input['clave'] ?? '';
    $latitud = $input['latitud'] ?? 0;
    $longitud = $input['longitud'] ?? 0;
    
    if (empty($nombre) || empty($usuario) || empty($clave)) {
        echo json_encode(['success' => false, 'error' => 'Datos incompletos']);
        exit;
    }
    
    try {
        $stmt = $pdo->prepare("INSERT INTO organizaciones (nombre, usuario, clave, latitud, longitud, activa) VALUES (?, ?, ?, ?, ?, 1)");
        $stmt->execute([$nombre, $usuario, $clave, $latitud, $longitud]);
        
        echo json_encode([
            'success' => true,
            'message' => 'Organización registrada exitosamente',
            'id' => $pdo->lastInsertId()
        ]);
        
    } catch (Exception $e) {
        echo json_encode(['success' => false, 'error' => $e->getMessage()]);
    }
    
} else if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    // Obtener organizaciones
    try {
        $stmt = $pdo->query("SELECT id, nombre, usuario, latitud, longitud, activa FROM organizaciones WHERE activa = 1");
        $organizaciones = $stmt->fetchAll(PDO::FETCH_ASSOC);
        
        echo json_encode([
            'success' => true,
            'data' => $organizaciones
        ]);
        
    } catch (Exception $e) {
        echo json_encode(['success' => false, 'error' => $e->getMessage()]);
    }
}
?>