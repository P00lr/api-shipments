# EJEMPLOS DE USO - Shipment Service API

## 📋 Tabla de Contenidos
1. Flujos de Autenticación
2. Operaciones de Usuarios
3. Operaciones de Personas
4. Operaciones de Oficinas
5. Operaciones de Cooperativas
6. Operaciones de Envíos
7. Operaciones de Roles
8. Casos de Error

---

## 🔐 1. AUTENTICACIÓN

### 1.1 Registro de Nuevo Usuario

**Request:**
```http
POST /api/auth/register
Content-Type: application/json

{
  "person": {
    "documentType": "CI",
    "documentNumber": "12345678",
    "fullName": "Juan Pérez García",
    "phone": "77712345678"
  },
  "username": "juanperez",
  "password": "Pass1234#",
  "email": "juan.perez@email.com",
  "officeId": "3fa85f64-5717-4562-b3fc-2c963f66afa6"
}
```

**Response (201 Created):**
```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "documentType": "CI",
  "documentNumber": "12345678",
  "fullName": "Juan Pérez García",
  "phone": "77712345678",
  "username": "juanperez",
  "email": "juan.perez@email.com",
  "officeName": "Oficina Central",
  "active": true
}
```

---

### 1.2 Login

**Request:**
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "juanperez",
  "password": "Pass1234#"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqdWFucGVyZXoiLCJpYXQiOjE3MzE0MjM0MDAsImV4cCI6MTczMTQyNzAwMCwidXVpZCI6IjNmYTg1ZjY0LTU3MTctNDU2Mi1iM2ZjLTJjOTYzZjY2YWZhNiIsIm9mZmljZUlkIjoiODZjODVmNjQtNTcxNy00NTYyLWEzZmMtMmM5NjNmNjZhZmE2Iiwicm9sZXMiOlsiUk9MRV9VU0VSIl19.example_signature"
}
```

**Cómo usar el token:**
```http
GET /api/v1/users/3fa85f64-5717-4562-b3fc-2c963f66afa6
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

### 1.3 Logout

**Request:**
```http
POST /api/auth/logout
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response (204 No Content):**
```
(Sin cuerpo)
```

---

## 👥 2. USUARIOS

### 2.1 Crear Usuario (ADMIN)

**Request:**
```http
POST /api/v1/users
Authorization: Bearer <ADMIN_TOKEN>
Content-Type: application/json

{
  "person": {
    "documentType": "CI",
    "documentNumber": "87654321",
    "fullName": "María López Rodríguez",
    "phone": "77787654321"
  },
  "username": "marialopez",
  "password": "MySecure@123",
  "email": "maria.lopez@example.com",
  "officeId": "8fb92f64-6717-4562-b3fc-2c963f66afa6"
}
```

**Response (200 OK):**
```json
{
  "id": "4fa85f64-5717-4562-b3fc-2c963f66afa7",
  "documentType": "CI",
  "documentNumber": "87654321",
  "fullName": "María López Rodríguez",
  "phone": "77787654321",
  "username": "marialopez",
  "email": "maria.lopez@example.com",
  "officeName": "Sucursal Norte",
  "active": true
}
```

---

### 2.2 Obtener Usuarios Paginados

**Request:**
```http
GET /api/v1/users/paged?pageNo=0&size=10&sortBy=username
Authorization: Bearer <ADMIN_TOKEN>
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "documentType": "CI",
      "documentNumber": "12345678",
      "fullName": "Juan Pérez García",
      "phone": "77712345678",
      "username": "juanperez",
      "email": "juan.perez@email.com",
      "officeName": "Oficina Central",
      "active": true
    },
    {
      "id": "4fa85f64-5717-4562-b3fc-2c963f66afa7",
      "documentType": "CI",
      "documentNumber": "87654321",
      "fullName": "María López Rodríguez",
      "phone": "77787654321",
      "username": "marialopez",
      "email": "maria.lopez@example.com",
      "officeName": "Sucursal Norte",
      "active": true
    }
  ],
  "pageNo": 0,
  "size": 10,
  "totalElements": 2,
  "totalPages": 1,
  "first": true,
  "last": true,
  "hasNext": false,
  "hasPrevious": false
}
```

---

### 2.3 Actualizar Usuario

**Request:**
```http
PUT /api/v1/users/3fa85f64-5717-4562-b3fc-2c963f66afa6
Authorization: Bearer <ADMIN_TOKEN>
Content-Type: application/json

