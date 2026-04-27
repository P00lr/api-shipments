# 📦 Shipment Service API - Documentación para Frontend

**Versión:** 1.0  
**Última actualización:** Abril 2026  
**Base URL:** `http://localhost:8080/api/v1`

---

## 🔐 Configuración Inicial

### Base URL
```
Desarrollo: http://localhost:8080/api/v1
Producción: https://api.shipment.com/api/v1
```

### Autenticación (JWT Bearer Token)

Todos los endpoints (excepto `/auth/**`) requieren autenticación.

**Header requerido:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Obtener token:**
```bash
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "usuario",
  "password": "contraseña"
}
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 3600
}
```

---

## 📍 Mapa de Endpoints

### Autenticación (Público)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/auth/login` | Iniciar sesión |
| `POST` | `/auth/refresh` | Refrescar token JWT |
| `POST` | `/auth/register` | Registrar nuevo usuario |

### Envíos (Shipments)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/shipments` | Listar envíos paginados |
| `GET` | `/shipments/{id}` | Obtener envío por ID |
| `GET` | `/shipments/suggestions` | Buscar sugerencias de envíos |
| `POST` | `/shipments` | Crear nuevo envío |
| `PATCH` | `/shipments/{id}/deliver` | Marcar envío como entregado |
| `DELETE` | `/shipments/{id}` | Cancelar envío |

### Personas (Persons)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/persons` | Listar personas |
| `GET` | `/persons/{id}` | Obtener persona por ID |
| `POST` | `/persons` | Crear persona |
| `PUT` | `/persons/{id}` | Actualizar persona |
| `DELETE` | `/persons/{id}` | Eliminar persona |

### Usuarios (Users)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/users` | Listar usuarios |
| `GET` | `/users/{id}` | Obtener usuario por ID |
| `POST` | `/users` | Crear usuario |
| `PUT` | `/users/{id}` | Actualizar usuario |
| `DELETE` | `/users/{id}` | Eliminar usuario |

### Oficinas (Offices)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/offices` | Listar oficinas |
| `GET` | `/offices/{id}` | Obtener oficina por ID |
| `POST` | `/offices` | Crear oficina |
| `PUT` | `/offices/{id}` | Actualizar oficina |

### Roles & Permisos
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/roles` | Listar roles |
| `POST` | `/roles` | Crear rol |
| `GET` | `/permissions` | Listar permisos |

### Cooperativas
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/cooperatives` | Listar cooperativas |
| `GET` | `/cooperatives/{id}` | Obtener cooperativa |
| `POST` | `/cooperatives` | Crear cooperativa |
| `PUT` | `/cooperatives/{id}` | Actualizar cooperativa |

---

## 📋 Contratos de API

### 1. ENVÍOS - Crear Nuevo Envío

**Endpoint:** `POST /shipments`

**Request:**
```json
{
  "destinationOfficeId": "550e8400-e29b-41d4-a716-446655440000",
  "itemDescription": "Caja de herramientas",
  "shippingCost": 50.00,
  "sender": {
    "documentType": "CI",
    "documentNumber": "1234567",
    "fullName": "Juan Pérez",
    "phone": "71234567"
  },
  "recipient": {
    "documentType": "CI",
    "documentNumber": "7654321",
    "fullName": "María García",
    "phone": "72345678"
  }
}
```

**Response:** `201 Created`
```json
{
  "id": "38e7c241-ed7e-4b67-a437-50c0f15fd611",
  "trackingCode": "5T-G6U",
  "internalCode": "SHP-20260424-000001",
  "itemDescription": "Caja de herramientas",
  "shippingCost": 50.00,
  "status": "REGISTERED",
  "nameOriginOffice": "San Ramon",
  "nameDestinationOffice": "Santa Cruz",
  "sender": {
    "documentType": "CI",
    "documentNumber": "1234567",
    "fullName": "Juan Pérez",
    "phone": "71234567"
  },
  "recipient": {
    "documentType": "CI",
    "documentNumber": "7654321",
    "fullName": "María García",
    "phone": "72345678"
  },
  "createdBy": "Paul Rodrigo",
  "createAt": "2026-04-24T13:45:00",
  "status": "REGISTERED"
}
```

---

### 2. ENVÍOS - Listar Paginado

**Endpoint:** `GET /shipments?page=0&size=10&sort=createdAt,desc`

