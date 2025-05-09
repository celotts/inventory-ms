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

help:
	@grep -E '^#|^[a-zA-Z_-]+:.*?## ' $(MAKEFILE_LIST) | \
	awk 'BEGIN {FS = ":.*?## "}; /^[^#].*/ {printf "  \033[36m%-20s\033[0m %s\n", $$1, $$2}'

clean-volumes: ## 🧹 Limpia volúmenes no usados
	docker volume prune -f

clean-images: ## 🧹 Limpia imágenes sin contenedor
	docker image prune -a -f

clean-docker: ## 🧹 Limpia imágenes sin contenedor y volúmenes no usados
	clean-images clean-volumes

up-local: ## 🧪 Levanta infra para trabajar desde IntelliJ
	cp $(ENV_FILE_LOCAL) .env.override
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) down -v
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) up --build

down-local: ## 🔻 Apaga infra local
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) down -v


up-dev: ## 🧪 Levanta todo el stack en contenedores (modo dev)
	cp $(ENV_FILE_DEV) .env.override
	docker compose down -v
	docker compose --env-file $(ENV_FILE_DEV) up --build


up-prod: ## 🚀 Levanta todo en entorno de producción
	cp $(ENV_FILE_PROD) .env.override
	docker compose -f docker-compose.yml --env-file $(ENV_FILE_PROD) down -v
	docker compose -f docker-compose.yml --env-file $(ENV_FILE_PROD) up --build

build-images: ## 🏗️ Build manual de imágenes (opcional si ya usas build automático)
	docker build -t inventory/discovery-service ../discovery-service
	docker build -t inventory/config-service ../config-service
	docker build -t inventory/product-service ../product-service


restart-db:  ## 🔄 Reinicia solo la base de datos
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) restart product-db

logs-local: ## 📜 Muestra logs de todos los servicios locales
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) logs -f


logs-dev: ## 📜 Muestra logs del entorno dev
	docker compose -f docker-compose.yml --env-file $(ENV_FILE_DEV) logs -f


check-db-init: ## 🔎 Verifica existencia del volumen de base de datos
	@echo "🔎 Verificando si el volumen de la base de datos existe..."
	@if ! docker volume inspect inventoryms_product-data >/dev/null 2>&1; then \
		echo "✅ Volumen no existe. Se ejecutará el script SQL al levantar PostgreSQL."; \
	else \
		echo "⚠️ Volumen ya existe. PostgreSQL NO ejecutará el script de inicialización."; \
	fi
	@$(MAKE) up-local


reset-local: # 🔁 Reinicia completamente el entorno local (infra)
	@echo "🔻 Apagando contenedores..."
	$(MAKE) down-local
	@echo "🧹 Limpiando recursos no usados de Docker..."
	$(MAKE) clean-docker
	@echo "🚀 Levantando entorno local limpio..."
	$(MAKE) up-local

stop-all: ## 🛑 Detiene todos los contenedores activos en Docker
	@echo "🛑 Deteniendo todos los contenedores..."
	docker ps -q | xargs -r docker stop
	docker system prune -f --volumes

