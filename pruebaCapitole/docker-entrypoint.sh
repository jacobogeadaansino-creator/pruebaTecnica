#!/bin/sh
set -e

# Crear directorio de logs si no existe
mkdir -p ${HOME}/app/log

# Configurar opciones de JVM por defecto si no están definidas
JAVA_OPTS="${JAVA_OPTS:--Xms256m -Xmx512m}"

# Imprimir configuración
echo "========================================="
echo "Iniciando Prueba Técnica Capitole"
echo "========================================="
echo "SERVER_URL: ${SERVER_URL:-http://localhost:6969}"
echo "JAVA_OPTS: ${JAVA_OPTS}"
echo "CONFIG: /app/config/application.yaml"
echo "LOG: ${HOME}/app/log/pruebatecnica.log"
echo "========================================="

# Ejecutar la aplicación
exec java ${JAVA_OPTS} -jar app.jar --spring.config.location=file:/app/config/application.yaml "$@"

