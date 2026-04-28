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
        $usuario = $data->usuario;
        $clave = $data->clave;
        
        // Primero buscar en usuarios_admin
        $admin_query = "SELECT id, usuario FROM usuarios_admin WHERE usuario = ? AND clave = ? AND activo = 1";
        $admin_stmt = $db->prepare($admin_query);
        $admin_stmt->execute([$usuario, $clave]);
        
        if ($admin_row = $admin_stmt->fetch(PDO::FETCH_ASSOC)) {
            echo json_encode([
                "success" => true,
                "message" => "Login exitoso",
                "data" => [
                    "tipo" => "admin",
                    "id" => $admin_row['id'],
                    "usuario" => $admin_row['usuario'],
                    "nombre" => null,
                    "latitud" => null,
                    "longitud" => null,
                    "activa" => 1
                ]
            ]);
            exit;
        }
        
        // Si no es admin, buscar en organizaciones
        $org_query = "SELECT id, usuario, nombre, latitud, longitud FROM organizaciones WHERE usuario = ? AND clave = ? AND activa = 1";
        $org_stmt = $db->prepare($org_query);
        $org_stmt->execute([$usuario, $clave]);
        
        if ($org_row = $org_stmt->fetch(PDO::FETCH_ASSOC)) {
            echo json_encode([
                "success" => true,
                "message" => "Login exitoso",
                "data" => [
                    "tipo" => "organizacion",
                    "id" => $org_row['id'],
                    "usuario" => $org_row['usuario'],
                    "nombre" => $org_row['nombre'],
                    "latitud" => $org_row['latitud'],
                    "longitud" => $org_row['longitud'],
                    "activa" => 1
                ]
            ]);
            exit;
        }
        
        // Si no se encuentra en ninguna tabla
        echo json_encode([
            "success" => false,
            "message" => "Credenciales incorrectas"
        ]);
        
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