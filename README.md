# MEXICARE - Sistema de Donación de Medicinas

## 📋 Descripción del Proyecto

MexicareV2 es una aplicación Android completa para la gestión de donaciones de medicinas próximas a caducar. El sistema conecta donantes con organizaciones que distribuyen las medicinas a personas que las necesitan.

## 🏗️ Arquitectura del Sistema

### Componentes Principales

1. **Aplicación Android** (Java)
   - Interfaz de usuario nativa
   - Gestión de donaciones y entregas
   - Visualización de organizaciones en mapa
   - Panel de administración para organizaciones

2. **Servidor API** (Python Flask)
   - API RESTful para comunicación con la app
   - Gestión de inventario automática
   - Validación de datos y lógica de negocio

3. **Backend Alternativo** (PHP)
   - API complementaria
   - Gestión de datos

4. **Base de Datos** (MySQL en Docker)
   - Almacenamiento de datos
   - Gestión de inventario
   - Registro de transacciones

## 🚀 Características Implementadas

### ✅ Funcionalidades Principales

- **Sistema de Donaciones**: Registro completo de donaciones con fecha de caducidad
- **Sistema de Entregas**: Control de entregas con validación de inventario
- **Gestión de Inventario**: Actualización automática al registrar donaciones/entregas
- **Mapa de Organizaciones**: Visualización de organizaciones activas con OpenStreetMap
- **Panel de Administración**: Gestión completa para organizaciones y administradores
- **Catálogo de Medicinas**: Base de datos completa de medicamentos
- **Autenticación**: Sistema de login para organizaciones y administradores

### 🎨 Interfaz de Usuario

- Diseño Material Design
- Navegación intuitiva
- Marca de agua corporativa
- Selector de fecha con calendario nativo
- Listas dinámicas con RecyclerView

## 📦 Requisitos del Sistema

### Para Ejecutar la Aplicación

- **Android Studio** Hedgehog o superior
- **JDK** 11 o superior
- **Docker** para la base de datos MySQL
- **Python** 3.8+ para el servidor API
- **API Level** 24+ (Android 7.0)

## 🔧 Instalación y Configuración

### 1. Base de Datos (Docker)

```bash
# Iniciar contenedor MySQL
docker-compose up -d

# Importar datos iniciales
docker exec -i mysql_container mysql -uroot -p proyecto_quinto < DATOS_PRUEBA.sql
docker exec -i mysql_container mysql -uroot -p proyecto_quinto < INDICES_OPTIMIZACION.sql
```

### 2. Servidor Python

```bash
cd python_server

# Instalar dependencias
pip install flask mysql-connector-python python-dotenv

# Configurar variables de entorno (.env)
DB_HOST=localhost
DB_PORT=3306
DB_USER=root
DB_PASSWORD=tu_password
DB_NAME=proyecto_quinto

# Ejecutar servidor
python app.py
```

El servidor estará disponible en:
- Web: http://localhost:5000
- Android Emulator: http://10.0.2.2:5000

### 3. Aplicación Android

```bash
# Abrir proyecto en Android Studio
File → Open → Seleccionar carpeta MexicareV2

# Sincronizar Gradle
Tools → Sync Project with Gradle Files

# Ejecutar aplicación
Run → Run 'app'
```

## 📁 Estructura del Proyecto

```
MexicareV2/
├── app/                          # Aplicación Android
│   ├── src/main/
│   │   ├── java/com/mexicare/
│   │   │   ├── activities/      # Activities principales
│   │   │   ├── adapters/        # Adapters para RecyclerView
│   │   │   ├── api/            # Cliente Retrofit
│   │   │   ├── fragments/      # Fragments para paneles
│   │   │   ├── models/         # Modelos de datos
│   │   │   └── utils/          # Utilidades
│   │   ├── res/                # Recursos (layouts, drawables)
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── backend/                     # Backend PHP (alternativo)
│   ├── api/                    # Endpoints PHP
│   ├── config/                 # Configuración
│   └── models/                 # Modelos PHP
├── python_server/              # Servidor API Python
│   ├── app.py                 # Aplicación Flask
│   └── .env                   # Variables de entorno
├── Logo/                       # Recursos gráficos
│   └── mexicare_logo.png
├── docker-compose.yml          # Configuración Docker
├── DATOS_PRUEBA.sql           # Datos iniciales
├── INDICES_OPTIMIZACION.sql   # Optimización BD
└── README.md                   # Este archivo
```