**Parameters:**
- `page` (query, default: 0) - Número de página
- `size` (query, default: 20) - Registros por página
- `sort` (query, default: createdAt,desc) - Campo de ordenamiento

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": "38e7c241-ed7e-4b67-a437-50c0f15fd611",
      "trackingCode": "5T-G6U",
      "itemDescription": "Caja de herramientas",
      "shippingCost": 50.00,
      "status": "REGISTERED",
      "nameOriginOffice": "San Ramon",
      "nameDestinationOffice": "Santa Cruz",
      "sender": {...},
      "recipient": {...},
      "createdBy": "Paul Rodrigo",
      "createAt": "2026-04-24T13:45:00"
    }
  ],
  "pageNo": 0,
  "size": 10,
  "totalElements": 45,
  "totalPages": 5,
  "first": true,
  "last": false,
  "hasNext": true,
  "hasPrevious": false
}
```

---

### 3. ENVÍOS - Buscar Sugerencias

**Endpoint:** `GET /shipments/suggestions?term=7654321&page=0&size=5`

Búsqueda de envíos por: **tracking code**, **número de documento** o **celular** del destinatario.

**Parameters:**
- `term` (query, required) - Término de búsqueda
- `page` (query, default: 0)
- `size` (query, default: 5, máximo: 5)

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": "38e7c241-ed7e-4b67-a437-50c0f15fd611",
      "trackingCode": "5T-G6U",
      "itemDescription": "Caja de herramientas",
      "fullName": "María García",
      "documentNumber": "7654321",
      "phone": "72345678",
      "status": "REGISTERED",
      "shippingCost": 50.00,
      "createdAt": "2026-04-24T13:45:00"
    }
  ],
  "pageNo": 0,
  "size": 5,
  "totalElements": 3,
  "totalPages": 1,
  "first": true,
  "last": true
}
```

---

### 4. ENVÍOS - Marcar como Entregado

**Endpoint:** `PATCH /shipments/{id}/deliver`

**Path Parameters:**
- `id` (UUID) - ID del envío

**Request Body:**
```json
{
  "documentNumber": "7654321"
}
```

**Response:** `200 OK`
```json
{
  "id": "38e7c241-ed7e-4b67-a437-50c0f15fd611",
  "trackingCode": "5T-G6U",
  "status": "DELIVERED",
  "deliveredAt": "2026-04-24T14:30:00",
  "...": "otros campos..."
}
```

---

### 5. USUARIOS - Crear Usuario

**Endpoint:** `POST /users`

**Request:**
```json
{
  "username": "jperez",
  "email": "jperez@example.com",
  "password": "SecurePass123!",
  "person": {
    "documentType": "CI",
    "documentNumber": "1234567",
    "fullName": "Juan Pérez",
    "phone": "71234567"
  },
  "officeId": "550e8400-e29b-41d4-a716-446655440000",
  "roleIds": ["550e8400-e29b-41d4-a716-446655440001"]
}
```

**Response:** `201 Created`
```json
{
  "id": "660e8400-e29b-41d4-a716-446655440000",
  "username": "jperez",
  "email": "jperez@example.com",
  "person": {
    "id": "770e8400-e29b-41d4-a716-446655440000",
    "fullName": "Juan Pérez",
    "documentNumber": "1234567",
    "documentType": "CI"
  },
  "office": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "San Ramon"
  },
  "roles": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440001",
      "name": "USER"
    }
  ]
}
```

---

### 6. PERSONAS - Crear Persona

**Endpoint:** `POST /persons`

**Request:**
```json
{
  "documentType": "CI",
  "documentNumber": "1234567",
  "fullName": "Juan Pérez López",
  "phone": "71234567"
}
```

**Response:** `201 Created`
```json
{
  "id": "770e8400-e29b-41d4-a716-446655440000",
  "documentType": "CI",
  "documentNumber": "1234567",
  "fullName": "Juan Pérez López",
  "phone": "71234567",
  "registered": "2026-04-24T13:45:00"
}
```

---

## ⚠️ Manejo de Errores

### Códigos HTTP y Significados

| Código | Significado | Acción Frontend |
|--------|-------------|-----------------|
| `200` | OK - Solicitud exitosa | Procesar respuesta normalmente |
| `201` | Created - Recurso creado | Mostrar éxito, redirigir si aplica |
| `400` | Bad Request - Datos inválidos | Mostrar errores de validación |
| `401` | Unauthorized - Sin autenticación | Redirigir a login |
| `403` | Forbidden - Sin permisos | Mostrar mensaje de acceso denegado |
| `404` | Not Found - Recurso no existe | Mostrar "recurso no encontrado" |
| `409` | Conflict - Recurso duplicado | Mostrar error de duplicado |
| `500` | Server Error - Error interno | Mostrar error genérico, contactar soporte |

### Estructura de Error

**Todos los errores retornan:**
```json
{
  "timestamp": "2026-04-24T14:30:00.123456",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "details": [
    {
      "field": "documentNumber",
      "message": "Document number must be 7-8 digits",
      "rejectedValue": "12345"
    },
    {
      "field": "phone",
      "message": "Phone must be 8 digits",
      "rejectedValue": "1234"
    }
  ],
  "path": "/api/v1/shipments"
}
```

