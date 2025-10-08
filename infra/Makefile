#------------------------------------------------------------------------
# 📦 Variables de entorno
#------------------------------------------------------------------------
ENV_FILE_LOCAL ?= .env.local
ENV_FILE_DEV   ?= .env.dev
ENV_FILE_PROD  ?= .env.prod

# 🆕 Archivo de compose unificado
COMPOSE_FILE_LOCAL ?= docker-compose.yml
COMPOSE_FILE_DEV   ?= docker-compose.yml
COMPOSE_FILE_PROD  ?= docker-compose.yml

# 🧰 Helpers DRY para Podman Compose por entorno
COMPOSE_LOCAL := podman-compose -f $(COMPOSE_FILE_LOCAL) --env-file $(ENV_FILE_LOCAL)
COMPOSE_DEV   := podman-compose -f $(COMPOSE_FILE_DEV)   --env-file $(ENV_FILE_DEV)
COMPOSE_PROD  := podman-compose -f $(COMPOSE_FILE_PROD)  --env-file $(ENV_FILE_PROD)

# 🔖 Perfiles disponibles (deben existir en docker-compose-no-usar.yml)
PROFILES_ALL := infra gateway product supplier purchase

# ====== Vars seguras (rutas absolutas) ======
CURDIR := $(abspath $(dir $(lastword $(MAKEFILE_LIST))))
ENV_FILE_LOCAL     ?= $(CURDIR)/.env.local

# Usa UNO o VARIOS compose files (separados por espacio)
# Ejemplos:
#   make up-local-supplier_fix COMPOSE_FILES="$(CURDIR)/docker-compose-podman.yml"
#   make up-local-supplier_fix COMPOSE_FILES="$(CURDIR)/docker-compose-no-usar.yml $(CURDIR)/docker-compose.supplier.yml"
COMPOSE_FILES ?= $(CURDIR)/docker-compose.yml

# Helper para expandir -f por cada archivo
COMPOSE_F := $(foreach f,$(COMPOSE_FILES),-f $(f))

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
# ♻️ Targets genéricos por perfil (UP/DOWN/LOGS)
#------------------------------------------------------------------------
up-%: ## Levanta por perfil: infra|gateway|product|supplier|purchase|all
	@if [ "$*" = "all" ]; then \
		$(COMPOSE_LOCAL) $(foreach p,$(PROFILES_ALL),--profile $(p)) up -d --build; \
	else \
		$(COMPOSE_LOCAL) --profile $* up -d --build; \
	fi

down-%: ## Baja por perfil: infra|gateway|product|supplier|purchase|all
	@if [ "$*" = "all" ]; then \
		$(COMPOSE_LOCAL) $(foreach p,$(PROFILES_ALL),--profile $(p)) down -v --remove-orphans; \
	else \
		$(COMPOSE_LOCAL) --profile $* down -v --remove-orphans; \
	fi

logs-%: ## Logs por perfil: infra|gateway|product|supplier|purchase
	$(COMPOSE_LOCAL) --profile $* logs -f

#------------------------------------------------------------------------
# 🦭 Funciones que USAN Podman Compose
#------------------------------------------------------------------------

## 🔽 Infraestructura por entorno
up-local: fix-container-orphans ## 🧪 Infra + product + gateway para trabajar desde IntelliJ
	@echo "🧹 Copiando variables de entorno..."
	cp $(ENV_FILE_LOCAL) .env.override
	@echo "🔨 Construyendo servicios..."
	cd .. && ./gradlew :config-service:bootJar :discovery-service:bootJar :product-service:bootJar :api-gateway:bootJar
	@echo "🦭 Levantando contenedores con Podman (perfiles: infra, product, gateway)..."
	$(COMPOSE_LOCAL) --profile infra --profile product --profile gateway up -d --build


## 🔽 Levantar solo supplier (más la base)
up-local-product-supplier: fix-container-orphans
	@echo "🧹 Copiando variables de entorno..."
	cp "$(ENV_FILE_LOCAL)" "$(CURDIR)/.env.override"

	@COMPOSE_FILE="$(CURDIR)/docker-compose-podman.yml"; \
	echo "🔎 Verificando que existan supplier-db/supplier-service y product-db/product-service en $$COMPOSE_FILE..."; \
	SERVICES="$$(COMPOSE_PROFILES=infra,supplier,product podman-compose -f $$COMPOSE_FILE --env-file "$(ENV_FILE_LOCAL)" \
	  --profile infra --profile supplier --profile product config --services 2>/dev/null || true)"; \
	echo "$$SERVICES" | grep -qx "supplier-db"      || { echo "❌ Falta supplier-db"; exit 1; }; \
	echo "$$SERVICES" | grep -qx "supplier-service" || { echo "❌ Falta supplier-service"; exit 1; }; \
	echo "$$SERVICES" | grep -qx "product-db"       || { echo "❌ Falta product-db"; exit 1; }; \
	echo "$$SERVICES" | grep -qx "product-service"  || { echo "❌ Falta product-service"; exit 1; }; \
	echo "✅ Servicios detectados: supplier y product."; \

	@echo "🔨 Construyendo jars necesarios (incluye product)..."
	cd "$(CURDIR)/.." && ./gradlew \
		:config-service:bootJar \
		:discovery-service:bootJar \
		:product-service:bootJar \
		:supplier-service:bootJar

	@echo "🦭 Levantando discovery, config, product-db, product-service, supplier-db y supplier-service..."
	COMPOSE_PROFILES=infra,product,supplier \
	podman-compose -f "$(CURDIR)/docker-compose-podman.yml" --env-file "$(ENV_FILE_LOCAL)" \
	  --profile infra --profile product --profile supplier \
	  up -d --build \
	  discovery-service config-service \
	  product-db product-service \
	  supplier-db supplier-service

up-local-purchase: fix-container-orphans ## 🧪 Base + purchase
	@echo "🧹 Copiando variables de entorno..."
	cp $(ENV_FILE_LOCAL) .env.override

	@echo "🔨 Construyendo base + purchase..."
	cd .. && ./gradlew \
		:config-service:bootJar \
		:discovery-service:bootJar \
		:api-gateway:bootJar \
		:product-service:bootJar \
		:purchase-service:bootJar

	@echo "🦭 Levantando contenedores (infra, product, gateway, purchase)..."
	podman-compose -f $(COMPOSE_FILE_LOCAL) --env-file $(ENV_FILE_LOCAL) \
		--profile infra --profile gateway --profile product \
		--profile purchase \
		up -d --build

