from flask import Flask, request, jsonify
import mysql.connector
import os
from dotenv import load_dotenv

load_dotenv()

app = Flask(__name__)

# CORS - Headers para permitir conexiones desde la web
@app.after_request
def add_cors_headers(response):
    response.headers['Access-Control-Allow-Origin'] = '*'
    response.headers['Access-Control-Allow-Methods'] = 'GET, POST, PUT, DELETE, OPTIONS'
    response.headers['Access-Control-Allow-Headers'] = 'Content-Type, Accept'
    return response

DB_CONFIG = {
    'host': os.getenv('DB_HOST', 'localhost'),
    'port': int(os.getenv('DB_PORT', '3306')),
    'user': os.getenv('DB_USER', 'root'),
    'password': os.getenv('DB_PASSWORD', 'Murat2007'),
    'database': os.getenv('DB_NAME', 'proyecto_quinto')
}

def get_db_connection():
    return mysql.connector.connect(**DB_CONFIG)

@app.route('/api/login', methods=['POST', 'GET'])
def login():
    print("=== LOGIN REQUEST ===")
    
    if request.method == 'GET':
        return jsonify({'message': 'Endpoint funcionando'})
    
    #Obtener datos
    data = request.get_json()
    print("Data received:", data)
    
    if not data:
        return jsonify({'success': False, 'message': 'No data'})
    
    usuario = data.get('usuario', '')
    clave = data.get('clave', '') or data.get('password', '')
    
    print(f"Buscando: usuario={usuario}, clave={clave}")
    
    if not usuario or not clave:
        return jsonify({'success': False, 'message': 'Faltan datos'})
    
    try:
        conn = get_db_connection()
        cursor = conn.cursor()
        
        #Buscar admin
        cursor.execute("SELECT id FROM usuarios_admin WHERE usuario = %s AND clave = %s", (usuario, clave))
        admin = cursor.fetchone()
        
        if admin:
            cursor.close()
            conn.close()
            print("Admin encontrado:", admin)
            return jsonify({'success': True, 'tipo': 'admin', 'id': admin[0]})
        
        #Buscar organizacion
        cursor.execute("SELECT id FROM organizaciones WHERE usuario = %s AND clave = %s", (usuario, clave))
        org = cursor.fetchone()
        
        cursor.close()
        conn.close()
        
        if org:
            print("Org encontrada:", org)
            return jsonify({'success': True, 'tipo': 'organizacion', 'id': org[0]})
        
        print("No encontrada")
        return jsonify({'success': False, 'message': 'Credenciales incorrectas'})
        
    except Exception as e:
        print("Error:", str(e))
        return jsonify({'success': False, 'message': str(e)})
        org = cursor.fetchone()
        
        cursor.close()
        conn.close()
        
        if org:
            return jsonify({'success': True, 'tipo': 'organizacion', 'id': org[0]})
        
        return jsonify({'success': False, 'message': 'Credenciales incorrectas'})
        
    except Exception as e:
        print(f"❌ ERROR EN LOGIN: {e}")
        return jsonify({'success': False, 'message': f'Error del servidor: {e}'})

@app.route('/api/organizaciones', methods=['GET', 'POST'])
def api_organizaciones():
    print(f"✨ REQUEST RECIBIDO: {request.method} /api/organizaciones")
    
    try:
        conn = get_db_connection()
        cursor = conn.cursor()
        
        if request.method == 'GET':
            medicina_id = request.args.get('medicina_id')
            print(f"📋 Parámetro medicina_id recibido: {medicina_id}")
            
            if medicina_id:
                # Filtrar organizaciones que tienen esta medicina en inventario
                query = "SELECT DISTINCT o.id, o.nombre, o.latitud, o.longitud, o.usuario FROM organizaciones o INNER JOIN inventario i ON o.id = i.id_organizacion WHERE o.activa = 1 AND i.id_medicina = %s AND i.cantidad > 0"
                print(f"🔍 Ejecutando query con filtro de medicina_id={medicina_id}")
                cursor.execute(query, (medicina_id,))
            else:
                # Todas las organizaciones activas
                print(f"🔍 Ejecutando query SIN filtro (todas las organizaciones)")
                cursor.execute("SELECT id, nombre, latitud, longitud, usuario FROM organizaciones WHERE activa = 1")
            
            orgs = cursor.fetchall()
            print(f"✅ Organizaciones encontradas: {len(orgs)}")
            cursor.close()
            conn.close()
            
            return jsonify([{
                'id': org[0],
                'nombre': org[1],
                'latitud': float(org[2]),
                'longitud': float(org[3]),
                'usuario': org[4],
                'activo': True
            } for org in orgs])
        
        elif request.method == 'POST':
            data = request.get_json()
            cursor.execute(
                "INSERT INTO organizaciones (latitud, longitud, nombre, usuario, clave, activa) VALUES (%s, %s, %s, %s, %s, 1)",
                (data['latitud'], data['longitud'], data['nombre'], data['usuario'], data['clave'])
            )
            conn.commit()
            org_id = cursor.lastrowid
            cursor.close()
            conn.close()
            
            return jsonify({'success': True, 'message': 'Organización registrada', 'id': org_id})
            
    except Exception as e:
        print(f"❌ ERROR EN ORGANIZACIONES: {e}")
        return jsonify({'success': False, 'message': str(e)})