{
  "person": {
    "documentType": "CI",
    "documentNumber": "12345678",
    "fullName": "Juan Pérez García López",
    "phone": "77712345679"
  },
  "username": "juanperez",
  "email": "juan.perez.updated@email.com"
}
```

**Response (200 OK):**
```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "documentType": "CI",
  "documentNumber": "12345678",
  "fullName": "Juan Pérez García López",
  "phone": "77712345679",
  "username": "juanperez",
  "email": "juan.perez.updated@email.com",
  "officeName": "Oficina Central",
  "active": true
}
```

---

### 2.4 Cambiar Contraseña

**Request:**
```http
PUT /api/v1/users/password/3fa85f64-5717-4562-b3fc-2c963f66afa6
Authorization: Bearer <ADMIN_TOKEN>
Content-Type: application/json

{
  "oldPassword": "Pass1234#",
  "newPassword": "NewPass123!"
}
```

**Response (200 OK):**
```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "documentType": "CI",
  "documentNumber": "12345678",
  "fullName": "Juan Pérez García",
  "phone": "77712345678",
  "username": "juanperez",
  "email": "juan.perez@email.com",
  "officeName": "Oficina Central",
  "active": true
}
```

---

## 👤 3. PERSONAS

### 3.1 Crear Persona

**Request:**
```http
POST /api/v1/persons
Authorization: Bearer <ADMIN_TOKEN>
Content-Type: application/json

{
  "documentType": "CI",
  "documentNumber": "99887766",
  "fullName": "Carlos Mendoza Aguirre",
  "phone": "77766554433"
}
```

**Response (201 Created):**
```json
{
  "id": "5fa85f64-5717-4562-b3fc-2c963f66afa8",
  "documentType": "CI",
  "documentNumber": "99887766",
  "fullName": "Carlos Mendoza Aguirre",
  "phone": "77766554433",
  "registered": false,
  "active": true
}
```

---

### 3.2 Buscar Persona por Número de Documento

**Request:**
```http
GET /api/v1/persons/ci/99887766
Authorization: Bearer <ADMIN_TOKEN>
```

**Response (200 OK):**
```json
{
  "id": "5fa85f64-5717-4562-b3fc-2c963f66afa8",
  "documentType": "CI",
  "documentNumber": "99887766",
  "fullName": "Carlos Mendoza Aguirre",
  "phone": "77766554433",
  "registered": false,
  "active": true
}
```

---

### 3.3 Verificar Existencia por Teléfono

**Request:**
```http
GET /api/v1/persons/phone/77766554433
Authorization: Bearer <ADMIN_TOKEN>
```

**Response (200 OK):**
```json
true
```

---

## 🏢 4. OFICINAS

### 4.1 Crear Oficina

**Request:**
```http
POST /api/v1/offices
Authorization: Bearer <ADMIN_TOKEN>
Content-Type: application/json

{
  "name": "Oficina Este",
  "address": "Calle Montes 789, Apto 10",
  "phone": "77799998888"
}
```

**Response (201 OK):**
```json
{
  "id": "6fa85f64-5717-4562-b3fc-2c963f66afa9",
  "name": "Oficina Este",
  "address": "Calle Montes 789, Apto 10",
  "phone": "77799998888",
  "active": true
}
```

---

### 4.2 Obtener Oficinas Paginadas

**Request:**
```http
GET /api/v1/offices/paged?page=0&size=20&sort=name,asc
Authorization: Bearer <ADMIN_TOKEN>
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "name": "Oficina Central",
      "address": "Av. Siempre Viva 123",
      "phone": "77712345678",
      "active": true
    },
    {
      "id": "8fb92f64-6717-4562-b3fc-2c963f66afa6",
      "name": "Sucursal Norte",
      "address": "Av. Nueva 456",
      "phone": "77787654321",
      "active": true
    },
    {
      "id": "6fa85f64-5717-4562-b3fc-2c963f66afa9",
      "name": "Oficina Este",
      "address": "Calle Montes 789, Apto 10",
      "phone": "77799998888",
      "active": true
    }
  ],
  "pageNo": 0,
  "size": 20,
  "totalElements": 3,
  "totalPages": 1,
  "first": true,
  "last": true,
  "hasNext": false,
  "hasPrevious": false
}
```

---

### 4.3 Actualizar Oficina

**Request:**
```http
PUT /api/v1/offices/3fa85f64-5717-4562-b3fc-2c963f66afa6
Authorization: Bearer <ADMIN_TOKEN>
Content-Type: application/json

