# Prueba Técnica Capitole

## Requisitos
- Java 21
- Maven 3.8+
- Docker

## Compilación del proyecto

1. Clona el repositorio y accede al directorio del proyecto:
   ```bash
   git clone <url-del-repositorio>
   cd pruebaTecnica/pruebaCapitole
   ```

2. Compila el proyecto y genera el JAR (sin tests):
   ```bash
   mvn clean package -DskipTests
   ```

## Construcción de la imagen Docker

1. Construye la imagen Docker:
   ```bash
   docker build -t prueba-capitole .
   ```

## Ejecución en un servidor

1. Ejecuta el contenedor Docker exponiendo el puerto 6969 y configurando la URL pública del servidor:
   ```bash
   docker run -p 6969:6969 -e SERVER_URL=http://miservidor.com:6969 prueba-capitole
   ```
   - Cambia `http://miservidor.com:6969` por la URL pública de tu servidor.

2. Accede a la API y a la documentación Swagger UI desde tu navegador:
   - Swagger UI: `http://miservidor.com:6969/swagger-ui.html`
   - Documentación OpenAPI: `http://miservidor.com:6969/api-docs`

## Arquitectura Hexagonal (resumen)
Este proyecto ya sigue una estructura por capas (domain, application, infrastructure). Para reforzar el enfoque hexagonal y facilitar el mantenimiento, consulta el plan detallado en `HEXAGONAL_REFACTOR_PLAN.md`, que propone:
- Puertos primarios/secundarios claros y consistentes (nomenclatura `*Port`).
- Controladores que dependan de puertos de aplicación (use cases), no de implementaciones concretas.
- Aislar framework y DTOs en infraestructura, dejando dominio y aplicación lo más puros posible.
- Estrategia de tests alineada (dobles de puertos en unit tests, tests de adaptadores por separado).

## Notas adicionales
- El archivo de configuración `application.yaml` se encuentra fuera del JAR y es copiado al contenedor Docker en `/app/config/application.yaml`.
- Los logs de la aplicación se almacenan en `${HOME}/app/log/pruebatecnica.log` dentro del contenedor.
- Puedes montar un volumen externo para los logs o la configuración si lo deseas.