@app.route('/api/catalogo', methods=['GET', 'POST'])
def api_catalogo():
    try:
        conn = get_db_connection()
        cursor = conn.cursor()
        
        if request.method == 'GET':
            cursor.execute("SELECT id, sustancia_activa, nombre_comercial, presentacion, descripcion FROM catalogo")
            items = cursor.fetchall()
            cursor.close()
            conn.close()
            
            return jsonify([{
                'id': item[0],
                'sustancia_activa': item[1],
                'nombre_comercial': item[2],
                'presentacion': item[3],
                'descripcion': item[4]
            } for item in items])
        
        elif request.method == 'POST':
            data = request.get_json()
            cursor.execute(
                "INSERT INTO catalogo (sustancia_activa, nombre_comercial, presentacion, descripcion) VALUES (%s, %s, %s, %s)",
                (data['sustancia_activa'], data['nombre_comercial'], data['presentacion'], data['descripcion'])
            )
            conn.commit()
            item_id = cursor.lastrowid
            cursor.close()
            conn.close()
            
            return jsonify({'success': True, 'message': 'Medicina agregada al catálogo', 'id': item_id})
            
    except Exception as e:
        return jsonify({'success': False, 'message': str(e)})

@app.route('/api/inventario', methods=['GET', 'POST'])
def api_inventario():
    try:
        conn = get_db_connection()
        cursor = conn.cursor()
        
        if request.method == 'GET':
            org_id = request.args.get('organizacion_id')
            if org_id:
                cursor.execute(
                    "SELECT i.id, c.nombre_comercial, c.sustancia_activa, i.cantidad, i.fecha_caducidad FROM inventario i JOIN catalogo c ON i.id_medicina = c.id WHERE i.id_organizacion = %s",
                    (org_id,)
                )
            else:
                cursor.execute(
                    "SELECT i.id, c.nombre_comercial, c.sustancia_activa, i.cantidad, i.fecha_caducidad, o.nombre FROM inventario i JOIN catalogo c ON i.id_medicina = c.id JOIN organizaciones o ON i.id_organizacion = o.id"
                )
            
            items = cursor.fetchall()
            cursor.close()
            conn.close()
            
            if org_id:
                return jsonify([{
                    'id': item[0],
                    'nombre_comercial': item[1],
                    'sustancia_activa': item[2],
                    'cantidad': item[3],
                    'fecha_caducidad': str(item[4]) if item[4] else None
                } for item in items])
            else:
                return jsonify([{
                    'id': item[0],
                    'nombre_comercial': item[1],
                    'sustancia_activa': item[2],
                    'cantidad': item[3],
                    'fecha_caducidad': str(item[4]) if item[4] else None,
                    'organizacion': item[5]
                } for item in items])
        
        elif request.method == 'POST':
            data = request.get_json()
            cursor.execute(
                "INSERT INTO inventario (id_organizacion, id_medicina, cantidad, fecha_caducidad) VALUES (%s, %s, %s, %s)",
                (data['id_organizacion'], data['id_medicina'], data['cantidad'], data.get('fecha_caducidad'))
            )
            conn.commit()
            inv_id = cursor.lastrowid
            cursor.close()
            conn.close()
            
            return jsonify({'success': True, 'message': 'Inventario actualizado', 'id': inv_id})
            
    except Exception as e:
        return jsonify({'success': False, 'message': str(e)})

