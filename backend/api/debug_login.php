<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type");

include_once '../config/database.php';

$database = new Database();
$db = $database->getConnection();

// Log everything
$input = file_get_contents("php://input");
$data = json_decode($input);

$response = [
    "debug_info" => [
        "method" => $_SERVER['REQUEST_METHOD'],
        "raw_input" => $input,
        "decoded_data" => $data,
        "usuario_received" => isset($data->usuario) ? $data->usuario : null,
        "clave_received" => isset($data->clave) ? $data->clave : null
    ]
];

if ($_SERVER['REQUEST_METHOD'] == 'POST' && !empty($data->usuario) && !empty($data->clave)) {
    $usuario = $data->usuario;
    $clave = $data->clave;
    
    // Check in organizaciones
    $query = "SELECT id, usuario, clave, nombre, latitud, longitud, activa FROM organizaciones WHERE usuario = ?";
    $stmt = $db->prepare($query);
    $stmt->execute([$usuario]);
    $user_data = $stmt->fetch(PDO::FETCH_ASSOC);
    
    $response["debug_info"]["user_found"] = $user_data ? true : false;
    $response["debug_info"]["user_data"] = $user_data;
    
    if ($user_data) {
        $response["debug_info"]["password_match"] = ($user_data['clave'] === $clave);
        $response["debug_info"]["user_active"] = ($user_data['activa'] == 1);
        
        if ($user_data['clave'] === $clave && $user_data['activa'] == 1) {
            $response["success"] = true;
            $response["message"] = "Login exitoso";
            $response["data"] = [
                "tipo" => "organizacion",
                "id" => $user_data['id'],
                "usuario" => $user_data['usuario'],
                "nombre" => $user_data['nombre'],
                "latitud" => $user_data['latitud'],
                "longitud" => $user_data['longitud'],
                "activa" => $user_data['activa']
            ];
        } else {
            $response["success"] = false;
            $response["message"] = "Credenciales incorrectas o usuario inactivo";
        }
    } else {
        // Check admin table
        $admin_query = "SELECT id, usuario, clave, activo FROM usuarios_admin WHERE usuario = ?";
        $admin_stmt = $db->prepare($admin_query);
        $admin_stmt->execute([$usuario]);
        $admin_data = $admin_stmt->fetch(PDO::FETCH_ASSOC);
        
        $response["debug_info"]["admin_found"] = $admin_data ? true : false;
        $response["debug_info"]["admin_data"] = $admin_data;
        
        if ($admin_data && $admin_data['clave'] === $clave && $admin_data['activo'] == 1) {
            $response["success"] = true;
            $response["message"] = "Login exitoso";
            $response["data"] = [
                "tipo" => "admin",
                "id" => $admin_data['id'],
                "usuario" => $admin_data['usuario'],
                "nombre" => null,
                "latitud" => null,
                "longitud" => null,
                "activa" => 1
            ];
        } else {
            $response["success"] = false;
            $response["message"] = "Usuario no encontrado";
        }
    }
} else {
    $response["success"] = false;
    $response["message"] = "Datos incompletos o método incorrecto";
}

echo json_encode($response, JSON_PRETTY_PRINT);
?>