# ---------------------------------------------------------------------
# 🧪 Supplier Service
# ---------------------------------------------------------------------
rebuild-supplier-local: ## 🔁 Rebuild supplier-service (local)
	@echo "🛠️  Rebuild supplier-service (local)"
	cp $(ENV_FILE_LOCAL) .env.override
	cd .. && ./gradlew :supplier-service:bootJar
	$(COMPOSE_LOCAL) --profile supplier build supplier-service
	$(COMPOSE_LOCAL) --profile supplier up -d supplier-db supplier-service

rebuild-supplier-dev: ## 🔁 Rebuild supplier-service (dev)
	@echo "🛠️  Rebuild supplier-service (dev)"
	cp $(ENV_FILE_DEV) .env.override
	$(COMPOSE_DEV) build supplier-service
	$(COMPOSE_DEV) up -d supplier-service

run-supplier-local: ## 🚀 Ejecutar supplier-service local (sin contenedor)
	@echo "🚀 supplier-service local en http://localhost:$(SUPPLIER_PORT)"
	cd ../supplier-service && ./gradlew bootRun --args='--spring.profiles.active=local'

test-supplier: ## 🧪 Tests supplier-service
	cd ../supplier-service && ./gradlew test

health-supplier: ## 💚 Health de supplier-service
	@curl -fsS http://localhost:$(SUPPLIER_PORT)/actuator/health | jq . || echo "❌ supplier-service no responde"

# ---------------------------------------------------------------------
# 🧪 Purchase Service
# ---------------------------------------------------------------------
rebuild-purchase-local: ## 🔁 Rebuild purchase-service (local)
	@echo "🛠️  Rebuild purchase-service (local)"
	cp $(ENV_FILE_LOCAL) .env.override
	cd .. && ./gradlew :purchase-service:bootJar
	$(COMPOSE_LOCAL) build purchase-service
	$(COMPOSE_LOCAL) up -d purchase-service

rebuild-purchase-dev: ## 🔁 Rebuild purchase-service (dev)
	@echo "🛠️  Rebuild purchase-service (dev)"
	cp $(ENV_FILE_DEV) .env.override
	$(COMPOSE_DEV) build purchase-service
	$(COMPOSE_DEV) up -d purchase-service

run-purchase-local: ## 🚀 Ejecutar purchase-service local (sin contenedor)
	@echo "🚀 purchase-service local en http://localhost:$(PURCHASE_PORT)"
	cd ../purchase-service && ./gradlew bootRun --args='--spring.profiles.active=local'

test-purchase: ## 🧪 Tests purchase-service
	cd ../purchase-service && ./gradlew test

health-purchase: ## 💚 Health de purchase-service
	@curl -fsS http://localhost:$(PURCHASE_PORT)/actuator/health | jq . || echo "❌ purchase-service no responde"

down-local: ## 🔻 Apaga infra local (perfiles: infra, product, gateway)
	$(COMPOSE_LOCAL) --profile infra --profile product --profile gateway down -v --remove-orphans

up-dev: ## 🧪 Levanta todo el stack en contenedores (modo dev)
	cp $(ENV_FILE_DEV) .env.override
	$(COMPOSE_DEV) $(foreach p,$(PROFILES_ALL),--profile $(p)) down -v --remove-orphans
	$(COMPOSE_DEV) $(foreach p,$(PROFILES_ALL),--profile $(p)) up -d --build

up-prod: ## 🚀 Levanta todo en entorno de producción
	cp $(ENV_FILE_PROD) .env.override
	$(COMPOSE_PROD) $(foreach p,$(PROFILES_ALL),--profile $(p)) down -v --remove-orphans
	$(COMPOSE_PROD) $(foreach p,$(PROFILES_ALL),--profile $(p)) up -d --build

up-db: ## 🔄 Levanta solo la base de datos para desarrollo local (product-db)
	$(COMPOSE_LOCAL) --profile product up -d product-db

## 🔁 Build y Rebuild
build-images: ## 🏗️ Build manual de imágenes
	podman build -t inventory/discovery-service ../discovery-service
	podman build -t inventory/config-service ../config-service
	podman build -t inventory/api-gateway ../api-gateway
	podman build -t inventory/product-service ../product-service

build-jars:
	cd .. && ./gradlew --console=plain :config-service:bootJar :discovery-service:bootJar :api-gateway:bootJar :product-service:bootJar

# 🆕 Comandos para servicios individuales
rebuild-api-gateway-local: ## 🔁 Reconstruye solo api-gateway (local)
	@echo "🛠️  Reconstruyendo api-gateway con env: $(ENV_FILE_LOCAL)"
	cp $(ENV_FILE_LOCAL) .env.override
	cd .. && ./gradlew :api-gateway:bootJar
	$(COMPOSE_LOCAL) build api-gateway
	$(COMPOSE_LOCAL) up -d api-gateway

rebuild-config-service-local: ## 🔁 Reconstruye solo config-service (local)
	@echo "🛠️  Reconstruyendo config-service con env: $(ENV_FILE_LOCAL)"
	cp $(ENV_FILE_LOCAL) .env.override
	$(COMPOSE_LOCAL) build config-service
	$(COMPOSE_LOCAL) up -d config-service

rebuild-discovery-local: ## 🔁 Reconstruye solo discovery-service (local)
	@echo "🛠️  Reconstruyendo discovery-service con env: $(ENV_FILE_LOCAL)"
	cp $(ENV_FILE_LOCAL) .env.override
	$(COMPOSE_LOCAL) build discovery-service
	$(COMPOSE_LOCAL) up -d discovery-service

rebuild-product-local: ## 🔁 Reconstruye solo product-service (local)
	@echo "🛠️  Reconstruyendo product-service con env: $(ENV_FILE_LOCAL)"
	cp $(ENV_FILE_LOCAL) .env.override
	$(COMPOSE_LOCAL) build product-service
	$(COMPOSE_LOCAL) up -d product-service

rebuild-product-dev: ## 🔁 Reconstruye solo product-service (dev)
	@echo "🛠️  Reconstruyendo product-service con env: $(ENV_FILE_DEV)"
	cp $(ENV_FILE_DEV) .env.override
	$(COMPOSE_DEV) build product-service
	$(COMPOSE_DEV) up -d product-service

## ♻️ Mantenimiento y limpieza
clean-volumes: ## 🧹 Limpia volúmenes no usados
	podman volume prune -f

clean-images: ## 🧹 Limpia imágenes sin contenedor
	podman image prune -a -f

clean-podman: ## 🧹 Limpia imágenes sin contenedor y volúmenes
	$(MAKE) clean-images
	$(MAKE) clean-volumes

