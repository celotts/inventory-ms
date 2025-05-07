#------------------------------------------------------------------------
# make up-local          # Levanta infra para trabajar desde IntelliJ
# make down-local        # Apaga infra local
# make logs-local        # Ver logs en tiempo real (local)
# make restart-db        # Reinicia solo la base de datos
# make reset-local       # Reinicia completamente el entorno local (infra)
# make up-dev            # Levanta todo el stack en contenedores (modo dev)
# make down-dev          # Apaga todo el stack en dev
# make logs-dev          # Ver logs de todos los contenedores en dev
# make up-prod           # Levanta todo en entorno de producción
# make build-images      # Construye manualmente imágenes
# make clean-docker      # Limpia imágenes, volúmenes, contenedores detenidos
# make stop-all          # Detiene todos los contenedores activos en Docker
# make reset-portainer   # Apaga, limpia y relanza stack de Portainer
#------------------------------------------------------------------------


# Variables
ENV_FILE_LOCAL ?= .env.local
ENV_FILE_DEV ?= .env.dev
ENV_FILE_PROD ?= .env.prod
# Levantar con archivo .env actual (puedes exportar ENV_FILE para cambiarlo)

# 🧪 LOCAL: ejecutas product-service desde IntelliJ
up-local:
	cp $(ENV_FILE_LOCAL) .env
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) down -v
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) up --build

down-local:
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) down -v

# 🧪 DEV: todo en contenedores (incluye product-service)
up-dev:
	cp $(ENV_FILE_DEV) .env
	docker compose down -v
	docker compose --env-file $(ENV_FILE_DEV) up --build

# 🧪 PROD: igual que dev pero con variables de producción
up-prod:
	cp $(ENV_FILE_PROD) .env
	docker compose down -v
	docker compose --env-file $(ENV_FILE_PROD) up --build

# 🏗️ Build manual de imágenes (opcional si ya usas build automático)
build-images:
	docker build -t inventory/discovery-service ../discovery-service
	docker build -t inventory/config-service ../config-service
	docker build -t inventory/product-service ../product-service

# 🔄 Reinicia solo la base de datos
restart-db:
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) restart product-db

# 📜 Muestra logs de todos los servicios locales
logs-local:
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) logs -f

# 📜 Muestra logs del entorno dev
logs-dev:
	docker compose -f docker-compose.yml --env-file $(ENV_FILE_DEV) logs -f

# 🧹 Limpia imágenes sin contenedor y volúmenes no usados
clean-docker:
	docker system prune -f --volumes

# 🔁 Reinicia completamente el entorno local (infra)
reset-local:
	@echo "🔻 Apagando contenedores..."
	$(MAKE) down-local
	@echo "🧹 Limpiando recursos no usados de Docker..."
	$(MAKE) clean-docker
	@echo "🚀 Levantando entorno local limpio..."
	$(MAKE) up-local

# 🔻 Detiene todos los contenedores en ejecución (cualquier origen)
stop-all:
	@echo "🛑 Deteniendo todos los contenedores..."
	docker ps -q | xargs -r docker stop
	docker system prune -f --volumes