{
  "name": "Oficina Central - Sede Principal",
  "address": "Av. Siempre Viva 123, Piso 5",
  "phone": "77712345679"
}
```

**Response (200 OK):**
```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "name": "Oficina Central - Sede Principal",
  "address": "Av. Siempre Viva 123, Piso 5",
  "phone": "77712345679",
  "active": true
}
```

---

## 🚚 5. COOPERATIVAS DE TRANSPORTE

### 5.1 Crear Cooperativa

**Request:**
```http
POST /api/v1/transportCooperatives
Authorization: Bearer <ADMIN_TOKEN>
Content-Type: application/json

{
  "name": "Transportes Unión Central",
  "enabled": true
}
```

**Response (201 Created):**
```json
{
  "id": "7fa85f64-5717-4562-b3fc-2c963f66afa10",
  "name": "Transportes Unión Central",
  "enabled": true
}
```

---

### 5.2 Asignar Oficinas a Cooperativa

**Request:**
```http
POST /api/v1/transportCooperatives/assignOffices
Authorization: Bearer <ADMIN_TOKEN>
Content-Type: application/json

{
  "cooperativeId": "7fa85f64-5717-4562-b3fc-2c963f66afa10",
  "officesId": [
    "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "8fb92f64-6717-4562-b3fc-2c963f66afa6",
    "6fa85f64-5717-4562-b3fc-2c963f66afa9"
  ]
}
```

**Response (200 OK):**
```json
{
  "cooperativeId": "7fa85f64-5717-4562-b3fc-2c963f66afa10",
  "cooperativeName": "Transportes Unión Central",
  "officeResponseDtos": [
    {
      "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "name": "Oficina Central",
      "address": "Av. Siempre Viva 123",
      "phone": "77712345678",
      "active": true
    },
    {
      "id": "8fb92f64-6717-4562-b3fc-2c963f66afa6",
      "name": "Sucursal Norte",
      "address": "Av. Nueva 456",
      "phone": "77787654321",
      "active": true
    },
    {
      "id": "6fa85f64-5717-4562-b3fc-2c963f66afa9",
      "name": "Oficina Este",
      "address": "Calle Montes 789, Apto 10",
      "phone": "77799998888",
      "active": true
    }
  ]
}
```

---

### 5.3 Obtener Cooperativa con sus Oficinas

**Request:**
```http
GET /api/v1/transportCooperatives/7fa85f64-5717-4562-b3fc-2c963f66afa10/whith-offices
Authorization: Bearer <ADMIN_TOKEN>
```

**Response (200 OK):**
```json
{
  "cooperativeId": "7fa85f64-5717-4562-b3fc-2c963f66afa10",
  "cooperativeName": "Transportes Unión Central",
  "offices": [
    {
      "officeId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "officeName": "Oficina Central"
    },
    {
      "officeId": "8fb92f64-6717-4562-b3fc-2c963f66afa6",
      "officeName": "Sucursal Norte"
    },
    {
      "officeId": "6fa85f64-5717-4562-b3fc-2c963f66afa9",
      "officeName": "Oficina Este"
    }
  ]
}
```

---

## 📦 6. ENVÍOS

### 6.1 Crear Envío

**Request:**
```http
POST /api/v1/shipments
Authorization: Bearer <ADMIN_TOKEN>
Content-Type: application/json

