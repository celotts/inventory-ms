#!/bin/bash

BUILD_DIR="$1"
AUTH_FILE="$2"
ENV_FILE_LOCAL="$3"

# 1. Asegurar que Podman Machine esté corriendo
podman machine start --existing >/dev/null 2>&1 || true

# 2. Leer credenciales
DOCKER_USERNAME=$(grep DOCKER_USERNAME "$ENV_FILE_LOCAL" | cut -d '=' -f 2)
DOCKER_PASSWORD=$(grep DOCKER_PASSWORD "$ENV_FILE_LOCAL" | cut -d '=' -f 2)

# 3. Crear directorio de build y archivo de autenticación para Podman
mkdir -p "$BUILD_DIR"
cat <<EOF > "$AUTH_FILE"
{
	"auths": {
		"docker.io": {
			"auth": "$(echo -n "$DOCKER_USERNAME:$DOCKER_PASSWORD" | base64)"
		}
	}
}
EOF

echo "=========================================================="
echo "✅ Archivo de autenticación generado en $AUTH_FILE."
echo "=========================================================="