@app.route('/api/donaciones', methods=['GET', 'POST'])
def api_donaciones():
    print(f"✨ REQUEST RECIBIDO: {request.method} /api/donaciones")
    
    try:
        conn = get_db_connection()
        cursor = conn.cursor()
        
        if request.method == 'GET':
            cursor.execute(
                "SELECT d.id, c.nombre_comercial, d.cantidad, d.fecha_donacion, d.curp_donatario, o.nombre FROM donaciones d JOIN catalogo c ON d.id_medicina = c.id JOIN organizaciones o ON d.id_organizacion = o.id ORDER BY d.fecha_donacion DESC"
            )
            donaciones = cursor.fetchall()
            cursor.close()
            conn.close()
            
            return jsonify([{
                'id': don[0],
                'medicamento': don[1],
                'cantidad': don[2],
                'fecha_donacion': str(don[3]),
                'curp_donatario': don[4],
                'organizacion': don[5]
            } for don in donaciones])
        
        elif request.method == 'POST':
            data = request.get_json()
            print(f"📦 DATOS DONACION RECIBIDOS: {data}")
            
            if not data:
                return jsonify({'success': False, 'message': 'No se recibieron datos JSON'})
            
            # Validar campos básicos
            required_fields = ['idOrganizacion', 'idMedicina', 'curpDonatario', 'cantidad']
            for field in required_fields:
                if field not in data:
                    return jsonify({'success': False, 'message': f'Campo requerido: {field}'})
            
            # Convertir fecha si existe
            fecha_mysql = '2025-12-31'  # Fecha por defecto
            if 'fechaCaducidad' in data and data['fechaCaducidad']:
                try:
                    fecha_partes = data['fechaCaducidad'].split('/')
                    if len(fecha_partes) == 3:
                        dia, mes, año = fecha_partes
                        fecha_mysql = f"{año}-{mes.zfill(2)}-{dia.zfill(2)}"
                except:
                    fecha_mysql = '2025-12-31'
            
            print(f"📅 Fecha para MySQL: {fecha_mysql}")
            
            # Insertar donacion con fecha de caducidad
            cursor.execute(
                "INSERT INTO donaciones (id_organizacion, id_medicina, curp_donatario, cantidad, fecha_caducidad) VALUES (%s, %s, %s, %s, %s)",
                (data['idOrganizacion'], data['idMedicina'], data['curpDonatario'], data['cantidad'], fecha_mysql)
            )
            don_id = cursor.lastrowid
            print(f"✅ DONACION REGISTRADA CON ID: {don_id}")
            
            # ACTUALIZAR INVENTARIO - Agregar medicina recibida
            cursor.execute(
                "SELECT id, cantidad FROM inventario WHERE id_organizacion = %s AND id_medicina = %s",
                (data['idOrganizacion'], data['idMedicina'])
            )
            inventario_existente = cursor.fetchone()
            
            if inventario_existente:
                # Actualizar cantidad existente
                nueva_cantidad = inventario_existente[1] + data['cantidad']
                cursor.execute(
                    "UPDATE inventario SET cantidad = %s WHERE id = %s",
                    (nueva_cantidad, inventario_existente[0])
                )
                print(f"✅ INVENTARIO ACTUALIZADO: nueva cantidad = {nueva_cantidad}")
            else:
                # Crear nueva entrada en inventario
                cursor.execute(
                    "INSERT INTO inventario (id_organizacion, id_medicina, cantidad, fecha_caducidad) VALUES (%s, %s, %s, %s)",
                    (data['idOrganizacion'], data['idMedicina'], data['cantidad'], fecha_mysql)
                )
                inv_id = cursor.lastrowid
                print(f"✅ INVENTARIO CREADO CON ID: {inv_id}")
            
            conn.commit()
            cursor.close()
            conn.close()
            
            return jsonify({'success': True, 'message': 'Donación registrada e inventario actualizado', 'id': don_id})
            
    except Exception as e:
        print(f"❌ ERROR EN DONACIONES: {e}")
        return jsonify({'success': False, 'message': str(e)})

