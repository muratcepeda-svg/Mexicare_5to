<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: GET, POST");
header("Access-Control-Allow-Headers: Content-Type");

include_once '../config/database.php';

$database = new Database();
$db = $database->getConnection();

$method = $_SERVER['REQUEST_METHOD'];

if ($method == 'GET') {
    // Obtener todas las medicinas
    $query = "SELECT * FROM catalogo ORDER BY nombre_comercial";
    $stmt = $db->prepare($query);
    $stmt->execute();
    
    $medicinas = [];
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $medicinas[] = [
            "id" => $row['id'],
            "sustancia_activa" => $row['sustancia_activa'],
            "nombre_comercial" => $row['nombre_comercial'],
            "presentacion" => $row['presentacion'],
            "descripcion" => $row['descripcion'],
            "created_at" => $row['created_at']
        ];
    }
    
    echo json_encode([
        "success" => true,
        "message" => "Medicinas obtenidas",
        "data" => $medicinas
    ]);
    
} else if ($method == 'POST') {
    // Crear nueva medicina
    $data = json_decode(file_get_contents("php://input"));
    
    if (!empty($data->sustancia_activa) && !empty($data->nombre_comercial) && !empty($data->presentacion)) {
        $query = "INSERT INTO catalogo (sustancia_activa, nombre_comercial, presentacion, descripcion) VALUES (?, ?, ?, ?)";
        $stmt = $db->prepare($query);
        
        if ($stmt->execute([
            $data->sustancia_activa,
            $data->nombre_comercial,
            $data->presentacion,
            $data->descripcion ?? ''
        ])) {
            echo json_encode([
                "success" => true,
                "message" => "Medicina creada exitosamente",
                "data" => [
                    "id" => $db->lastInsertId(),
                    "sustancia_activa" => $data->sustancia_activa,
                    "nombre_comercial" => $data->nombre_comercial,
                    "presentacion" => $data->presentacion
                ]
            ]);
        } else {
            echo json_encode([
                "success" => false,
                "message" => "Error al crear medicina"
            ]);
        }
    } else {
        echo json_encode([
            "success" => false,
            "message" => "Datos incompletos"
        ]);
    }
}
?>