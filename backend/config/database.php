<?php
// Configuración de base de datos MySQL
$host = "localhost";
$username = "root";
$password = "Murat2007";
$database = "proyecto_quinto";

try {
    $pdo = new PDO("mysql:host=$host;dbname=$database;charset=utf8", $username, $password);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch(PDOException $e) {
    die("Error de conexión: " . $e->getMessage());
}
?>