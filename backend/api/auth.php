<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

include_once '../config/database.php';

$database = new Database();
$db = $database->getConnection();

$method = $_SERVER['REQUEST_METHOD'];
$request = explode('/', trim($_SERVER['PATH_INFO'],'/'));

if ($method == 'POST') {
    $data = json_decode(file_get_contents("php://input"));
    
    if ($request[0] == 'login') {
        if ($request[1] == 'admin') {
            // Login de administrador
            $query = "SELECT * FROM usuarios_admin WHERE usuario = ? AND clave = ? AND activo = 1";
            $stmt = $db->prepare($query);
            $stmt->bindParam(1, $data->usuario);
            $stmt->bindParam(2, $data->clave);
            $stmt->execute();
            
            if ($stmt->rowCount() > 0) {
                $row = $stmt->fetch(PDO::FETCH_ASSOC);
                echo json_encode([
                    "success" => true,
                    "message" => "Login exitoso",
                    "data" => [
                        "id" => $row['id'],
                        "usuario" => $row['usuario'],
                        "activo" => $row['activo']
                    ]
                ]);
            } else {
                echo json_encode([
                    "success" => false,
                    "message" => "Credenciales incorrectas"
                ]);
            }
        } 
        else if ($request[1] == 'organizacion') {
            // Login de organización
            $query = "SELECT * FROM organizaciones WHERE usuario = ? AND clave = ? AND activa = 1";
            $stmt = $db->prepare($query);
            $stmt->bindParam(1, $data->usuario);
            $stmt->bindParam(2, $data->clave);
            $stmt->execute();
            
            if ($stmt->rowCount() > 0) {
                $row = $stmt->fetch(PDO::FETCH_ASSOC);
                echo json_encode([
                    "success" => true,
                    "message" => "Login exitoso",
                    "data" => [
                        "id" => $row['id'],
                        "nombre" => $row['nombre'],
                        "usuario" => $row['usuario'],
                        "latitud" => $row['latitud'],
                        "longitud" => $row['longitud'],
                        "activa" => $row['activa']
                    ]
                ]);
            } else {
                echo json_encode([
                    "success" => false,
                    "message" => "Credenciales incorrectas"
                ]);
            }
        }
    }
} else {
    echo json_encode([
        "success" => false,
        "message" => "Método no permitido"
    ]);
}
?>