{
  "destinationOfficeId": "8fb92f64-6717-4562-b3fc-2c963f66afa6",
  "sender": {
    "documentType": "CI",
    "documentNumber": "12345678",
    "fullName": "Juan Pérez García",
    "phone": "77712345678"
  },
  "recipient": {
    "documentType": "CI",
    "documentNumber": "87654321",
    "fullName": "María López Rodríguez",
    "phone": "77787654321"
  },
  "itemDescription": "Documentos importantes - Contrato laboral",
  "shippingCost": 150.50
}
```

**Response (200 OK):**
```json
{
  "id": "8fa85f64-5717-4562-b3fc-2c963f66afa11",
  "nameOriginOffice": "Oficina Central",
  "nameDestinationOffice": "Sucursal Norte",
  "createdBy": "juanperez",
  "sender": {
    "documentType": "CI",
    "documentNumber": "12345678",
    "fullName": "Juan Pérez García",
    "phone": "77712345678"
  },
  "recipient": {
    "documentType": "CI",
    "documentNumber": "87654321",
    "fullName": "María López Rodríguez",
    "phone": "77787654321"
  },
  "receivedBy": null,
  "itemDescription": "Documentos importantes - Contrato laboral",
  "trackingCode": "AB3-C4D-EF5",
  "createAt": "2025-11-12T14:30:00",
  "updateAt": null,
  "deliveredAt": null,
  "shippingCost": 150.50,
  "status": "PENDING"
}
```

---

### 6.2 Obtener Envío por ID

**Request:**
```http
GET /api/v1/shipments/8fa85f64-5717-4562-b3fc-2c963f66afa11
Authorization: Bearer <TOKEN>
```

**Response (200 OK):**
```json
{
  "id": "8fa85f64-5717-4562-b3fc-2c963f66afa11",
  "nameOriginOffice": "Oficina Central",
  "nameDestinationOffice": "Sucursal Norte",
  "createdBy": "juanperez",
  "sender": {
    "documentType": "CI",
    "documentNumber": "12345678",
    "fullName": "Juan Pérez García",
    "phone": "77712345678"
  },
  "recipient": {
    "documentType": "CI",
    "documentNumber": "87654321",
    "fullName": "María López Rodríguez",
    "phone": "77787654321"
  },
  "receivedBy": null,
  "itemDescription": "Documentos importantes - Contrato laboral",
  "trackingCode": "AB3-C4D-EF5",
  "createAt": "2025-11-12T14:30:00",
  "updateAt": null,
  "deliveredAt": null,
  "shippingCost": 150.50,
  "status": "PENDING"
}
```

---

### 6.3 Buscar Envíos con Autocomplete

**Request:**
```http
GET /api/v1/shipments/suggestions?term=AB3&page=0&size=10
Authorization: Bearer <TOKEN>
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": "8fa85f64-5717-4562-b3fc-2c963f66afa11",
      "trackingCode": "AB3-C4D-EF5",
      "itemDescription": "Documentos importantes - Contrato laboral",
      "fullName": "María López Rodríguez",
      "documentNumber": "87654321",
      "phone": "77787654321",
      "status": "PENDING",
      "shippingCost": 150.50,
      "createdAt": "2025-11-12T14:30:00"
    }
  ],
  "pageNo": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1,
  "first": true,
  "last": true,
  "hasNext": false,
  "hasPrevious": false
}
```

---

### 6.4 Marcar Envío como Entregado

**Request:**
```http
PATCH /api/v1/shipments/8fa85f64-5717-4562-b3fc-2c963f66afa11/deliver
Authorization: Bearer <ADMIN_TOKEN>
Content-Type: application/json

{
  "documentNumber": "87654321"
}
```

**Response (200 OK):**
```json
{
  "id": "8fa85f64-5717-4562-b3fc-2c963f66afa11",
  "nameOriginOffice": "Oficina Central",
  "nameDestinationOffice": "Sucursal Norte",
  "createdBy": "juanperez",
  "sender": {
    "documentType": "CI",
    "documentNumber": "12345678",
    "fullName": "Juan Pérez García",
    "phone": "77712345678"
  },
  "recipient": {
    "documentType": "CI",
    "documentNumber": "87654321",
    "fullName": "María López Rodríguez",
    "phone": "77787654321"
  },
  "receivedBy": {
    "documentType": "CI",
    "documentNumber": "87654321",
    "fullName": "María López Rodríguez",
    "phone": "77787654321"
  },
  "itemDescription": "Documentos importantes - Contrato laboral",
  "trackingCode": "AB3-C4D-EF5",
  "createAt": "2025-11-12T14:30:00",
  "updateAt": "2025-11-13T09:45:00",
  "deliveredAt": "2025-11-13T10:00:00",
  "shippingCost": 150.50,
  "status": "DELIVERED"
}
```

---

### 6.5 Cancelar Envío

**Request:**
```http
DELETE /api/v1/shipments/8fa85f64-5717-4562-b3fc-2c963f66afa11
Authorization: Bearer <ADMIN_TOKEN>
```

**Response (200 OK):**
```json
{
  "id": "8fa85f64-5717-4562-b3fc-2c963f66afa11",
  "nameOriginOffice": "Oficina Central",
  "nameDestinationOffice": "Sucursal Norte",
  "createdBy": "juanperez",
  "sender": {
    "documentType": "CI",
    "documentNumber": "12345678",
    "fullName": "Juan Pérez García",
    "phone": "77712345678"
  },
  "recipient": {
    "documentType": "CI",
    "documentNumber": "87654321",
    "fullName": "María López Rodríguez",
    "phone": "77787654321"
  },
  "receivedBy": null,
  "itemDescription": "Documentos importantes - Contrato laboral",
  "trackingCode": "AB3-C4D-EF5",
  "createAt": "2025-11-12T14:30:00",
  "updateAt": "2025-11-12T15:00:00",
  "deliveredAt": null,
  "shippingCost": 150.50,
  "status": "CANCELLED"
}
```

---

## 🎭 7. ROLES

### 7.1 Crear Rol

**Request:**
```http
POST /api/v1/roles
Authorization: Bearer <ADMIN_TOKEN>
Content-Type: application/json