@app.route('/api/entregas', methods=['GET', 'POST'])
def api_entregas():
    print(f"✨ REQUEST RECIBIDO: {request.method} /api/entregas")
    
    try:
        conn = get_db_connection()
        cursor = conn.cursor()
        
        if request.method == 'GET':
            cursor.execute(
                "SELECT e.id, c.nombre_comercial, e.cantidad, e.fecha_entrega, e.curp_receptor, o.nombre FROM entregas e JOIN catalogo c ON e.id_medicina = c.id JOIN organizaciones o ON e.id_organizacion = o.id ORDER BY e.fecha_entrega DESC"
            )
            entregas = cursor.fetchall()
            cursor.close()
            conn.close()
            
            return jsonify([{
                'id': ent[0],
                'medicamento': ent[1],
                'cantidad': ent[2],
                'fecha_entrega': str(ent[3]),
                'curp_receptor': ent[4],
                'organizacion': ent[5]
            } for ent in entregas])
        
        elif request.method == 'POST':
            data = request.get_json()
            print(f"📦 DATOS ENTREGA RECIBIDOS: {data}")
            
            if not data:
                return jsonify({'success': False, 'message': 'No se recibieron datos JSON'})
            
            # Validar campos básicos
            required_fields = ['id_organizacion', 'id_medicina', 'curp_receptor', 'cantidad']
            for field in required_fields:
                if field not in data:
                    return jsonify({'success': False, 'message': f'Campo requerido: {field}'})
            
            # VERIFICAR INVENTARIO DISPONIBLE
            cursor.execute(
                "SELECT id, cantidad FROM inventario WHERE id_organizacion = %s AND id_medicina = %s",
                (data['id_organizacion'], data['id_medicina'])
            )
            inventario_existente = cursor.fetchone()
            
            if not inventario_existente:
                return jsonify({'success': False, 'message': 'No hay existencias de esta medicina en inventario'})
            
            cantidad_disponible = inventario_existente[1]
            cantidad_entregar = data['cantidad']
            
            if cantidad_entregar > cantidad_disponible:
                return jsonify({'success': False, 'message': f'Cantidad insuficiente. Disponible: {cantidad_disponible}'})
            
            # Insertar entrega
            cursor.execute(
                "INSERT INTO entregas (id_organizacion, id_medicina, cantidad, curp_receptor) VALUES (%s, %s, %s, %s)",
                (data['id_organizacion'], data['id_medicina'], data['cantidad'], data['curp_receptor'])
            )
            ent_id = cursor.lastrowid
            print(f"✅ ENTREGA REGISTRADA CON ID: {ent_id}")
            
            # ACTUALIZAR INVENTARIO - Restar medicina entregada
            nueva_cantidad = cantidad_disponible - cantidad_entregar
            
            if nueva_cantidad > 0:
                cursor.execute(
                    "UPDATE inventario SET cantidad = %s WHERE id = %s",
                    (nueva_cantidad, inventario_existente[0])
                )
                print(f"✅ INVENTARIO ACTUALIZADO: nueva cantidad = {nueva_cantidad}")
            else:
                # Si queda en 0, eliminar del inventario
                cursor.execute(
                    "DELETE FROM inventario WHERE id = %s",
                    (inventario_existente[0],)
                )
                print(f"✅ INVENTARIO ELIMINADO: cantidad llegó a 0")
            
            conn.commit()
            cursor.close()
            conn.close()
            
            return jsonify({'success': True, 'message': 'Entrega registrada e inventario actualizado', 'id': ent_id})
            
    except Exception as e:
        print(f"❌ ERROR EN ENTREGAS: {e}")
        return jsonify({'success': False, 'message': str(e)})

@app.route('/')
def home():
    return '<h1>🐍 Servidor Python - Mexicare</h1><p>Servidor funcionando correctamente</p>'

if __name__ == '__main__':
    print("="*60)
    print("🚀 INICIANDO SERVIDOR PYTHON MEXICARE")
    print("="*60)
    print(f"📊 Configuración DB: {DB_CONFIG}")
    print(f"🌐 Web: http://localhost:5000")
    print(f"📱 Android URL: http://10.0.2.2:5000")
    print("="*60)
    app.run(host='0.0.0.0', port=5000, debug=True)