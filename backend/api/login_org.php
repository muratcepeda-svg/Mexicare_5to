<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type");

include_once '../config/database.php';

$database = new Database();
$db = $database->getConnection();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $data = json_decode(file_get_contents("php://input"));
    
    if (!empty($data->usuario) && !empty($data->clave)) {
        // Buscar solo en tabla organizaciones
        $query = "SELECT id, usuario, nombre, latitud, longitud, activa 
                  FROM organizaciones 
                  WHERE usuario = ? AND clave = ? AND activa = 1";
        
        $stmt = $db->prepare($query);
        $stmt->execute([$data->usuario, $data->clave]);
        
        if ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            echo json_encode([
                "success" => true,
                "message" => "Login exitoso",
                "data" => [
                    "tipo" => "organizacion",
                    "id" => $row['id'],
                    "usuario" => $row['usuario'],
                    "nombre" => $row['nombre'],
                    "latitud" => $row['latitud'],
                    "longitud" => $row['longitud'],
                    "activa" => $row['activa']
                ]
            ]);
        } else {
            // Debug: verificar si el usuario existe
            $debug_query = "SELECT usuario, clave, activa FROM organizaciones WHERE usuario = ?";
            $debug_stmt = $db->prepare($debug_query);
            $debug_stmt->execute([$data->usuario]);
            $debug_result = $debug_stmt->fetch(PDO::FETCH_ASSOC);
            
            if ($debug_result) {
                echo json_encode([
                    "success" => false,
                    "message" => "Contraseña incorrecta o usuario inactivo",
                    "debug" => "Usuario existe: " . $debug_result['usuario'] . ", Activa: " . $debug_result['activa']
                ]);
            } else {
                echo json_encode([
                    "success" => false,
                    "message" => "Usuario no encontrado"
                ]);
            }
        }
    } else {
        echo json_encode([
            "success" => false,
            "message" => "Datos incompletos"
        ]);
    }
} else {
    echo json_encode([
        "success" => false,
        "message" => "Método no permitido"
    ]);
}
?>