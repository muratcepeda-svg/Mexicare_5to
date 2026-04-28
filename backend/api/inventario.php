<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: GET, POST");
header("Access-Control-Allow-Headers: Content-Type");

include_once '../config/database.php';

$database = new Database();
$db = $database->getConnection();

$method = $_SERVER['REQUEST_METHOD'];
$request = explode('/', trim($_SERVER['PATH_INFO'] ?? '', '/'));

if ($method == 'GET') {
    if (isset($request[1]) && $request[0] == 'organizacion') {
        // Inventario por organización con JOIN
        $orgId = $request[1];
        $query = "SELECT i.*, c.nombre_comercial, c.sustancia_activa, o.nombre as nombre_organizacion
                  FROM inventario i 
                  JOIN catalogo c ON i.id_medicina = c.id 
                  JOIN organizaciones o ON i.id_organizacion = o.id 
                  WHERE i.id_organizacion = ? AND i.cantidad > 0
                  ORDER BY c.nombre_comercial";
        $stmt = $db->prepare($query);
        $stmt->execute([$orgId]);
    } else {
        // Inventario completo
        $query = "SELECT i.*, c.nombre_comercial, c.sustancia_activa, o.nombre as nombre_organizacion
                  FROM inventario i 
                  JOIN catalogo c ON i.id_medicina = c.id 
                  JOIN organizaciones o ON i.id_organizacion = o.id 
                  WHERE i.cantidad > 0
                  ORDER BY o.nombre, c.nombre_comercial";
        $stmt = $db->prepare($query);
        $stmt->execute();
    }
    
    $inventario = [];
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $inventario[] = [
            "id" => $row['id'],
            "id_organizacion" => $row['id_organizacion'],
            "id_medicina" => $row['id_medicina'],
            "cantidad" => $row['cantidad'],
            "fecha_caducidad" => $row['fecha_caducidad'],
            "nombre_medicina" => $row['nombre_comercial'],
            "sustancia_activa" => $row['sustancia_activa'],
            "nombre_organizacion" => $row['nombre_organizacion']
        ];
    }
    
    echo json_encode([
        "success" => true,
        "message" => "Inventario obtenido",
        "data" => $inventario
    ]);
}
?>