# ANÁLISIS TÉCNICO EXHAUSTIVO - Shipment Service API

## 📊 RESUMEN EJECUTIVO

### Estadísticas Generales
- **Total de Controladores:** 8
- **Total de Endpoints:** 47 
- **Controladores Implementados:** 7
- **Controladores Vacíos:** 1 (PermissionController)

### Distribución de Endpoints
| Método HTTP | Cantidad | Porcentaje |
|-------------|----------|-----------|
| GET         | 21       | 44.7%     |
| POST        | 14       | 29.8%     |
| PUT         | 8        | 17.0%     |
| PATCH       | 1        | 2.1%      |
| DELETE      | 7        | 14.9%     |

### Seguridad
- **Endpoints Públicos:** 3 (/api/auth/*)
- **Endpoints Protegidos:** 44
- **Autenticación Requerida:** JWT Bearer Token
- **Roles Implementados:** ADMIN, USER
- **Política CORS:** Habilitada para dominios conocidos

---

## 🔐 AUTENTICACIÓN Y AUTORIZACIÓN

### JWT (JSON Web Token)
```
Algoritmo: HMAC-SHA256
Ubicación: Authorization Header
Formato: Bearer {token}
Duración: 1 hora (3600000ms en desarrollo)
```

### Claims del Token JWT
```json
{
  "sub": "username",           // Identificador del usuario
  "uuid": "usuario-uuid",      // UUID único del usuario
  "officeId": "office-uuid",   // Oficina asignada
  "roles": ["ROLE_ADMIN"],     // Lista de roles
  "iat": 1731423400,           // Issued At (fecha emisión)
  "exp": 1731427000            // Expiration (fecha vencimiento)
}
```

### Roles y Permisos

#### ROLE_ADMIN
- Acceso completo a todos los endpoints
- Puede crear, modificar y eliminar recursos
- Puede acceder a datos de configuración (usuarios, roles, permisos)
- Puede hacer login con límite de 5 intentos antes de lockout (10 minutos)

#### ROLE_USER
- Solo lectura de envíos (GET /api/v1/shipments/*)
- No puede crear/modificar/eliminar recursos
- Limitado a operaciones de consulta

#### PUBLIC (Sin autenticación)
- POST /api/auth/register - Registro
- POST /api/auth/login - Login
- POST /api/auth/logout - Logout

### Validación de Seguridad
```
⚠️ NIVELES DE PROTECCIÓN IMPLEMENTADOS:
✅ CSRF deshabilitado (JWT no necesita CSRF)
✅ Sesión STATELESS (No se almacenan sesiones)
✅ CORS configurado para dominios específicos
✅ Token JWT se valida en cada request
✅ Roles validados en nivel de endpoint
✅ Contraseña hasheada en BD (implícito)
✅ Login con lockout anti-fuerza bruta (5 intentos, 10 min)
```

---

## 🗄️ BASE DE DATOS

### Configuración
- **Sistema:** PostgreSQL
- **Host:** localhost
- **Puerto:** 5432
- **Base de Datos:** db_shipment
- **Usuario:** postgres
- **Password:** root123

### Estrategia JPA
- **DDL:** update (Genera/actualiza esquema automáticamente)
- **Show SQL:** Habilitado (logs de queries)
- **Format SQL:** Habilitado (queries legibles en logs)
- **Hibernate Dialect:** PostgreSQL

### Modelado de Datos
- **Eliminación Lógica:** Implementada (campos `active`, `enabled`)
- **Auditoría:** Campos `createAt`, `updateAt` en entidades
- **UUIDs:** Identificadores únicos para todas las entidades

---

## 📡 API REST - ESTRUCTURA

### Base URL
```
http://localhost:8080
Puerto Dinámico: ${PORT:8080} (Para Render/Cloud)
```

### Versionado
```
/api/auth          - Sin versionado (Autenticación)
/api/v1/...        - Versión 1 (Todos los recursos)
```

### Convenciones HTTP
```
GET     - Obtener recursos
POST    - Crear recursos
PUT     - Actualizar recursos completos
PATCH   - Actualizar parcial (solo delivery)
DELETE  - Eliminar (lógico) recursos
OPTIONS - CORS pre-flight
```

---

## 🛣️ ENDPOINTS POR RECURSO

### 1️⃣ Autenticación (/api/auth)
```
POST   /register      ✅ Registro de nuevo usuario
POST   /login         ✅ Login y generación de JWT
POST   /logout        ✅ Invalidación de token
```

### 2️⃣ Usuarios (/api/v1/users)
```
GET    /              ✅ Listar todos (sin paginar)
GET    /paged         ✅ Listar paginado
GET    /{id}          ✅ Obtener por ID
POST   /              ✅ Crear usuario
PUT    /{id}          ✅ Actualizar usuario
PUT    /password/{id} ✅ Cambiar contraseña
DELETE /{id}          ✅ Desactivar usuario
```

### 3️⃣ Personas (/api/v1/persons)
```
GET    /              ✅ Listar paginado
GET    /{id}          ✅ Obtener por ID
GET    /ci/{ci}       ✅ Buscar por número documento
GET    /phone/{phone} ✅ Verificar existencia por teléfono
POST   /              ✅ Crear persona
PUT    /{id}          ✅ Actualizar persona
DELETE /{id}          ✅ Desactivar persona
```

### 4️⃣ Oficinas (/api/v1/offices)
```
GET    /paged         ✅ Listar paginado
GET    /{id}          ✅ Obtener por ID
POST   /              ✅ Crear oficina
PUT    /{id}          ✅ Actualizar oficina
DELETE /{id}          ✅ Desactivar oficina
```

### 5️⃣ Cooperativas de Transporte (/api/v1/transportCooperatives)
```
GET    /              ✅ Listar todas
GET    /{id}          ✅ Obtener por ID
GET    /{id}/whith-offices  ✅ Obtener con oficinas asignadas
POST   /              ✅ Crear cooperativa
POST   /assignOffices ✅ Asignar oficinas
PUT    /{id}          ✅ Actualizar cooperativa
DELETE /{id}          ✅ Desactivar cooperativa
```

### 6️⃣ Envíos (/api/v1/shipments)
```
GET    /              ✅ Listar paginado
GET    /{id}          ✅ Obtener por ID
GET    /suggestions   ✅ Buscar con autocomplete
POST   /              ✅ Crear envío
PATCH  /{id}/deliver  ✅ Marcar como entregado
DELETE /{id}          ✅ Cancelar envío
```

### 7️⃣ Roles (/api/v1/roles)
```
GET    /              ✅ Listar paginado
GET    /{id}          ✅ Obtener por ID
POST   /              ✅ Crear rol
PUT    /{id}          ✅ Actualizar rol
DELETE /{id}          ✅ Desactivar rol
```

### 8️⃣ Permisos (/api/v1/permissions)
```
❌ VACÍO - No implementado
Estructura DTO existe pero sin controlador
```

---

## 📋 VALIDACIONES IMPLEMENTADAS

### Validaciones de Entrada (Jakarta Validation)
```
✅ @NotBlank - Campo no puede estar vacío
✅ @NotNull - Campo obligatorio
✅ @Email - Validación de formato email
✅ @Pattern - Validación con regex
✅ @Size - Validación de longitud
✅ @Positive - Validación de números positivos
✅ @Valid - Validación recursiva de objetos anidados
```

### Patrones de Validación (Regex)

#### Username
```
Patrón: [a-zA-Z0-9\s,.áéíóúÁÉÍÓÚñÑ\-]{3,15}
Rango: 3-15 caracteres
Permite: Letras, números, espacios, comas, puntos, guiones, acentos
```

#### Password
```
Patrón: [a-zA-Z0-9._\-#$]{8,15}
Rango: 8-15 caracteres
Permitidos: Letras, números, puntos, guiones, hash, dólar
Ejemplos válidos: Pass1234#, MyPass$123, secure.pass-1
```

#### Teléfono
```
Patrón: \d{8,15}
Rango: 8-15 dígitos
Ejemplos: 77712345678, 87654321, 70123456789
```

#### Número de Documento
```
Patrón: \d{7,9}
Rango: 7-9 dígitos
Ejemplos: 1234567, 12345678, 123456789
```

#### Dirección
```
Patrón: [a-zA-Z0-9\s,.áéíóúÁÉÍÓÚñÑ\-#/]{4,60}
Rango: 4-60 caracteres
Permitidos: Letras, números, espacios, comas, puntos, guiones, hash, slash
```

#### Descripción de Ítem
```
Patrón: [a-zA-Z0-9\s,.áéíóúÁÉÍÓÚñÑ\-]{4,260}
Rango: 4-260 caracteres
Permitidos: Letras, números, espacios, comas, puntos, guiones, acentos
```

### Validaciones de Negocio
```
✅ Username único
✅ Email único
✅ Número de documento único
✅ Teléfono único (parcial)
✅ Office debe existir (FK)
✅ Cooperativa debe existir (FK)
✅ No permitir duplicados en relaciones
✅ No permitir cancelar envío ya entregado
```

---

## 🚫 MANEJO DE ERRORES

### Estructura de Respuesta de Error
```json
{
  "error": "NOT_FOUND",
  "message": "Usuario no encontrado con ID: xxx",
  "code": 404,
  "date": "2025-11-12T14:30:00",
  "path": "/api/v1/users/xxx"
}
```

### Códigos de Error Implementados

| Código | Nombre | Causa | Ejemplo |
|--------|--------|-------|---------|
| 400 | BAD_REQUEST | Validación fallida | Campo vacío, formato inválido |
| 401 | UNAUTHORIZED | Sin autenticación | Token ausente, expirado, inválido |
| 403 | FORBIDDEN | Sin autorización | Requiere rol ADMIN |
| 404 | NOT_FOUND | Recurso no existe | Usuario no encontrado |
| 409 | CONFLICT | Conflicto de datos | Email/username duplicado |
| 500 | INTERNAL_SERVER_ERROR | Error del servidor | Bug, error BD, excepción no controlada |

### Excepciones Personalizadas

#### ResourceNotFoundException (404)
```
Cuándo: Un recurso buscado no existe
Ejemplos:
  - Usuario con ID 123 no encontrado
  - Oficina con ID 456 no encontrada
  - Envío con ID 789 no encontrado
```

#### PersonValidationException (409)
```
Cuándo: Error de validación en operación de Persona
Ejemplos:
  - Número de documento ya existe
  - Teléfono duplicado
  - Datos incompletos
```

#### OfficeValidationException (409)
```
Cuándo: Error de validación en operación de Oficina
Ejemplos:
  - Nombre de oficina ya existe
  - Cooperativa asignada no existe
```

#### UserValidationException (409)
```
Cuándo: Error de validación en operación de Usuario
Ejemplos:
  - Username ya existe
  - Email ya registrado
  - Persona asociada no válida
```

#### ShipmentValidationException (409)
```
Cuándo: Error de validación en operación de Envío
Ejemplos:
  - Envío ya entregado, no puede cancelarse
  - Oficina de destino no existe
  - Datos de remitente/destinatario inválidos
```

#### TransportCooperativeException (500)
```
Cuándo: Error crítico en operación de Cooperativa
Ejemplos:
  - Error al asignar oficinas
  - Error de operación en BD
```

#### ResourceAlreadyExistsException (409)
```
Cuándo: El recurso ya existe
Ejemplos:
  - Rol ya existe
  - Cooperativa ya existe
```

---

## 📄 PAGINACIÓN

### Sistema de Paginación Implementado

#### PageResponse<T>
```java
{
  "content": [],           // Lista de elementos
  "pageNo": 0,             // Número de página (base 0)
  "size": 10,              // Elementos por página
  "totalElements": 150,    // Total de elementos
  "totalPages": 15,        // Total de páginas
  "first": true,           // ¿Es primera página?
  "last": false,           // ¿Es última página?
  "hasNext": true,         // ¿Hay siguiente?
  "hasPrevious": false     // ¿Hay anterior?
}
```

#### Parámetros Query

| Parámetro | Tipo | Default | Máximo | Descripción |
|-----------|------|---------|--------|-------------|
| page | int | 0 | - | Número de página (0-based) |
| pageNo | int | 0 | - | Alternativa de nombre (en algunos endpoints) |
| size | int | 10 | 50 | Registros por página |
| sort | string | id | - | Campo,dirección (ej: name,desc) |
| sortBy | string | id | - | Alternativa de nombre (en User) |

#### Ejemplo de Uso
```
GET /api/v1/users/paged?pageNo=0&size=20&sortBy=username
GET /api/v1/shipments?page=1&size=25&sort=createdAt,desc
GET /api/v1/persons?page=0&size=10
```

#### Validaciones Defensivas (UserController)
```
✅ Si pageNo < 0 → Se ajusta a 0
✅ Si size <= 0 → Se ajusta a 10
✅ Si size > 50 → Se ajusta a 50
```

---

## 🔄 OPERACIONES ESPECIALES

### Eliminación Lógica (Soft Delete)
```
Implementación: Campos booleanos (active, enabled)
Comportamiento:
  - DELETE no elimina registros
  - Solo marca como inactivo
  - Datos se mantienen en BD para auditoría
  - GET no retorna registros inactivos (generalmente)

Endpoints afectados:
  - DELETE /offices/{id}
  - DELETE /persons/{id}
  - DELETE /users/{id}
  - DELETE /roles/{id}
  - DELETE /transportCooperatives/{id}
  - DELETE /shipments/{id}
```

### Búsqueda con Autocomplete
```
Endpoint: GET /api/v1/shipments/suggestions
Parámetros:
  - term: Término de búsqueda
  - page, size, sort: Paginación normal
Campos indexados:
  - trackingCode
  - itemDescription
  - fullName (destinatario)
  - documentNumber (destinatario)
```

### Asignación de Oficinas a Cooperativas
```
Endpoint: POST /api/v1/transportCooperatives/assignOffices
Body:
{
  "cooperativeId": "uuid",
  "officesId": ["uuid1", "uuid2", ...]
}
Validaciones:
  - Mínimo 1 oficina
  - Cooperativa debe existir
  - Todas las oficinas deben existir
Respuesta: CooperativeOfficeResponse con lista de oficinas
```

### Entrega de Envíos
```
Endpoint: PATCH /api/v1/shipments/{id}/deliver
Body:
{
  "documentNumber": "12345678"
}
Operaciones:
  - Valida número de documento
  - Cambia estado a DELIVERED
  - Registra fecha de entrega
  - Registra quién recibió (opcional)
Validaciones:
  - Envío debe existir
  - No puede estar ya entregado
```

---

## 📦 ESTRUCTURAS DE DATOS

### Tipos de Documento Soportados
```
Enum DocumentType:
  - CI (Cédula de Identidad)
  - NIT (Número de Identificación Tributaria)
  - PASSPORT (Pasaporte)
  - LICENSE (Licencia)
```

### Estados de Envío
```
Enum ShipmentStatus:
  - PENDING (Pendiente)
  - IN_TRANSIT (En tránsito)
  - DELIVERED (Entregado)
  - CANCELLED (Cancelado)
```

### DTOs Principales

#### UserRequestDto (Crear Usuario)
```
- person: PersonRequestDto (anidado)
- username: string[3-15]
- password: string[8-15]
- email: string (formato email)
- officeId: UUID
```

#### PersonRequestDto (Crear Persona)
```
- documentType: DocumentType (enum)
- documentNumber: string[7-9]
- fullName: string
- phone: string[8-15]
```

#### ShipmentRequestDto (Crear Envío)
```
- destinationOfficeId: UUID
- sender: ShipmentPersonRequestDto
- recipient: ShipmentPersonRequestDto
- itemDescription: string[4-260]
- shippingCost: BigDecimal (> 0)
```

#### TransportCooperativeRequest (Crear Cooperativa)
```
- name: string[3-100]
- enabled: boolean
```

#### OfficeRequestDto (Crear Oficina)
```
- name: string[4-60]
- address: string[4-60]
- phone: string[8-15]
```

#### RoleRequestDto (Crear Rol)
```
- name: string[3-60]
```

---

## 🔍 HALLAZGOS TÉCNICOS Y PROBLEMAS DETECTADOS

### ✅ Fortalezas
1. **Seguridad JWT robusta** - Tokens con claims completos
2. **Validación exhaustiva** - Patrones regex personalizados
3. **Eliminación lógica** - Auditoría preservada
4. **CORS configurado** - Restricción a dominios conocidos
5. **OpenAPI/Swagger** - Documentación automática
6. **Paginación flexible** - Soporta múltiples formatos

### ⚠️ Problemas Detectados

#### 1. PermissionController Vacío
```
Problema: Controller existe pero está completamente vacío
Impacto: Endpoints de /api/v1/permissions no funcionan
Solución: Implementar los 5-7 endpoints estándar CRUD
```

#### 2. RoleController - Parámetro @PathVariable sin nombre
```
Código problemático:
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deactivateRoleById(@NonNull UUID id)
Problema: @PathVariable debería tener nombre explícito
Solución: @PathVariable(name="id") UUID id
```

#### 3. RoleController - DELETE retorna 201 en UPDATE
```
Problema:
  @PutMapping("/{id}")
  return ResponseEntity.status(HttpStatus.CREATED)
Debería ser: HttpStatus.OK (200)
```

#### 4. ShipmentController - PUT comentado
```
Problema: Endpoint de actualización está descomentado (commented out)
Impacto: No se pueden actualizar envíos después de crear
Ubicación: ShipmentController línea ~63
```

#### 5. UserController/RoleController - Inconsistencia de paginación
```
Problema: UserController usa parámetros personalizados (pageNo, size, sortBy)
Otros: Usan Pageable estándar de Spring
Solución: Estandarizar a Pageable en todos
```

#### 6. Error en ShipmentUpdateRequestDto
```
Problema: Importado pero nunca usado (PUT está comentado)
Impacto: Código muerto
```

#### 7. Login - Lock mechanism no verificado
```
Problema: Se menciona lockout de 10 minutos
Nota: Requiere verificación de implementación real en servicio
```

#### 8. ResponseEntity inconsistente en algunos DELETE
```
CooperativeController.deleteCooperative() retorna TransportCooperativeResponse
RoleController.deleteRoleById() retorna String
UserController.deactivate() retorna UserResponseDto
Debería: Ser consistente en todos
```

---

## 🎯 RECOMENDACIONES

### Implementación Pendiente
1. ✅ **PermissionController** - Completar implementación CRUD
2. ✅ **ShipmentController.updateShipment()** - Descommentar y completar
3. ✅ **Estandarizar Pageable** - Usar Spring Pageable en todos los controllers
4. ✅ **Validación de Permisos** - Implementar relación Roles-Permissions-Users

### Mejoras de Código
1. **Usar ResponseEntity consistente** - Mismo DTO/tipo en todos los delete
2. **Explícitar @PathVariable** - Nombrar explícitamente parámetros
3. **Remover código muerto** - ShipmentUpdateRequestDto
4. **Usar HttpStatus correcto** - 200 OK en updates, no 201 CREATED
5. **Centralizar validaciones** - Mover lógica de pageable a helper

### Seguridad
1. ✅ **Implementar Rate Limiting** - Limitar requests por IP/usuario
2. ✅ **Token Refresh** - Agregar mecanismo de refresh sin re-login
3. ✅ **Auditoría de cambios** - Registrar quién modificó qué y cuándo
4. ✅ **Encriptación en tránsito** - HTTPS en producción (obligatorio)
5. ✅ **Secrets Management** - Usar variables de entorno para JWT_SECRET

### Performance
1. ✅ **Indexación en BD** - Campos de búsqueda frequent
2. ✅ **Caché** - Redis para usuarios/roles/permisos
3. ✅ **Lazy Loading** - Relaciones complejas
4. ✅ **Query Optimization** - Revisar queries N+1

### Documentación
1. ✅ **Swagger/OpenAPI** - Ya implementado pero revisar completitud
2. ✅ **Postman Collection** - Crear colección con ejemplos
3. ✅ **Guía de autenticación** - Flujo OAuth2 detallado
4. ✅ **Guía de integración** - Cómo consumir API desde frontend

---

## 📚 ENUMS Y CONSTANTES

### DocumentType
```
CI
NIT
PASSPORT
LICENSE
```

### ShipmentStatus
```
PENDING
IN_TRANSIT
DELIVERED
CANCELLED
```

---

## 🔗 DEPENDENCIAS EXTERNAS DETECTADAS

### Spring Framework
- spring-boot-starter-web (REST API)
- spring-boot-starter-security (Autenticación/Autorización)
- spring-boot-starter-data-jpa (ORM)
- spring-boot-starter-validation (Validación)

### JWT
- jjwt (JSON Web Token)
  
### Base de Datos
- postgresql driver
- spring-boot-starter-data-jpa

### Documentación
- springdoc-openapi (Swagger/OpenAPI 3.0)

### Utilities
- lombok (Annotations helpers)

---

## 🧪 PRUEBAS SUGERIDAS

### Unit Tests
```
✅ Validadores (patrones regex)
✅ Mappers (DTO ↔ Entity)
✅ Services (lógica de negocio)
✅ JWT generation/validation
```

### Integration Tests
```
✅ CRUD endpoints
✅ Autenticación/Autorización
✅ Validación de constraints
✅ Manejo de excepciones
```

### Load Tests
```
✅ Paginación con millones de registros
✅ Autenticación simultánea
✅ Búsqueda con suggestions
```

---

## 📊 MÉTRICAS CLAVE

| Métrica | Valor |
|---------|-------|
| Total de Controladores | 8 |
| Total de Endpoints | 47 |
| Endpoints GET | 21 |
| Endpoints POST | 14 |
| Endpoints PUT | 8 |
| Endpoints PATCH | 1 |
| Endpoints DELETE | 7 |
| Endpoints Públicos | 3 |
| Endpoints Protegidos | 44 |
| DTOs (Request) | 12+ |
| DTOs (Response) | 12+ |
| Excepciones Personalizadas | 8 |
| Campos de Validación | 50+ |

---

Documento generado: 2025-04-24
Versión: 1.0.0