### Errores Comunes

**401 - No Autenticado:**
```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "JWT token has expired or is invalid",
  "timestamp": "2026-04-24T14:30:00"
}
```

**403 - Sin Permisos:**
```json
{
  "status": 403,
  "error": "Forbidden",
  "message": "You don't have permission to access this resource",
  "timestamp": "2026-04-24T14:30:00"
}
```

**404 - No Encontrado:**
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Shipment with ID 38e7c241-ed7e-4b67-a437-50c0f15fd611 not found",
  "timestamp": "2026-04-24T14:30:00"
}
```

**409 - Conflicto (Duplicado):**
```json
{
  "status": 409,
  "error": "Conflict",
  "message": "Username 'jperez' already exists",
  "timestamp": "2026-04-24T14:30:00"
}
```

---

## 🔄 Consideraciones de Flujo

### 1. Flujo de Creación de Envío

**Orden de operaciones:**

1. **Validar que la oficina destino existe**
   ```
   GET /offices/{destinationOfficeId}
   ```

2. **Crear o validar personas (sender/recipient)**
   - Si es nuevo: `POST /persons`
   - Si existe: Se valida automáticamente

3. **Crear el envío**
   ```
   POST /shipments
   ```
   → Backend automáticamente asigna:
   - `trackingCode` (único, aleatorio)
   - `internalCode` (basado en fecha + secuencia)
   - `status: REGISTERED`
   - `originOffice` (del usuario autenticado)

4. **Respuesta incluye tracking code** → Compartir con cliente

### 2. Flujo de Entrega

**Orden obligatorio:**

1. **Buscar envío**
   ```
   GET /shipments/{id} O /shipments/suggestions?term=...
   ```

2. **Validar estado**
   - Solo si `status === "REGISTERED"`
   - Si es `DELIVERED` → Error 409
   - Si es `CANCELED` → Error 409

3. **Marcar como entregado**
   ```
   PATCH /shipments/{id}/deliver
   Con documentNumber del recipient
   ```

4. **Validación automática:**
   - Backend valida que documentNumber coincida con recipient
   - Si no coincide → Error 400

### 3. Flujo de Búsqueda

**Recomendación para buscador en tiempo real:**

```
GET /shipments/suggestions?term={valor}&page=0&size=5
```

- **Máximo 5 resultados por búsqueda**
- **Pagina automáticamente**
- **Busca en estos campos:**
  - Tracking code (ej: `5T-G6U`)
  - Número de documento del recipient (ej: `7654321`)
  - Celular del recipient (ej: `72345678`)

---

## 🚫 Restricciones y Limitaciones

### Validación de Datos

**CI/Documento de Identidad:**
- Patrón: `\d{7,8}(-[a-zA-Z]{1,2})?`
- Ejemplos válidos: `1234567`, `12345678`, `1234567-A`, `12345678-AB`

**Teléfono:**
- Patrón: `\d{8}` (exactamente 8 dígitos)
- Ejemplo: `71234567`

**Nombre Completo:**
- Mínimo 3 caracteres
- Máximo 100 caracteres

**Costo de Envío:**
- Mínimo: 0
- Máximo: 9999.99
- 2 decimales

**Código de Seguimiento:**
- Generado automáticamente
- Formato: `XX-YYY` (5 caracteres, letras y números)
- Único en la base de datos

### Límites Operacionales

- **Paginación máxima por página:** 100 registros
- **Sugerencias máximas:** 5 resultados
- **Token JWT:** Válido por 1 hora
- **Reintentos fallidos:** 5 intentos máximo antes de lockout (10 minutos)

### Estado de Envíos

Solo dos estados posibles:
- `REGISTERED` - Envío registrado, puede ser entregado o cancelado
- `DELIVERED` - Envío entregado, no se puede modificar
- `CANCELED` - Envío cancelado, no se puede entregar

Una vez en `DELIVERED` o `CANCELED` **no hay regresión de estado**.

---

## 📝 Notas Importantes

1. **Token JWT:** Incluir siempre en el header `Authorization: Bearer <token>`
2. **CORS:** Solo origen `http://localhost:5173` en desarrollo
3. **Content-Type:** Siempre `application/json`
4. **IDs:** Todos los IDs de recurso son UUID (formato estándar)
5. **Timestamps:** ISO 8601 con zona horaria
6. **Paginación:** 0-indexed (primera página es 0)

---

**Para soporte técnico:** Contactar al equipo de backend  
**Última revisión:** Abril 24, 2026
