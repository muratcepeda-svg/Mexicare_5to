// ===== CONFIGURACIÓN =====
const API_BASE = 'http://localhost:5000';

// ===== API CLIENT =====
const API = {
    async request(url, options = {}) {
        try {
            const res = await fetch(API_BASE + url, {
                ...options,
                headers: {
                    'Content-Type': 'application/json',
                    ...options.headers
                }
            });
            const text = await res.text();
            return text ? JSON.parse(text) : {};
        } catch (e) {
            console.error('API Error:', e);
            return { success: false, message: e.message };
        }
    },

    login(usuario, clave) {
        return this.request('/api/login', {
            method: 'POST',
            body: JSON.stringify({ usuario, clave })
        });
    },

    getCatalogo() {
        return this.request('/api/catalogo');
    },

    getInventario(orgId) {
        const url = orgId ? '/api/inventario?organizacion_id=' + orgId : '/api/inventario';
        return this.request(url);
    },

    getOrganizaciones() {
        return this.request('/api/organizaciones');
    },

    getDonaciones() {
        return this.request('/api/donaciones');
    },

    getEntregas() {
        return this.request('/api/entregas');
    },

    registrarOrganizacion(data) {
        return this.request('/api/organizaciones', {
            method: 'POST',
            body: JSON.stringify(data)
        });
    }
};

console.log('API configurada:', API_BASE);