{
  "name": "SUPERVISOR"
}
```

**Response (201 Created):**
```json
{
  "name": "SUPERVISOR"
}
```

---

### 7.2 Obtener Roles Paginados

**Request:**
```http
GET /api/v1/roles?page=0&size=10
Authorization: Bearer <ADMIN_TOKEN>
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "name": "ADMIN"
    },
    {
      "name": "USER"
    },
    {
      "name": "SUPERVISOR"
    }
  ],
  "pageNo": 0,
  "size": 10,
  "totalElements": 3,
  "totalPages": 1,
  "first": true,
  "last": true,
  "hasNext": false,
  "hasPrevious": false
}
```

---

## ❌ 8. CASOS DE ERROR

### 8.1 Validación Fallida - Email Inválido

**Request:**
```http
POST /api/auth/register
Content-Type: application/json

{
  "person": {
    "documentType": "CI",
    "documentNumber": "11111111",
    "fullName": "Test User",
    "phone": "77712345678"
  },
  "username": "testuser",
  "password": "Pass1234#",
  "email": "invalid-email",
  "officeId": "3fa85f64-5717-4562-b3fc-2c963f66afa6"
}
```

**Response (400 Bad Request):**
```json
{
  "error": "BAD_REQUEST",
  "message": "email: El email no es válido",
  "code": 400,
  "date": "2025-11-12T14:35:00",
  "path": "/api/auth/register"
}
```

---

### 8.2 Username Duplicado

**Request:**
```http
POST /api/auth/register
Content-Type: application/json

{
  "person": {
    "documentType": "CI",
    "documentNumber": "22222222",
    "fullName": "Another User",
    "phone": "77799999999"
  },
  "username": "juanperez",
  "password": "Pass1234#",
  "email": "another@email.com",
  "officeId": "3fa85f64-5717-4562-b3fc-2c963f66afa6"
}
```

**Response (409 Conflict):**
```json
{
  "error": "CONFLICT",
  "message": "El nombre de usuario 'juanperez' ya está registrado",
  "code": 409,
  "date": "2025-11-12T14:40:00",
  "path": "/api/auth/register"
}
```

---

### 8.3 Token Expirado o Inválido

**Request:**
```http
GET /api/v1/users/3fa85f64-5717-4562-b3fc-2c963f66afa6
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.INVALID_TOKEN
```

**Response (401 Unauthorized):**
```json
{
  "error": "UNAUTHORIZED",
  "message": "Token JWT inválido o expirado",
  "code": 401,
  "date": "2025-11-12T14:45:00",
  "path": "/api/v1/users/3fa85f64-5717-4562-b3fc-2c963f66afa6"
}
```

---

### 8.4 Sin Rol Requerido (Requiere ADMIN)

**Request:**
```http
POST /api/v1/users
Authorization: Bearer <USER_TOKEN>
Content-Type: application/json