stop-all: ## 🛑 Detiene todos los contenedores activos en Podman
	@echo "🛑 Deteniendo todos los contenedores..."
	podman ps -q | xargs -r podman stop
	podman system prune -f --volumes

reset-db: ## 🧨 Reinicia product-db con script inicial
	@echo "⛔ Eliminando contenedor y volumen de product-db..."
	$(COMPOSE_LOCAL) down -v --remove-orphans
	@echo "🚀 Levantando product-db..."
	$(COMPOSE_LOCAL) up --build -d product-db
	@echo "📋 Mostrando logs..."
	podman logs -f product-db

reset-local: ## 🔁 Reinicia completamente el entorno local
	$(MAKE) down-local
	$(MAKE) clean-podman
	$(MAKE) up-local

reload-schema: ## ♻️ Ejecuta el script SQL en product-db
	podman cp ./init-scripts/product-init.sql product-db:/tmp/product-init.sql
	podman exec -it product-db psql -U product -d product -f /tmp/product-init.sql

## 🔍 Logs y verificación
logs-local: ## 📜 Logs de todos los servicios locales
	$(COMPOSE_LOCAL) logs -f

logs-dev: ## 📜 Logs del entorno dev
	$(COMPOSE_DEV) logs -f

# 🆕 Logs específicos por servicio
logs-api-gateway: ## 📜 Logs del API Gateway
	$(COMPOSE_LOCAL) logs -f api-gateway

logs-discovery: ## 📜 Logs del Discovery Service
	$(COMPOSE_LOCAL) logs -f discovery-service

logs-config: ## 📜 Logs del Config Service
	$(COMPOSE_LOCAL) logs -f config-service

logs-product: ## 📜 Logs del Product Service
	$(COMPOSE_LOCAL) logs -f product-service

logs-db: ## 📜 Logs de la base de datos
	$(COMPOSE_LOCAL) logs -f product-db

check-db-init: ## 🔎 Verifica existencia del volumen product-db
	@echo "🔎 Verificando volumen de la base de datos..."
	@if ! podman volume inspect inventoryms_product-data >/dev/null 2>&1; then \
		echo "✅ Volumen no existe. Se ejecutará el script SQL al levantar PostgreSQL."; \
	else \
		echo "⚠️ Volumen ya existe. PostgreSQL NO ejecutará el script de inicialización."; \
	fi
	@$(MAKE) up-local

# 🆕 Status y testing
status: ## 📊 Ver estado de todos los servicios
	$(COMPOSE_LOCAL) ps

podman-status: ## 📊 Ver todos los contenedores Podman
	podman ps -a

test-services: ## 🧪 Probar que todos los servicios estén funcionando
	@echo "🧪 Probando servicios..."
	@echo "Discovery Service (8761):"
	@curl -s http://localhost:8761/actuator/health | jq . || echo "❌ Discovery Service no responde"
	@echo "\nConfig Service (7777):"
	@curl -s http://localhost:7777/actuator/health | jq . || echo "❌ Config Service no responde"
	@echo "\nAPI Gateway (8090):"
	@curl -s http://localhost:8090/actuator/health | jq . || echo "❌ API Gateway no responde"
	@echo "\nProduct Service (9090):"
	@curl -s http://localhost:9090/actuator/health | jq . || echo "❌ Product Service no responde"

