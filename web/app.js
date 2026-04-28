// ===== CONFIGURACIÓN =====
const API_BASE = 'http://localhost:5000';

// ===== API =====
const API = {
    async get(url) {
        const res = await fetch(API_BASE + url);
        return await res.json();
    },
    
    async post(url, data) {
        const res = await fetch(API_BASE + url, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        });
        return await res.json();
    },
    
    login(usuario, clave) {
        return this.post('/api/login', {usuario, clave});
    },
    
    getCatalogo() {
        return this.get('/api/catalogo');
    },
    
    getInventario(orgId) {
        if (orgId) return this.get('/api/inventario?organizacion_id=' + orgId);
        return this.get('/api/inventario');
    },
    
    getOrganizaciones() {
        return this.get('/api/organizaciones');
    },
    
    getDonaciones() {
        return this.get('/api/donaciones');
    },
    
    getEntregas() {
        return this.get('/api/entregas');
    }
};

// ===== NAVEGACIÓN =====
function navigateTo(page) {
    document.querySelectorAll('[id^="page-"]').forEach(p => p.style.display = 'none');
    const target = document.getElementById('page-' + page);
    if (target) target.style.display = 'block';
    window.scrollTo(0, 0);
}

function goBack() {
    navigateTo('home');
}

function switchTab(el, tabName) {
    el.closest('.tabs-container').querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
    el.classList.add('active');
    document.querySelectorAll('.tab-content').forEach(c => c.style.display = 'none');
    document.getElementById('tab-' + tabName).style.display = 'block';
}

// ===== LOGIN =====
async function doLogin() {
    const u = document.getElementById('login-usuario').value;
    const p = document.getElementById('login-password').value;
    
    if (!u || !p) {
        alert('Ingresa usuario y contraseña'); return;
    }
    
    try {
        const r = await API.login(u, p);
        console.log('Login:', r);
        
        if (r.success) {
            navigateTo(r.tipo === 'admin' ? 'admin' : 'org-dashboard');
        } else {
            alert(r.message || 'Credenciales incorrectas');
        }
    } catch (e) {
        alert('Error: ' + e.message);
    }
}

// ===== CARGAR CATÁLOGO =====
async function loadCatalogo() {
    try {
        const meds = await API.getCatalogo();
        console.log('Catalogo:', meds);
        
        document.querySelectorAll('select.form-input').forEach(sel => {
            if (sel.children.length <= 1) {
                meds.forEach(m => {
                    const opt = document.createElement('option');
                    opt.value = m.id;
                    opt.textContent = m.nombre_comercial + ' - ' + m.presentacion;
                    sel.appendChild(opt);
                });
            }
        });
    } catch(e) {
        console.error('Error catalogo:', e);
    }
}

// ===== INVENTARIO GLOBAL =====
async function loadInventarioGlobal() {
    try {
        const items = await API.getInventario();
        console.log('Inventario:', items);
        
        const porOrg = {};
        items.forEach(item => {
            const org = item.organizacion || 'Otro';
            if (!porOrg[org]) porOrg[org] = [];
            porOrg[org].push(item);
        });
        
        const container = document.querySelector('.inventory-cards');
        if (container) {
            container.innerHTML = '';
            Object.keys(porOrg).forEach(org => {
                const itemsOrg = porOrg[org];
                const total = itemsOrg.reduce((s, i) => s + i.cantidad, 0);
                
                let html = itemsOrg.map(i => `
                    <div class="inv-item">
                        <span class="inv-name">${i.nombre_comercial}</span>
                        <span class="inv-qty">${i.cantidad} unidades</span>
                    </div>
                `).join('');
                
                container.innerHTML += `
                    <div class="inventory-card">
                        <div class="inv-header">
                            <span class="inv-org">🏥 ${org}</span>
                            <span class="inv-total">${total} unidades</span>
                        </div>
                        <div class="inv-items">${html}</div>
                    </div>
                `;
            });
            
            const badge = document.querySelector('#tab-inventario .badge');
            if (badge) badge.textContent = Object.keys(porOrg).length + ' orgs';
        }
    } catch(e) {
        console.error('Error inventario:', e);
    }
}

// ===== ORGANIZACIONES =====
async function loadOrganizaciones() {
    try {
        const orgs = await API.getOrganizaciones();
        console.log('Organizaciones:', orgs);
        
        const lista = document.querySelector('.org-list');
        if (lista) {
            lista.innerHTML = '';
            orgs.forEach(o => {
                lista.innerHTML += `
                    <div class="org-list-item" onclick="alert('${o.nombre}')">
                        <span class="org-list-icon">🏥</span>
                        <div class="org-list-info">
                            <h4>${o.nombre}</h4>
                            <p>${o.usuario}</p>
                        </div>
                        <div class="org-list-status available">Abierto</div>
                    </div>
                `;
            });
        }
    } catch(e) {
        console.error('Error orgs:', e);
    }
}

// ===== INICIALIZACIÓN =====
document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('page-home').style.display = 'block';
    loadCatalogo();
    loadOrganizaciones();
});

// ===== EFECTOS =====
document.querySelectorAll('.main-card').forEach(c => {
    c.addEventListener('mouseenter', () => c.querySelector('.card-icon').style.transform = 'scale(1.1)');
    c.addEventListener('mouseleave', () => c.querySelector('.card-icon').style.transform = 'scale(1)');
});