{
  "person": {
    "documentType": "CI",
    "documentNumber": "33333333",
    "fullName": "New User",
    "phone": "77788888888"
  },
  "username": "newuser",
  "password": "Pass1234#",
  "email": "new@email.com",
  "officeId": "3fa85f64-5717-4562-b3fc-2c963f66afa6"
}
```

**Response (403 Forbidden):**
```json
{
  "error": "FORBIDDEN",
  "message": "Acceso denegado: Se requiere rol ADMIN",
  "code": 403,
  "date": "2025-11-12T14:50:00",
  "path": "/api/v1/users"
}
```

---

### 8.5 Recurso No Encontrado

**Request:**
```http
GET /api/v1/users/00000000-0000-0000-0000-000000000000
Authorization: Bearer <ADMIN_TOKEN>
```

**Response (404 Not Found):**
```json
{
  "error": "NOT_FOUND",
  "message": "Usuario no encontrado con ID: 00000000-0000-0000-0000-000000000000",
  "code": 404,
  "date": "2025-11-12T14:55:00",
  "path": "/api/v1/users/00000000-0000-0000-0000-000000000000"
}
```

---

### 8.6 Password Inválido (No cumple validación)

**Request:**
```http
POST /api/auth/register
Content-Type: application/json

{
  "person": {
    "documentType": "CI",
    "documentNumber": "44444444",
    "fullName": "Another User",
    "phone": "77766666666"
  },
  "username": "anotheruser",
  "password": "short",
  "email": "another@email.com",
  "officeId": "3fa85f64-5717-4562-b3fc-2c963f66afa6"
}
```

**Response (400 Bad Request):**
```json
{
  "error": "BAD_REQUEST",
  "message": "password: El password debe contener entre 8 y 15 caracteres válidos",
  "code": 400,
  "date": "2025-11-12T15:00:00",
  "path": "/api/auth/register"
}
```

---

### 8.7 No Permitir Cancelar Envío ya Entregado

**Request:**
```http
DELETE /api/v1/shipments/8fa85f64-5717-4562-b3fc-2c963f66afa11
Authorization: Bearer <ADMIN_TOKEN>
(Nota: Este envío ya fue entregado en el ejemplo 6.4)
```

**Response (409 Conflict):**
```json
{
  "error": "CONFLICT",
  "message": "No se puede cancelar un envío que ya ha sido entregado",
  "code": 409,
  "date": "2025-11-12T15:05:00",
  "path": "/api/v1/shipments/8fa85f64-5717-4562-b3fc-2c963f66afa11"
}
```

---

## 📊 Tabla Resumen de Status Codes

| Código | Nombre | Uso Común |
|--------|--------|----------|
| 200 | OK | GET, PUT, PATCH, DELETE (exitoso) |
| 201 | CREATED | POST (recurso creado) |
| 204 | NO_CONTENT | POST /logout, DELETE (sin cuerpo) |
| 400 | BAD_REQUEST | Validación fallida |
| 401 | UNAUTHORIZED | Sin token o token inválido |
| 403 | FORBIDDEN | Sin rol requerido |
| 404 | NOT_FOUND | Recurso no existe |
| 409 | CONFLICT | Duplicado, estado inválido |
| 500 | INTERNAL_SERVER_ERROR | Error del servidor |

---

## 🔗 Flujos Comunes

### Flujo 1: Registro y Login de Usuario

1. **POST /api/auth/register** - Registro inicial
2. **POST /api/auth/login** - Obtener JWT token
3. **GET /api/v1/users/{id}** - Con Authorization header
4. **PUT /api/v1/users/{id}** - Actualizar perfil
5. **POST /api/auth/logout** - Cerrar sesión

### Flujo 2: Crear y Entregar Envío

1. **POST /api/v1/shipments** - Crear envío
2. **GET /api/v1/shipments/{id}** - Ver estado
3. **GET /api/v1/shipments/suggestions?term=...** - Buscar
4. **PATCH /api/v1/shipments/{id}/deliver** - Marcar entregado
5. **GET /api/v1/shipments/{id}** - Confirmar estado DELIVERED

### Flujo 3: Administración de Cooperativas

1. **POST /api/v1/transportCooperatives** - Crear cooperativa
2. **POST /api/v1/offices** - Crear oficinas
3. **POST /api/v1/transportCooperatives/assignOffices** - Asignar
4. **GET /api/v1/transportCooperatives/{id}/whith-offices** - Ver relación
5. **PUT /api/v1/transportCooperatives/{id}** - Actualizar

---

Documento generado: 2025-04-24
Versión: 1.0.0
