#------------------------------------------------------------------------
# 📦 Variables de entorno
#------------------------------------------------------------------------
ENV_FILE_LOCAL ?= .env.local
ENV_FILE_DEV ?= .env.dev
ENV_FILE_PROD ?= .env.prod

#------------------------------------------------------------------------
# 🧑‍💻 Funciones de desarrollo LOCAL (sin Docker)
#------------------------------------------------------------------------
validate-product: ## ✅ Valida la compilación y tests de product-service (sin Docker)
	cd .. && ./gradlew :product-service:build

validate-product-no-tests: ## ✅ Valida product-service sin ejecutar pruebas
	cd .. && ./gradlew :product-service:build -x test

clean-gradle: ## 🧹 Limpia caché y compilación de Gradle
	cd .. && ./gradlew clean

#------------------------------------------------------------------------
# 🐳 Funciones que USAN Docker Compose
#------------------------------------------------------------------------

## 🔽 Infraestructura por entorno
up-local: ## 🧪 Levanta infra para trabajar desde IntelliJ
	@echo "🧹 Copiando variables de entorno..."
	cp $(ENV_FILE_LOCAL) .env.override

	@echo "🔨 Construyendo servicios..."
	cd .. && ./gradlew :config-service:bootJar :discovery-service:bootJar :product-service:bootJar

	@echo "🧼 Apagando contenedores anteriores..."
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) down -v

	@echo "🐳 Levantando contenedores..."
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

up-db: ## 🔄 Levanta solo la base de datos para desarrollo local
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) up -d product-db

## 🔁 Build y Rebuild
build-images: ## 🏗️ Build manual de imágenes
	docker build -t inventory/discovery-service ../discovery-service
	docker build -t inventory/config-service ../config-service
	docker build -t inventory/product-service ../product-service

build-jars:
	cd .. && ./gradlew --console=plain :config-service:bootJar :discovery-service:bootJar :product-service:bootJar

rebuild-config-service-local: ## 🔁 Reconstruye solo config-service (local)
	@echo "🛠️  Reconstruyendo config-service con env: $(ENV_FILE_LOCAL)"
	cp $(ENV_FILE_LOCAL) .env.override
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) build config-service
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) up -d config-service

rebuild-discovery-local: ## 🔁 Reconstruye solo discovery-service (local)
	@echo "🛠️  Reconstruyendo discovery-service con env: $(ENV_FILE_LOCAL)"
	cp $(ENV_FILE_LOCAL) .env.override
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) build discovery-service
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) up -d discovery-service

rebuild-product-local: ## 🔁 Reconstruye solo product-service (local)
	@echo "🛠️  Reconstruyendo product-service con env: $(ENV_FILE_LOCAL)"
	cp $(ENV_FILE_LOCAL) .env.override
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) build product-service
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) up -d product-service

rebuild-product-dev: ## 🔁 Reconstruye solo product-service (dev)
	@echo "🛠️  Reconstruyendo product-service con env: $(ENV_FILE_DEV)"
	cp $(ENV_FILE_DEV) .env.override
	docker compose -f docker-compose.yml --env-file $(ENV_FILE_DEV) build product-service
	docker compose -f docker-compose.yml --env-file $(ENV_FILE_DEV) up -d product-service

## ♻️ Mantenimiento y limpieza
clean-volumes: ## 🧹 Limpia volúmenes no usados
	docker volume prune -f

clean-images: ## 🧹 Limpia imágenes sin contenedor
	docker image prune -a -f

clean-docker: ## 🧹 Limpia imágenes sin contenedor y volúmenes
	$(MAKE) clean-images
	$(MAKE) clean-volumes

stop-all: ## 🛑 Detiene todos los contenedores activos en Docker
	@echo "🛑 Deteniendo todos los contenedores..."
	docker ps -q | xargs -r docker stop
	docker system prune -f --volumes

reset-db: ## 🧨 Reinicia product-db con script inicial
	@echo "⛔ Eliminando contenedor y volumen de product-db..."
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) down -v --remove-orphans
	@echo "🚀 Levantando product-db..."
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) up --build -d product-db
	@echo "📋 Mostrando logs..."
	docker logs -f product-db

reset-local: ## 🔁 Reinicia completamente el entorno local
	$(MAKE) down-local
	$(MAKE) clean-docker
	$(MAKE) up-local

reload-schema: ## ♻️ Ejecuta el script SQL en product-db
	docker cp ./init-scripts/product-init.sql product-db:/tmp/product-init.sql
	docker exec -it product-db psql -U product -d product -f /tmp/product-init.sql

## 🔍 Logs y verificación
logs-local: ## 📜 Logs de todos los servicios locales
	docker compose -f docker-compose-local.yml --env-file $(ENV_FILE_LOCAL) logs -f

logs-dev: ## 📜 Logs del entorno dev
	docker compose -f docker-compose.yml --env-file $(ENV_FILE_DEV) logs -f

check-db-init: ## 🔎 Verifica existencia del volumen product-db
	@echo "🔎 Verificando volumen de la base de datos..."
	@if ! docker volume inspect inventoryms_product-data >/dev/null 2>&1; then \
		echo "✅ Volumen no existe. Se ejecutará el script SQL al levantar PostgreSQL."; \
	else \
		echo "⚠️ Volumen ya existe. PostgreSQL NO ejecutará el script de inicialización."; \
	fi
	@$(MAKE) up-local
# Construcción paso a paso con verificación
build-step-by-step:
	@echo "🧹 Limpiando proyecto..."
	cd .. && ./gradlew clean
	@echo "🔨 Construyendo config-service..."
	cd .. && ./gradlew :config-service:bootJar
	@echo "🔨 Construyendo discovery-service..."
	cd .. && ./gradlew :discovery-service:bootJar
	@echo "🔨 Construyendo product-service..."
	cd .. && ./gradlew :product-service:bootJar
	@echo "✅ Verificando JARs creados..."
	@ls -la ../*/build/libs/*.jar || echo "❌ Algunos JARs no se crearon"
	@echo "🎉 Construcción completada!"
