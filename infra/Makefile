#------------------------------------------------------------------------
# 📦 Variables de entorno
#------------------------------------------------------------------------
ENV_FILE_LOCAL ?= .env.local
ENV_FILE_DEV ?= .env.dev
ENV_FILE_PROD ?= .env.prod

#------------------------------------------------------------------------
# 📖 Utilidades
#------------------------------------------------------------------------
help:
	@grep -E '^#|^[a-zA-Z_-]+:.*?## ' $(MAKEFILE_LIST) | \
	awk 'BEGIN {FS = ":.*?## "}; /^[^#].*/ {printf "  \033[36m%-20s\033[0m %s\n", $$1, $$2}'

#------------------------------------------------------------------------
# 🧹 Limpieza y mantenimiento
#------------------------------------------------------------------------
clean-volumes: ## 🧹 Limpia volúmenes no usados
	docker volume prune -f

clean-images: ## 🧹 Limpia imágenes sin contenedor
	docker image prune -a -f

clean-docker: ## 🧹 Limpia imágenes sin contenedor y volúmenes no usados
	$(MAKE) clean-images
	$(MAKE) clean-volumes

stop-all: ## 🛑 Detiene todos los contenedores activos en Docker
	@echo "🛑 Deteniendo todos los contenedores..."
	docker ps -q | xargs -r docker stop
	docker system prune -f --volumes

#------------------------------------------------------------------------
# 🚀 Levantar entornos
#------------------------------------------------------------------------
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

#------------------------------------------------------------------------
# 🏗️ Build y Rebuild
#------------------------------------------------------------------------
build-images: ## 🏗️ Build manual de imágenes (opcional si ya usas build automático)
	docker build -t inventory/discovery-service ../discovery-service
	docker build -t inventory/config-service ../config-service
	docker build -t inventory/product-service ../product-service

build-jars:
	cd .. && ./gradlew --console=plain :config-service:bootJar :discovery-service:bootJar :product-service:bootJar

rebuild-config-service-local: ## 🔁 Reconstruye solo config-service (entorno local)
	@echo "🛠️  Reconstruyendo config-service con env: $(ENV_FILE_LOCAL)"
	cp $(ENV_FILE_LOCAL) .env.override
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) build config-service
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) up -d config-service

rebuild-discovery-local: ## 🔁 Reconstruye solo discovery-service (entorno local)
	@echo "🛠️  Reconstruyendo discovery-service con env: $(ENV_FILE_LOCAL)"
	cp $(ENV_FILE_LOCAL) .env.override
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) build discovery-service
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) up -d discovery-service

rebuild-product-local: ## 🔁 Reconstruye solo product-service (entorno local)
	@echo "🛠️  Reconstruyendo imagen del product-service con env: $(ENV_FILE_LOCAL)"
	cp $(ENV_FILE_LOCAL) .env.override
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) build product-service
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) up -d product-service

rebuild-product-dev: ## 🔁 Reconstruye solo product-service (entorno dev)
	@echo "🛠️  Reconstruyendo imagen del product-service con env: $(ENV_FILE_DEV)"
	cp $(ENV_FILE_DEV) .env.override
	docker compose -f docker-compose.yml --env-file $(ENV_FILE_DEV) build product-service
	docker compose -f docker-compose.yml --env-file $(ENV_FILE_DEV) up -d product-service

#------------------------------------------------------------------------
# 🔄 Reinicios y recarga
#------------------------------------------------------------------------
reset-db: ## 🧨 Elimina y vuelve a crear product-db con datos iniciales
	@echo "⛔ Eliminando contenedor y volumen de product-db..."
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) down -v --remove-orphans
	@echo "🚀 Levantando solo product-db..."
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) up --build -d product-db
	@echo "📋 Mostrando logs de product-db..."
	docker logs -f product-db

reset-local: ## 🔁 Reinicia completamente el entorno local (infra)
	@echo "🔻 Apagando contenedores..."
	$(MAKE) down-local
	@echo "🧹 Limpiando recursos no usados de Docker..."
	$(MAKE) clean-docker
	@echo "🚀 Levantando entorno local limpio..."
	$(MAKE) up-local

reload-schema: ## ♻️ Ejecuta manualmente el script SQL en product-db
	docker cp ./init-scripts/product-init.sql product-db:/tmp/product-init.sql
	docker exec -it product-db psql -U product -d product -f /tmp/product-init.sql

#------------------------------------------------------------------------
# 🔍 Logs y verificación
#------------------------------------------------------------------------
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

validate-product: ## ✅ Valida la compilación y tests de product-service (sin Docker)
	cd .. && ./gradlew :product-service:build

validate-product-no-tests: ## ✅ Valida product-service sin ejecutar pruebas
	cd .. && ./gradlew :product-service:build -x test