# Construcción paso a paso con verificación
build-step-by-step:
	@echo "🧹 Limpiando proyecto..."
	cd .. && ./gradlew clean
	@echo "🔨 Construyendo config-service..."
	cd .. && ./gradlew :config-service:bootJar
	@echo "🔨 Construyendo discovery-service..."
	cd .. && ./gradlew :discovery-service:bootJar
	@echo "🔨 Construyendo api-gateway..."
	cd .. && ./gradlew :api-gateway:bootJar
	@echo "🔨 Construyendo product-service..."
	cd .. && ./gradlew :product-service:bootJar
	@echo "✅ Verificando JARs creados..."
	@ls -la ../*/build/libs/*.jar || echo "❌ Algunos JARs no se crearon"
	@echo "🎉 Construcción completada con Podman!"

# 🆕 Comandos adicionales para Podman
pods-list: ## 📋 Lista todos los pods de Podman
	podman pod ps

pods-stats: ## 📊 Estadísticas de recursos de Podman
	podman stats --no-stream

images-list: ## 🖼️ Lista todas las imágenes de Podman
	podman images

system-info: ## ℹ️ Información del sistema Podman
	podman system info

podman-start: ## 🚀 Inicia la máquina virtual de Podman
	podman machine start

podman-machine-list: ## 📋 Lista todas las máquinas virtuales de Podman
	podman machine list

podman-view-all: ## 👀 Ver todos los contenedores (activos e inactivos)
	podman ps -a

podman-remove-all: ## 🗑️ Eliminar todos los contenedores
	podman rm --all --force

#------------------------------------------------------------------------
# 🧪 Product Service - Comandos específicos para desarrollo local
#------------------------------------------------------------------------

## 🧪 Testing del Product Service
test-product: ## 🧪 Ejecutar todos los tests del product-service
	@echo "🧪 Ejecutando tests del product-service..."
	cd ../product-service && ./gradlew test

test-product-unit: ## 🧪 Ejecutar solo tests unitarios del product-service
	@echo "🧪 Ejecutando tests unitarios del product-service..."
	cd ../product-service && ./gradlew test --tests "*Test"

test-product-controller: ## 🧪 Testear ProductController específicamente
	@echo "🧪 Testeando ProductController..."
	cd ../product-service && ./gradlew test --tests ProductControllerTest

test-product-app: ## 🧪 Testear carga de aplicación del product-service
	@echo "🧪 Testeando carga de aplicación..."
	cd ../product-service && ./gradlew test --tests ProductServiceApplicationTests

test-product-health: ## 🧪 Testear health endpoint del product-service
	@echo "🧪 Testeando health endpoint..."
	cd ../product-service && ./gradlew test --tests HealthCheckTest

test-product-report: ## 📊 Abrir reporte de tests del product-service
	@echo "📊 Abriendo reporte de tests..."
	@open ../product-service/build/reports/tests/test/index.html || echo "❌ No se pudo abrir el reporte. Ejecuta 'make test-product' primero."

test-product-local: ## 🧪 Ejecutar tests con perfil local
	@echo "🧪 Ejecutando tests con perfil local..."
	cd ../product-service && ./gradlew test --args='--spring.profiles.active=local'

## 🚀 Desarrollo local del Product Service (sin contenedores)
run-product-local: ## 🚀 Ejecutar product-service localmente con perfil local (puerto 9090)
	@echo "🚀 Iniciando product-service en modo desarrollo local con perfil 'local'..."
	@echo "📍 La aplicación estará disponible en: http://localhost:9090"
	@echo "🧪 Endpoint de prueba: http://localhost:9090/api/v1/products/test"
	@echo "💚 Health check: http://localhost:9090/actuator/health"
	@echo "🔧 Usando perfil: local (localhost:5432)"
	cd ../product-service && ./gradlew bootRun --args='--spring.profiles.active=local'

run-product-dev: ## 🚀 Ejecutar product-service con perfil dev (para contenedores)
	@echo "🚀 Iniciando product-service en modo desarrollo con perfil 'dev'..."
	@echo "🔧 Usando perfil: dev (product-db:5432)"
	cd ../product-service && ./gradlew bootRun --args='--spring.profiles.active=dev'

run-product-profile: ## 🚀 Ejecutar product-service con perfil específico (usar PROFILE=nombre)
	@echo "🚀 Iniciando product-service con perfil: $(PROFILE)"
	cd ../product-service && ./gradlew bootRun --args='--spring.profiles.active=$(PROFILE)'

dev-product: run-product-local ## 🚀 Alias para run-product-local

stop-product-local: ## 🛑 Detener product-service local
	@echo "🛑 Deteniendo product-service local..."
	@pkill -f "product-service" || echo "ℹ️  No hay procesos del product-service ejecutándose"

## 🔨 Build del Product Service
build-product: ## 🔨 Construir product-service para producción
	@echo "🔨 Construyendo product-service para producción..."
	cd ../product-service && ./gradlew build

build-product-no-tests: ## 🔨 Construir product-service sin tests
	@echo "🔨 Construyendo product-service sin tests..."
	cd ../product-service && ./gradlew build -x test

clean-product: ## 🧹 Limpiar archivos temporales del product-service
	@echo "🧹 Limpiando product-service..."
	cd ../product-service && ./gradlew clean

rebuild-product-local-dev: clean-product build-product ## ♻️ Limpiar y construir product-service

## ✅ Calidad del Product Service
check-product: clean-product test-product build-product ## ✅ Verificación completa del product-service (clean + test + build)
	@echo "✅ Verificación de calidad del product-service completada"

lint-product: ## 🔍 Verificar estilo de código del product-service
	@echo "🔍 Verificando estilo de código del product-service..."
	cd ../product-service && ./gradlew check

## 📦 Dependencias del Product Service
deps-product: ## 📦 Mostrar dependencias del product-service
	@echo "📦 Mostrando dependencias del product-service..."
	cd ../product-service && ./gradlew dependencies

deps-product-update: ## 📦 Actualizar dependencias del product-service
	@echo "📦 Actualizando dependencias del product-service..."
	cd ../product-service && ./gradlew build --refresh-dependencies

## 🛠️ Utilidades del Product Service
health-product: ## 💚 Verificar health del product-service
	@echo "💚 Verificando health del product-service..."
	@curl -f http://localhost:9090/actuator/health 2>/dev/null && echo "\n✅ Product Service funcionando correctamente" || echo "❌ Product Service no responde. ¿Está ejecutándose?"

test-endpoints-product: ## 🧪 Probar endpoints principales del product-service
	@echo "🧪 Probando endpoints del product-service..."
	@echo "💚 Health endpoint:"
	@curl -s http://localhost:9090/actuator/health | jq .status || echo "❌ Health endpoint no responde"
	@echo "\n🧪 Test endpoint:"
	@curl -s http://localhost:9090/api/v1/products/test || echo "❌ Test endpoint no responde"
	@echo "\n📦 Products endpoint (básico):"
	@curl -s "http://localhost:9090/api/v1/products" | jq '. | length' || echo "❌ Products endpoint no responde"
	@echo "\n📄 Paginación endpoint:"
	@curl -s "http://localhost:9090/api/v1/products/paginated?size=3&sortBy=createdAt&sortDir=desc" | jq '.content | length' || echo "❌ Paginación endpoint no responde"

test-pagination-endpoints: ## 🧪 Probar endpoints de paginación con filtros
	@echo "🧪 Probando endpoints de paginación del product-service..."
	@echo "📄 Paginación básica:"
	@curl -s "http://localhost:9090/api/v1/products/paginated?size=5&sortBy=createdAt&sortDir=desc" | jq . || echo "❌ Paginación básica no responde"
	@echo "\n🔍 Paginación con filtro por código:"
	@curl -s "http://localhost:9090/api/v1/products/paginated?size=5&sortBy=name&sortDir=asc&code=ABC" | jq . || echo "❌ Filtro por código no responde"
	@echo "\n🔍 Paginación con filtro por nombre:"
	@curl -s "http://localhost:9090/api/v1/products/paginated?size=5&sortBy=name&sortDir=asc&name=test" | jq . || echo "❌ Filtro por nombre no responde"
	@echo "\n🔍 Paginación con múltiples filtros:"
	@curl -s "http://localhost:9090/api/v1/products/paginated?size=10&sortBy=name&sortDir=asc&code=ABC&description=test" | jq . || echo "❌ Múltiples filtros no responden"

info-product: ## ℹ️ Información del product-service
	@echo "ℹ️  Información del Product Service:"
	@echo "  📁 Proyecto: product-service"
	@echo "  ☕ Java: 17"
	@echo "  🔧 Build: Gradle"
	@echo "  🗄️  BD Producción: PostgreSQL"
	@echo "  🧪 BD Tests: H2"
	@echo "  🌐 Puerto: 9090"
	@echo "  📍 Endpoints principales:"
	@echo "    - GET /api/v1/products/test"
	@echo "    - GET /api/v1/products"
	@echo "    - GET /api/v1/products/paginated"
	@echo "    - GET /actuator/health"

## 🎯 Comandos rápidos del Product Service
quick-test-product: ## ⚡ Test rápido del product-service
	@echo "⚡ Test rápido del product-service..."
	cd ../product-service && ./gradlew test --parallel

quick-build-product: ## ⚡ Build rápido del product-service
	@echo "⚡ Build rápido del product-service..."
	cd ../product-service && ./gradlew build -x test

## 📊 Reportes del Product Service
coverage-product: ## 📊 Generar reporte de cobertura del product-service
	@echo "📊 Generando reporte de cobertura del product-service..."
	cd ../product-service && ./gradlew jacocoTestReport
	@echo "📊 Reporte disponible en: ../product-service/build/reports/jacoco/test/html/index.html"

## 🔄 Comandos integrados (Product Service + Infraestructura)
dev-full-stack: up-db run-product-local ## 🚀 Levantar BD en Docker + Product Service local
	@echo "🎉 Full stack listo: BD en Docker + Product Service local"

test-integration-product: up-db ## 🧪 Tests de integración con BD real
	@echo "🧪 Ejecutando tests de integración con BD PostgreSQL..."
	@sleep 5
	cd ../product-service && ./gradlew test --args='--spring.profiles.active=local'

## ✅ Validación completa de funcionalidad
validate-pagination-feature: health-product test-pagination-endpoints ## ✅ Validar funcionalidad completa de paginación con filtros
	@echo "✅ Validación de funcionalidad de paginación completada"

#------------------------------------------------------------------------
# 🆕 Nuevas funcionalidades agregadas
#------------------------------------------------------------------------

## 📱 Monitoreo y debugging avanzado
monitor-resources: ## 📊 Monitorear recursos del sistema en tiempo real
	@echo "📊 Monitoreando recursos del sistema..."
	podman stats --format "table {{.Name}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.NetIO}}\t{{.BlockIO}}"

debug-network: ## 🌐 Debugging de la red de contenedores
	@echo "🌐 Inspeccionando red de contenedores..."
	podman network ls
	@echo "\n📋 Detalles de la red por defecto:"
	podman network inspect podman

debug-volumes: ## 💾 Debugging de volúmenes de datos
	@echo "💾 Inspeccionando volúmenes..."
	podman volume ls
	@echo "\n📋 Detalles del volumen de datos:"
	podman volume inspect inventoryms_product-data 2>/dev/null || echo "⚠️ Volumen no existe"

debug-containers: ## 🔍 Debugging detallado de contenedores
	@echo "🔍 Estado detallado de contenedores..."
	podman ps -a --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}\t{{.Image}}"

## 🔧 Comandos de mantenimiento avanzado
maintenance-full: ## 🛠️ Mantenimiento completo del sistema
	@echo "🛠️ Ejecutando mantenimiento completo..."
	$(MAKE) stop-all
	$(MAKE) clean-podman
	podman system reset --force
	@echo "✅ Mantenimiento completado"

backup-volumes: ## 💾 Crear backup de volúmenes importantes
	@echo "💾 Creando backup de volúmenes..."
	@mkdir -p ./backups/$(shell date +%Y%m%d_%H%M%S)
	podman run --rm -v inventoryms_product-data:/data -v ./backups:/backup alpine tar czf /backup/product-data-$(shell date +%Y%m%d_%H%M%S).tar.gz -C /data .
	@echo "✅ Backup creado en ./backups/"

restore-volumes: ## 🔄 Restaurar volúmenes desde backup (especificar BACKUP_FILE)
	@echo "🔄 Restaurando volúmenes desde backup: $(BACKUP_FILE)"
	podman run --rm -v inventoryms_product-data:/data -v ./backups:/backup alpine tar xzf /backup/$(BACKUP_FILE) -C /data
	@echo "✅ Volúmenes restaurados"

## 🚀 Despliegue y CI/CD helpers
deploy-staging: ## 🎭 Desplegar en entorno de staging
	@echo "🎭 Desplegando en staging..."
	$(MAKE) build-jars
	$(MAKE) up-dev
	$(MAKE) test-services
	@echo "✅ Despliegue en staging completado"

deploy-production: ## 🚀 Desplegar en entorno de producción
	@echo "🚀 Desplegando en producción..."
	$(MAKE) build-jars
	$(MAKE) up-prod
	@sleep 30
	$(MAKE) test-services
	@echo "✅ Despliegue en producción completado"

pre-commit-checks: ## ✅ Verificaciones antes de commit
	@echo "✅ Ejecutando verificaciones pre-commit..."
	$(MAKE) clean-gradle
	$(MAKE) build-jars
	$(MAKE) test-product
	$(MAKE) lint-product
	@echo "✅ Todas las verificaciones pasaron"

## 📈 Performance y benchmarking
benchmark-endpoints: ## 📈 Benchmark de endpoints principales
	@echo "📈 Ejecutando benchmark de endpoints..."
	@command -v ab >/dev/null 2>&1 || { echo "❌ Apache Bench (ab) no está instalado"; exit 1; }
	@echo "🧪 Benchmark de health endpoint:"
	ab -n 100 -c 10 http://localhost:9090/actuator/health
	@echo "\n📦 Benchmark de products endpoint:"
	ab -n 100 -c 10 http://localhost:9090/api/v1/products
	@echo "\n📄 Benchmark de paginación endpoint:"
	ab -n 100 -c 10 "http://localhost:9090/api/v1/products/paginated?size=10"

load-test-basic: ## 🎯 Test de carga básico
	@echo "🎯 Ejecutando test de carga básico..."
	@command -v wrk >/dev/null 2>&1 || { echo "❌ wrk no está instalado. Instalar con: brew install wrk"; exit 1; }
	wrk -t12 -c400 -d30s http://localhost:9090/api/v1/products

## 🔐 Seguridad y validación
security-scan: ## 🔐 Escaneo de seguridad de imágenes
	@echo "🔐 Escaneando imágenes en busca de vulnerabilidades..."
	@command -v podman >/dev/null 2>&1 || { echo "❌ Podman no está disponible"; exit 1; }
	@for image in inventory/discovery-service inventory/config-service inventory/api-gateway inventory/product-service; do \
		echo "🔍 Escaneando $$image..."; \
		podman run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image $$image || echo "⚠️ No se pudo escanear $$image"; \
	done

validate-configs: ## ✅ Validar archivos de configuración
	@echo "✅ Validando archivos de configuración..."
	@echo "🔍 Validando docker-compose.yml..."
	$(COMPOSE_LOCAL) config > /dev/null && echo "✅ docker-compose.yml válido" || echo "❌ docker-compose.yml inválido"
	@echo "🔍 Validando archivos .env..."
	@for env_file in $(ENV_FILE_LOCAL) $(ENV_FILE_DEV) $(ENV_FILE_PROD); do \
		if [ -f $$env_file ]; then \
			echo "✅ $$env_file existe"; \
		else \
			echo "❌ $$env_file no existe"; \
		fi \
	done

## 📋 Reportes y documentación
generate-reports: ## 📋 Generar reportes del proyecto
	@echo "📋 Generando reportes del proyecto..."
	@mkdir -p ./reports/$(shell date +%Y%m%d_%H%M%S)
	@echo "📊 Generando reporte de tests..."
	$(MAKE) test-product
	@echo "📈 Generando reporte de cobertura..."
	$(MAKE) coverage-product
	@echo "🏗️ Generando reporte de dependencias..."
	cd ../product-service && ./gradlew dependencyInsight --dependency org.springframework.boot > ../infrastructure/reports/$(shell date +%Y%m%d_%H%M%S)/dependencies.txt
	@echo "✅ Reportes generados en ./reports/"

system-info-full: ## ℹ️ Información completa del sistema
	@echo "ℹ️ Información completa del sistema:"
	@echo "======================================"
	@echo "🐳 Podman version:"
	podman --version
	@echo "\n🦭 Podman machine info:"
	podman machine list
	@echo "\n📊 Estadísticas del sistema:"
	podman system info | head -20
	@echo "\n📦 Imágenes disponibles:"
	podman images --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}\t{{.Created}}"
	@echo "\n🔌 Contenedores activos:"
	podman ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

## 🔄 Workflows automatizados
workflow-dev: ## 🔄 Workflow completo de desarrollo
	@echo "🔄 Iniciando workflow de desarrollo..."
	$(MAKE) clean-gradle
	$(MAKE) validate-configs
	$(MAKE) up-db
	@echo "⏳ Esperando que la BD esté lista..."
	@sleep 10
	$(MAKE) test-product-local
	$(MAKE) run-product-local &
	@echo "⏳ Esperando que el servicio esté listo..."
	@sleep 15
	$(MAKE) health-product
	$(MAKE) test-endpoints-product
	@echo "✅ Workflow de desarrollo completado"

workflow-ci: ## 🔄 Workflow de integración continua
	@echo "🔄 Iniciando workflow de CI..."
	$(MAKE) pre-commit-checks
	$(MAKE) build-step-by-step
	$(MAKE) up-dev
	@sleep 30
	$(MAKE) test-services
	$(MAKE) benchmark-endpoints
	@echo "✅ Workflow de CI completado"

workflow-production: ## 🔄 Workflow de producción
	@echo "🔄 Iniciando workflow de producción..."
	$(MAKE) security-scan
	$(MAKE) validate-configs
	$(MAKE) backup-volumes
	$(MAKE) deploy-production
	$(MAKE) load-test-basic
	@echo "✅ Workflow de producción completado"

## 🆘 Troubleshooting y diagnóstico
troubleshoot-db: ## 🆘 Diagnóstico de problemas de base de datos
	@echo "🆘 Diagnosticando problemas de base de datos..."
	@echo "🔍 Estado del contenedor de BD:"
	podman ps -a --filter name=product-db
	@echo "\n📋 Logs recientes de la BD:"
	podman logs --tail 20 product-db 2>/dev/null || echo "❌ No se pueden obtener logs de product-db"
	@echo "\n💾 Verificando volumen de datos:"
	podman volume inspect inventoryms_product-data >/dev/null 2>&1 && echo "✅ Volumen existe" || echo "❌ Volumen no existe"
	@echo "\n🌐 Verificando conectividad:"
	podman exec product-db pg_isready -U product 2>/dev/null && echo "✅ PostgreSQL respondiendo" || echo "❌ PostgreSQL no responde"

troubleshoot-network: ## 🆘 Diagnóstico de problemas de red
	@echo "🆘 Diagnosticando problemas de red..."
	@echo "🌐 Redes disponibles:"
	podman network ls
	@echo "\n🔌 Conectividad entre contenedores:"
	@for service in discovery-service config-service api-gateway product-service; do \
		echo "🧪 Probando $service..."; \
		podman exec product-service ping -c 1 $service 2>/dev/null && echo "✅ $service accesible" || echo "❌ $service no accesible"; \
	done

troubleshoot-services: ## 🆘 Diagnóstico de problemas de servicios
	@echo "🆘 Diagnosticando problemas de servicios..."
	@echo "📊 Estado de servicios:"
	$(MAKE) status
	@echo "\n🧪 Health checks:"
	$(MAKE) test-services
	@echo "\n📋 Logs de errores recientes:"
	podman-compose logs --tail 10 | grep -i error || echo "ℹ️ No se encontraron errores recientes"

fix-common-issues: ## 🔧 Solucionar problemas comunes
	@echo "🔧 Solucionando problemas comunes..."
	@echo "🧹 Limpiando recursos no utilizados..."
	$(MAKE) clean-podman
	@echo "🔄 Reiniciando máquina Podman..."
	podman machine stop 2>/dev/null || true
	podman machine start
	@echo "🚀 Recreando contenedores..."
	$(MAKE) reset-local
	@echo "✅ Problemas comunes solucionados"

## 🎯 Comandos específicos para diferentes roles
# Para desarrolladores
dev-setup: ## 👨‍💻 Configuración inicial para desarrolladores
	@echo "👨‍💻 Configurando entorno de desarrollo..."
	$(MAKE) podman-start
	$(MAKE) validate-configs
	$(MAKE) clean-gradle
	$(MAKE) up-db
	@echo "📚 Para empezar a desarrollar ejecuta: make dev-product"
	@echo "✅ Entorno de desarrollo configurado"

# Para QA/Testing
qa-setup: ## 🧪 Configuración para QA y testing
	@echo "🧪 Configurando entorno de QA..."
	$(MAKE) up-dev
	@sleep 30
	$(MAKE) test-services
	$(MAKE) test-endpoints-product
	$(MAKE) test-pagination-endpoints
	@echo "✅ Entorno de QA configurado y verificado"

# Para DevOps
devops-setup: ## ⚙️ Configuración para DevOps
	@echo "⚙️ Configurando entorno DevOps..."
	$(MAKE) system-info-full
	$(MAKE) validate-configs
	$(MAKE) security-scan
	$(MAKE) backup-volumes
	@echo "✅ Entorno DevOps configurado"

## 📊 Métricas y monitoreo
collect-metrics: ## 📊 Recopilar métricas del sistema
	@echo "📊 Recopilando métricas del sistema..."
	@mkdir -p ./metrics/$(shell date +%Y%m%d_%H%M%S)
	@echo "📈 Métricas de contenedores:" > ./metrics/$(shell date +%Y%m%d_%H%M%S)/container_stats.txt
	podman stats --no-stream >> ./metrics/$(shell date +%Y%m%d_%H%M%S)/container_stats.txt
	@echo "💾 Uso de volúmenes:" > ./metrics/$(shell date +%Y%m%d_%H%M%S)/volume_usage.txt
	podman system df >> ./metrics/$(shell date +%Y%m%d_%H%M%S)/volume_usage.txt
	@echo "✅ Métricas guardadas en ./metrics/"

health-check-all: ## 💚 Verificación de salud completa
	@echo "💚 Verificando salud completa del sistema..."
	@echo "🔍 Verificando servicios principales:"
	$(MAKE) test-services
	@echo "\n🔍 Verificando endpoints específicos:"
	$(MAKE) test-endpoints-product
	@echo "\n🔍 Verificando base de datos:"
	$(MAKE) troubleshoot-db
	@echo "\n📊 Estado de recursos:"
	$(MAKE) monitor-resources

## 🔄 Scripts de automatización
auto-restart: ## 🔄 Reinicio automático con verificaciones
	@echo "🔄 Ejecutando reinicio automático..."
	$(MAKE) down-local
	@sleep 5
	$(MAKE) clean-volumes
	$(MAKE) up-local
	@sleep 30
	$(MAKE) health-check-all
	@echo "✅ Reinicio automático completado"

daily-maintenance: ## 📅 Mantenimiento diario automatizado
	@echo "📅 Ejecutando mantenimiento diario..."
	$(MAKE) backup-volumes
	$(MAKE) clean-images
	$(MAKE) collect-metrics
	$(MAKE) generate-reports
	@echo "✅ Mantenimiento diario completado"

## 📱 Comandos de utilidad móvil/rápida
quick-start: up-db dev-product ## ⚡ Inicio rápido para desarrollo
quick-test: test-product health-product ## ⚡ Test rápido completo
quick-deploy: build-jars up-dev test-services ## ⚡ Despliegue rápido
quick-stop: stop-product-local down-local ## ⚡ Parada rápida completa

## 📖 Ayuda mejorada y documentación
help-product: ## 📖 Mostrar ayuda específica del product-service
	@echo "🧪 Product Service - Comandos disponibles:"
	@echo "============================================"
	@echo ""
	@echo "🧪 TESTING:"
	@echo "  make test-product              - Ejecutar todos los tests"
	@echo "  make test-product-local        - Ejecutar tests con perfil local"
	@echo "  make test-product-unit         - Ejecutar solo tests unitarios"
	@echo "  make test-product-report       - Abrir reporte de tests en navegador"
	@echo "  make test-integration-product  - Tests de integración con BD real"
	@echo ""
	@echo "🚀 DESARROLLO:"
	@echo "  make run-product-local         - Ejecutar con perfil local (localhost:5432)"
	@echo "  make run-product-dev           - Ejecutar con perfil dev (product-db:5432)"
	@echo "  make run-product-profile PROFILE=X - Ejecutar con perfil específico"
	@echo "  make dev-product               - Alias para run-product-local"
	@echo "  make stop-product-local        - Detener aplicación local"
	@echo ""
	@echo "🔨 BUILD:"
	@echo "  make build-product             - Construir para producción"
	@echo "  make clean-product             - Limpiar archivos temporales"
	@echo "  make rebuild-product-local-dev - Limpiar y construir"
	@echo ""
	@echo "✅ VALIDACIÓN:"
	@echo "  make check-product             - Verificar código (build + test)"
	@echo "  make validate-pagination-feature - Validar paginación con filtros"
	@echo "  make lint-product              - Verificar estilo de código"
	@echo ""
	@echo "🛠️  UTILIDADES:"
	@echo "  make health-product            - Verificar health de la aplicación"
	@echo "  make test-endpoints-product    - Probar endpoints principales"
	@echo "  make test-pagination-endpoints - Probar endpoints de paginación con filtros"
	@echo "  make info-product              - Información del proyecto"

help-workflows: ## 📖 Ayuda para workflows automatizados
	@echo "🔄 Workflows Automatizados:"
	@echo "=========================="
	@echo ""
	@echo "👨‍💻 DESARROLLO:"
	@echo "  make dev-setup                 - Configuración inicial para desarrolladores"
	@echo "  make workflow-dev              - Workflow completo de desarrollo"
	@echo "  make quick-start               - Inicio rápido para desarrollo"
	@echo ""
	@echo "🧪 QA/TESTING:"
	@echo "  make qa-setup                  - Configuración para QA y testing"
	@echo "  make workflow-ci               - Workflow de integración continua"
	@echo "  make quick-test                - Test rápido completo"
	@echo ""
	@echo "🚀 PRODUCCIÓN:"
	@echo "  make devops-setup              - Configuración para DevOps"
	@echo "  make workflow-production       - Workflow de producción"
	@echo "  make quick-deploy              - Despliegue rápido"
	@echo ""
	@echo "🛠️  MANTENIMIENTO:"
	@echo "  make daily-maintenance         - Mantenimiento diario automatizado"
	@echo "  make auto-restart              - Reinicio automático con verificaciones"
	@echo "  make maintenance-full          - Mantenimiento completo del sistema"

help-troubleshooting: ## 📖 Ayuda para solución de problemas
	@echo "🆘 Solución de Problemas:"
	@echo "========================"
	@echo ""
	@echo "🔍 DIAGNÓSTICO:"
	@echo "  make troubleshoot-db           - Diagnóstico de problemas de base de datos"
	@echo "  make troubleshoot-network      - Diagnóstico de problemas de red"
	@echo "  make troubleshoot-services     - Diagnóstico de problemas de servicios"
	@echo "  make health-check-all          - Verificación de salud completa"
	@echo ""
	@echo "🔧 SOLUCIONES:"
	@echo "  make fix-common-issues         - Solucionar problemas comunes"
	@echo "  make auto-restart              - Reinicio automático con verificaciones"
	@echo "  make reset-local               - Reiniciar completamente el entorno local"
	@echo ""
	@echo "📊 MONITOREO:"
	@echo "  make monitor-resources         - Monitorear recursos en tiempo real"
	@echo "  make collect-metrics           - Recopilar métricas del sistema"
	@echo "  make system-info-full          - Información completa del sistema"

help: ## 📖 Mostrar esta ayuda
	@echo "🦭 Makefile para Podman - Sistema de Microservicios"
	@echo "=================================================="
	@echo ""
	@echo "⚡ COMANDOS RÁPIDOS:"
	@echo "  make quick-start               - Inicio rápido para desarrollo"
	@echo "  make quick-test                - Test rápido completo"
	@echo "  make quick-deploy              - Despliegue rápido"
	@echo "  make quick-stop                - Parada rápida completa"
	@echo ""
	@echo "📖 AYUDA DETALLADA:"
	@echo "  make help-product              - Ayuda específica del Product Service"
	@echo "  make help-workflows            - Ayuda para workflows automatizados"
	@echo "  make help-troubleshooting      - Ayuda para solución de problemas"
	@echo ""
	@echo "🔍 CATEGORÍAS PRINCIPALES:"
	@echo ""
	@grep -E '^[a-zA-Z_-]+:.*?## .*$' $(MAKEFILE_LIST) | \
		awk 'BEGIN {FS = ":.*?## "}; {printf "  %-25s %s\n", $1, $2}' | \
		grep -E "(🔽|🔁|♻️|🔍|📊|🧪|🚀|🔨|✅|🛠️|🎯|📊|🔄|🆘|📱|🔐|📋)" | \
		head -20
	@echo ""
	@echo "💡 Ejecuta 'make help-[categoria]' para ayuda específica"

help-supplier: ## 📖 Comandos de supplier-service
	@echo "supplier-service:"
	@echo "  make up-supplier              - Levantar perfil supplier (docker-compose)"
	@echo "  make rebuild-supplier-local   - Rebuild + up (local)"
	@echo "  make run-supplier-local       - Ejecutar sin contenedor (perfil local)"
	@echo "  make test-supplier            - Ejecutar tests"
	@echo "  make health-supplier          - Ver health"

help-purchase: ## 📖 Comandos de purchase-service
	@echo "purchase-service:"
	@echo "  make up-purchase              - Levantar perfil purchase (docker-compose)"
	@echo "  make rebuild-purchase-local   - Rebuild + up (local)"
	@echo "  make run-purchase-local       - Ejecutar sin contenedor (perfil local)"
	@echo "  make test-purchase            - Ejecutar tests"
	@echo "  make health-purchase          - Ver health"

fix-container-orphans: ## 🛠️ Limpia contenedores huérfanos del proyecto
	@echo "🧹 Eliminando contenedores huérfanos (compose project: $(COMPOSE_PROJECT_NAME))..."
	$(COMPOSE_LOCAL)  down -v --remove-orphans 2>/dev/null || true
	$(COMPOSE_DEV)    down -v --remove-orphans 2>/dev/null || true
	$(COMPOSE_PROD)   down -v --remove-orphans 2>/dev/null || true
	@echo "🗑️  Pruning de recursos no usados..."
	podman rm -af || true
	podman system prune -af --volumes || true
	@echo "✅ Limpieza de huérfanos completada"

help-all: ## 📖 Mostrar todos los comandos disponibles en el Makefile
	@echo "🦭 Makefile - Lista completa de comandos"
	@echo "========================================"
	@grep -E '^[a-zA-Z0-9_.-]+:.*## ' $(MAKEFILE_LIST) | \
	  awk -F ':.*## ' '{ printf "  %-35s %s\n", $$1, $$2 }' | sort

up-supplier: ## 🚀 Levanta sólo la BD y el supplier-service
	$(COMPOSE_LOCAL) --profile supplier up -d --build supplier-db supplier-service

logs-supplier: ## 📜 Logs de supplier-service
	$(COMPOSE_LOCAL) --profile supplier logs -f supplier-service

status-supplier: ## 📊 Estado de servicios del perfil supplier
	$(COMPOSE_LOCAL) --profile supplier ps

up-ms: fix-container-orphans ## 🚀 Infra + supplier + purchase (sin product)
	@echo "🧹 Copiando variables de entorno..."
	cp $(ENV_FILE_LOCAL) .env.override
	@echo "🔨 Construyendo JARs necesarios..."
	cd .. && ./gradlew :config-service:bootJar :discovery-service:bootJar :supplier-service:bootJar :purchase-service:bootJar
	@echo "🦭 Levantando contenedores (infra, supplier, purchase)..."
	$(COMPOSE_LOCAL) --profile infra --profile supplier --profile purchase up -d --build

# --- target con check corregido ---
up-local-supplier: fix-container-orphans
	@echo "🧹 Copiando variables de entorno..."
	cp "$(ENV_FILE_LOCAL)" "$(CURDIR)/.env.override"

	@echo "🔎 Verificando que supplier-db y supplier-service existan con perfiles infra,supplier..."
	@SERVICES="$$(COMPOSE_PROFILES=infra,supplier podman-compose $(COMPOSE_F) --env-file "$(ENV_FILE_LOCAL)" \
	  --profile infra --profile supplier config --services 2>/dev/null || true)"; \
	echo "$$SERVICES" | grep -E '^(supplier-db|supplier-service)$$' >/dev/null || { \
	  echo "❌ No se encuentran supplier-db/supplier-service con los perfiles infra,supplier"; \
	  echo "ℹ️  Debug (config --services):"; echo "$$SERVICES"; \
	  echo "💡 Prueba manual:"; \
	  echo "   COMPOSE_PROFILES=infra,supplier podman-compose $(COMPOSE_F) --env-file $(ENV_FILE_LOCAL) --profile infra --profile supplier config --services"; \
	  exit 1; \
	}

	@echo "🔨 Construyendo jars necesarios..."
	cd "$(CURDIR)/.." && ./gradlew \
		:config-service:bootJar \
		:discovery-service:bootJar \
		:supplier-service:bootJar

	@echo "🦭 Levantando discovery, config, supplier-db y supplier-service..."
	COMPOSE_PROFILES=infra,supplier \
	podman-compose $(COMPOSE_F) --env-file "$(ENV_FILE_LOCAL)" \
	  --profile infra --profile supplier \
	  up -d --build discovery-service config-service supplier-db supplier-service


.PHONY: help help-product help-workflows help-troubleshooting \
        up-local down-local up-dev up-prod up-db \
        build-images build-jars \
        rebuild-api-gateway-local rebuild-config-service-local \
        rebuild-discovery-local rebuild-product-local rebuild-product-dev \
        clean-volumes clean-images clean-podman stop-all reset-db reset-local \
        logs-local logs-dev logs-api-gateway logs-discovery logs-config logs-product logs-db check-db-init \
        status podman-status test-services build-step-by-step \
        pods-list pods-stats images-list system-info podman-start podman-machine-list podman-view-all podman-remove-all \
        test-product test-product-unit test-product-controller test-product-app test-product-health test-product-report \
        test-product-local run-product-local run-product-dev run-product-profile dev-product stop-product-local \
        build-product build-product-no-tests clean-product rebuild-product-local-dev check-product lint-product \
        deps-product deps-product-update health-product test-endpoints-product test-pagination-endpoints info-product \
        quick-test-product quick-build-product coverage-product dev-full-stack test-integration-product validate-pagination-feature \
        monitor-resources debug-network debug-volumes debug-containers maintenance-full backup-volumes restore-volumes \
        deploy-staging deploy-production pre-commit-checks benchmark-endpoints load-test-basic security-scan validate-configs \
        generate-reports system-info-full workflow-dev workflow-ci workflow-production troubleshoot-db troubleshoot-network troubleshoot-services \
        fix-common-issues dev-setup qa-setup devops-setup collect-metrics health-check-all auto-restart daily-maintenance \
        quick-start quick-test quick-deploy quick-stop fix-orphans fix-container-orphans \
        up-% down-% logs-%