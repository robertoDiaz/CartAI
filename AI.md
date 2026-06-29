# AI Context Document

## Contexto de la última sesión

Se han resuelto múltiples problemas relacionados con la gestión y actualización del **avatar de usuario** y el formulario de perfil. 

Los cambios realizados se dividen entre el Backend (`CartAI`) y el Frontend (`CartAI-WEB`).

### Problemas Solucionados

1. **Inconsistencia de campos en respuesta de subida:**
   - **Problema:** El backend devolvía `avatarFileId` usando un `id` vacío del adaptador S3/MinIO, mientras que el frontend esperaba `avatarFileURL`.
   - **Solución:** Se corrigió `UserAvatarRestController` para devolver el nombre de archivo de MinIO (`fileName`) en lugar de `id` vacío, y se actualizó el frontend (`identityService.ts` y `ProfilePage.tsx`) para usar el campo correcto (`avatarFileId`).

2. **Ruta de visualización de ficheros (Frontend):**
   - **Problema:** El frontend intentaba renderizar el avatar desde `/api/files/{id}`, un endpoint que no existe.
   - **Solución:** Se actualizó a `/api/storage/files/{id}` en `ProfilePage.tsx` y `Navbar.tsx` para que coincida con `StorageRestController`.

3. **Pérdida de identidad tras Login (Backend/Frontend):**
   - **Problema:** Tras el login, el backend no devolvía el `userId` ni el `avatarFileId` en `AuthRestResponse`, por lo que el store del frontend tenía esos campos como `undefined`. Esto provocaba que `PUT /api/users/{id}` fallase al enviarse como `undefined`.
   - **Solución:** Se añadieron `userId` y `avatarFileId` al record `AuthRestResponse` y se mapearon correctamente en `AuthRestMapper`.

4. **Bloqueo de CORS por Spring Security en preflight (Backend):**
   - **Problema:** Spring Security interceptaba y bloqueaba con HTTP 401 las peticiones preflight (`OPTIONS`) del formulario de actualización de perfil (`PUT`) antes de llegar a la configuración de CORS, causando un falso error CORS en el navegador.
   - **Solución:** Se añadió `OPTIONS` a los métodos permitidos en `application.properties` y se refactorizó `CorsConfig` para exponer un bean `CorsConfigurationSource`. Además, se añadió `.cors(Customizer.withDefaults())` en `SecurityConfig` para que Spring Security lo valide de forma nativa antes de la autenticación.

5. **Error de Deserialización de Roles en Jackson (Backend):**
   - **Problema:** El frontend enviaba un array de strings (ej. `["ADMIN"]`) pero `UpdateUserRestRequest` requería un `Set<Role>` completo, lo cual provocaba un `HttpMessageNotReadableException`.
   - **Solución:** Se modificó el DTO `UpdateUserRestRequest` para recibir `Set<String>` y se actualizó `UserRestController` para buscar los objetos `Role` en BD antes de pasarlos al caso de uso (igual que en la creación).

6. **Promoción de ficheros de bucket temporal a permanente (Backend):**
   - **Problema:** Al actualizar el perfil con un nuevo avatar, el caso de uso `UpdateUserUseCase` no movía el fichero de MinIO del bucket temporal (que auto-expira) al permanente.
   - **Solución:** Se implementó la lógica de `storagePort.promoteFile()` y limpieza de avatar antiguo dentro de `UpdateUserUseCase`.

### Estrategia de Testing de Integración (Backend)

Durante las últimas sesiones se ha definido un estándar estricto para los Tests de Integración (`*IT.java`) con el fin de evitar dependencias entre tests, problemas de estado compartido y fallos en cascada.

1. **Patrón Atómico (Atomic Integration Tests)**:
   - Los tests de integración en la capa REST **no comparten estado**.
   - Se ha eliminado la clase `BaseFlowIT` (que compartía tokens JWT estáticos y obligaba a usar `@Order`).
   - Se usa una clase base muy ligera llamada `BaseIT`, la cual inicializa el contexto de Spring (`@SpringBootTest`, `@AutoConfigureMockMvc`, MongoDB embebido con flapdoodle, mocks de MinIO y Kafka).
   - `BaseIT` no guarda ningún estado de sesión. Contiene utilidades sin estado (`login()`, `removeTestUsers()`, `cleanCollections()`).

2. **Aislamiento en cada Test**:
   - Cada `@Test` debe ser responsable de **crear sus propios datos**, hacer **su propio login** para obtener un JWT fresco (usando el helper `login()`), y actuar.
   - El orden de ejecución de los tests ya no importa.
   - Limpieza: Los tests que creen datos fuera del flujo de "semilla" (bootstrap) deben limpiarlos en el `@AfterEach`. Por ejemplo, eliminando las colecciones específicas con `cleanCollections(...)` o eliminando usuarios específicos de prueba con `removeTestUsers(...)`.
   
Este diseño atómico prioriza la mantenibilidad, robustez y el aislamiento por encima de la reutilización de peticiones de login.

### 🔮 Roadmap Futuro (Deuda Técnica)

- **Testcontainers para MongoDB:** Reemplazar `Flapdoodle` embebido por contenedores Docker con MongoDB (configurado en
  modo Replica Set). Esto permitirá eliminar la propiedad `cartai.mongo.transaction.enabled=false` y ejecutar las
  transacciones multi-documento de Spring Data exactamente igual que en producción.

### Soluciones Arquitectónicas Recientes (Backend)

7. **Generación limpia de UUIDs y registro en BBDD para Avatares:**
   - **Problema:** `UpdateUserAvatarUseCase` subía el archivo de imagen directamente a S3 utilizando el nombre del archivo original (por ejemplo, con espacios). Al no registrarlo en `StoredFileRepositoryPort`, las peticiones de descarga posteriores (`StorageRestController.getFile`) fallaban devolviendo un HTTP 404, y los problemas con los espacios en URLs rompían la visualización.
   - **Solución:** Se ha refactorizado `UpdateUserAvatarUseCase` para que inyecte `IncrementIdGeneratorPort` y `StoredFileRepositoryPort`. Ahora, al subir un avatar, se genera automáticamente un identificador (UUID) limpio, se concatena su extensión original y se persisten los metadatos en MongoDB. Ahora, la carga pública funciona de forma impecable usando la URL interna oficial de ficheros.
   - **Complementos de la solución:** Se ha relajado la obligatoriedad de la cabecera `Authorization` en `StorageRestController` (vía `required = false`) ya que las etiquetas `<img>` del frontend no la envían de manera nativa. También se ha ajustado la anotación `@GetMapping("/files/{id:.+}")` para evitar que Spring MVC trunque la extensión del archivo.
   - **Limpieza de Código:** Se ha eliminado `UploadAvatarUseCase` (y `UploadAvatarCommand`) junto con el endpoint `POST /api/users/avatar` en `UserAvatarRestController`, ya que no se usaban ni formaban parte del flujo arquitectónico funcional (todo se hace mediante `PUT /api/users/avatar/{id}`).
