# Reglas del Proyecto

- **NO realizar commits ni subidas (push) automáticamente.**
  Bajo ninguna circunstancia los agentes autónomos de Antigravity deben ejecutar comandos de control de versiones (`git commit`, `git push`, etc.) de manera proactiva o automática tras realizar cambios.
  Cualquier operación de control de versiones debe ser autorizada o solicitada explícitamente por el usuario.

- **Arquitectura Hexagonal Estricta.**
  Las clases de dominio (bajo `domain/model/`) deben ser puras y no importar ni depender de clases de infraestructura (`infrastructure/`) ni de aplicación (`application/`).

- **Uso de Records para DTOs y Comandos.**
  Todos los comandos, DTOs REST de entrada y salida, y objetos de valor intermedios deben definirse como Java `record` para garantizar la inmutabilidad de los datos de transporte.

- **Tests de Integración Atómicos e Independientes.**
  Los tests de integración (`*IT.java`) no deben compartir estado estático ni de sesión. Cada caso de prueba es responsable de su propio setup, login (para obtener un JWT fresco) y limpieza posterior de colecciones en `@AfterEach`.

- **Creación y Modificación de Tests.**
  Si un cambio introduce nueva funcionalidad o modifica la existente, es obligatorio crear o adaptar los tests correspondientes (unitarios, de integración o funcionales/E2E) para dar cobertura a los cambios realizados.