## 🗄️ Estructura de Base de Datos

### Tablas Principales

- **usuarios_admin**: Administradores del sistema
- **organizaciones**: Organizaciones registradas
- **catalogo**: Catálogo de medicinas
- **inventario**: Inventario actual de cada organización
- **donaciones**: Registro de donaciones recibidas
- **entregas**: Registro de entregas realizadas

## 🔐 Usuarios de Prueba

### Administrador
- Usuario: `admin`
- Contraseña: `admin123`

### Organizaciones
- **Cruz Roja**: usuario `cruzroja`, contraseña `password123`
- **DIF Central**: usuario `difcentral`, contraseña `password123`
- **Cáritas**: usuario `caritas`, contraseña `password123`

## 📱 Flujo de la Aplicación

### Para Donantes
1. Seleccionar "QUIERO DONAR"
2. Ingresar CURP del donante
3. Seleccionar medicina del catálogo
4. Ingresar cantidad
5. Seleccionar fecha de caducidad (calendario)
6. Ver organizaciones en mapa
7. Confirmar donación

### Para Solicitantes
1. Seleccionar "REQUIERO MEDICINA"
2. Buscar medicina disponible
3. Ver organizaciones con stock
4. Contactar organización

### Para Organizaciones
1. Login con credenciales
2. Acceder al panel de administración
3. Gestionar:
   - Inventario actual
   - Donaciones recibidas
   - Entregas realizadas

## 🛠️ Tecnologías Utilizadas

### Frontend (Android)
- Java
- Android SDK
- Retrofit (HTTP Client)
- OSMDroid (Mapas)
- RecyclerView
- CardView
- Material Design

### Backend
- Python Flask
- PHP
- MySQL
- Docker

### Herramientas
- Android Studio
- Gradle
- Git

## 📊 API Endpoints

### Autenticación
- `POST /api/login` - Login de usuarios

### Organizaciones
- `GET /api/organizaciones` - Listar organizaciones activas
- `POST /api/organizaciones` - Registrar organización

### Catálogo
- `GET /api/catalogo` - Obtener catálogo de medicinas
- `POST /api/catalogo` - Agregar medicina al catálogo

### Inventario
- `GET /api/inventario?organizacion_id={id}` - Consultar inventario
- `POST /api/inventario` - Actualizar inventario

### Donaciones
- `GET /api/donaciones` - Listar donaciones
- `POST /api/donaciones` - Registrar donación

### Entregas
- `GET /api/entregas` - Listar entregas
- `POST /api/entregas` - Registrar entrega

## 🎯 Características Técnicas

### Gestión Automática de Inventario
- Las donaciones incrementan el inventario automáticamente
- Las entregas decrementan el inventario con validación
- Eliminación automática de items con cantidad 0

### Validaciones
- Verificación de stock antes de entregas
- Validación de campos requeridos
- Formato de fechas DD/MM/YYYY → YYYY-MM-DD

### Optimización
- Índices en base de datos para consultas rápidas
- Caché de datos en la aplicación
- Consultas optimizadas con JOINs

## 📝 Notas de Desarrollo

- El proyecto usa Kotlin DSL para Gradle
- La base de datos se ejecuta en Docker para portabilidad
- El servidor Python maneja la lógica de negocio principal
- El backend PHP está disponible como alternativa

## 👥 Créditos

Proyecto desarrollado para la materia de Laboratorio de Desarrollo de Software
Instituto Politécnico Nacional - CECyT 9 "Juan de Dios Bátiz"

## 📄 Licencia

Este proyecto es de uso académico.

---

**Versión:** 2.0.0  
**Fecha:** Noviembre 2024  
**Estado:** ✅ Funcional y listo para entrega
