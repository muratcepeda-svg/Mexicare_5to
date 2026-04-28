<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Content-Type');

require_once '../config/database.php';

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode(['success' => false, 'error' => 'Method not allowed']);
    exit;
}

$input = json_decode(file_get_contents('php://input'), true);
$usuario = $input['usuario'] ?? '';
$password = $input['password'] ?? '';

if (empty($usuario) || empty($password)) {
    echo json_encode(['success' => false, 'error' => 'Usuario y password requeridos']);
    exit;
}

try {
    // Buscar en usuarios_admin
    $stmt = $pdo->prepare("SELECT id, usuario FROM usuarios_admin WHERE usuario = ? AND clave = ? AND activo = 1");
    $stmt->execute([$usuario, $password]);
    $admin = $stmt->fetch(PDO::FETCH_ASSOC);
    
    if ($admin) {
        echo json_encode([
            'success' => true,
            'type' => 'admin',
            'user' => $admin
        ]);
        exit;
    }
    
    // Buscar en organizaciones
    $stmt = $pdo->prepare("SELECT id, usuario, nombre FROM organizaciones WHERE usuario = ? AND clave = ? AND activa = 1");
    $stmt->execute([$usuario, $password]);
    $org = $stmt->fetch(PDO::FETCH_ASSOC);
    
    if ($org) {
        echo json_encode([
            'success' => true,
            'type' => 'organization',
            'user' => $org
        ]);
        exit;
    }
    
    echo json_encode(['success' => false, 'error' => 'Credenciales incorrectas']);
    
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(['success' => false, 'error' => $e->getMessage()]);
}
?>