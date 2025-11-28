#------------------------------------------------------------------------
# 🦭 Makefile para Podman - Sistema de Microservicios InventoryMS
#------------------------------------------------------------------------

#------------------------------------------------------------------------
# 📦 Variables de entorno y configuración
#------------------------------------------------------------------------
CURDIR := $(abspath $(dir $(lastword $(MAKEFILE_LIST))))
ROOT_DIR := $(abspath $(CURDIR)/..)
GRADLEW := $(ROOT_DIR)/gradlew

# Variables de utilidad para salida limpia
Q := @
ifndef VERBOSE
.SILENT:
endif

# Archivos de entorno
ENV_FILE_LOCAL ?= $(CURDIR)/.env.local
ENV_FILE_LOCAL_SUPPLIER ?= $(CURDIR)/.env.local-supplier
ENV_FILE_LOCAL_SUPPLIER_TAX ?= $(CURDIR)/.env.local-supplier-tax
ENV_FILE_DEV ?= $(CURDIR)/.env.dev
ENV_FILE_PROD ?= $(CURDIR)/.env.prod

# Resolución de archivos de entorno
ENV_SUPPLIER_FALLBACK ?= $(ENV_FILE_LOCAL)
ENV_FILE_LOCAL_SUPPLIER_RESOLVED := $(if $(wildcard $(ENV_FILE_LOCAL_SUPPLIER)),$(ENV_FILE_LOCAL_SUPPLIER),$(ENV_SUPPLIER_FALLBACK))
ENV_FILE ?= $(ENV_FILE_LOCAL_SUPPLIER_TAX)
ENV_FILE_RESOLVED := $(if $(wildcard $(ENV_FILE)),$(ENV_FILE),)

# Configuración de Compose
COMPOSE_FILE ?= docker-compose-podman.yml
COMPOSE_PROJECT_NAME ?= inventoryms
COMPOSE_PROVIDER ?= podman
export COMPOSE_DOCKER_CLI_BUILD ?= 0

# Puertos de servicios
PRODUCT_PORT ?= 9090
SUPPLIER_PORT ?= 9091
PURCHASE_PORT ?= 9095
TAX_PORT ?= 9092
GATEWAY_PORT ?= 8090
DISCOVERY_PORT ?= 8761
CONFIG_PORT ?= 7777

# Perfiles y servicios
PROFILES_ALL := infra gateway product supplier tax purchase
SERVICES_ALL := discovery-service config-service api-gateway product-service supplier-service tax-service purchase-service

# Comandos base
RUN := COMPOSE_PROVIDER=$(COMPOSE_PROVIDER) COMPOSE_PROJECT_NAME=$(COMPOSE_PROJECT_NAME)
COMPOSE_BASE := $(RUN) podman compose -f $(COMPOSE_FILE)

# Comandos específicos por entorno
COMPOSE_LOCAL := $(COMPOSE_BASE) --env-file $(ENV_FILE_LOCAL)
COMPOSE_LOCAL_SUPPLIER := $(COMPOSE_BASE) --env-file $(ENV_FILE_LOCAL_SUPPLIER_RESOLVED)
COMPOSE_DEV := $(COMPOSE_BASE) --env-file $(ENV_FILE_DEV)
COMPOSE_PROD := $(COMPOSE_BASE) --env-file $(ENV_FILE_PROD)
#------------------------------------------------------------------------

# 💡 MACROS DE UTILIDAD
define check_health
	curl -f --max-time 5 $(1) >/dev/null 2>&1 && echo "✅ $(2)" || echo "❌ $(2)"
endef

#🎯 COMANDOS PRINCIPALES - FLUJOS DE TRABAJO ESENCIALES
#------------------------------------------------------------------------

#🚀 INICIAR SERVICIOS
start: up-local ## 🚀 Iniciar servicios base (Infra + Product + Gateway)
start-all: up-local-all-ordered ## 🚀 Iniciar TODOS los servicios
start-parallel: up-local-all ## 🚀 Iniciar todos los servicios en paralelo

#🛑 DETENER SERVICIOS
stop: down-all ## 🛑 Detener todos los servicios

#🔄 REINICIAR SERVICIOS
restart: stop start ## 🔄 Reiniciar servicios base
restart-all: stop start-all ## 🔄 Reiniciar todos los servicios

#🛠️ DESARROLLO
dev: up-local ## 🧪 Entorno de desarrollo base
dev-full: up-local-all-ordered ## 🧪 Entorno de desarrollo completo
dev-supplier: up-local-supplier ## 🧪 Entorno de desarrollo con Supplier
dev-tax: up-local-with-tax ## 🧪 Entorno de desarrollo con Tax

#🏗️ CONSTRUCCIÓN
build-all: build-jars-all ## 🔨 Construir todos los servicios
build-jars: ## 🔨 Construir JARs base
	$(Q) cd $(ROOT_DIR) && $(GRADLEW) :config-service:bootJar :discovery-service:bootJar :api-gateway:bootJar :product-service:bootJar
build-jars-all: ## 🔨 Construir TODOS los JARs
	@echo "🔨 Construyendo todos los JARs..."
	$(Q) cd $(ROOT_DIR) && $(GRADLEW) :config-service:bootJar :discovery-service:bootJar :api-gateway:bootJar :product-service:bootJar :supplier-service:bootJar :tax-service:bootJar #:purchase-service:bootJar

#🧪 TESTING
test-all: ## 🧪 Ejecutar todos los tests
	$(Q) cd $(ROOT_DIR) && $(GRADLEW) test
test-quick: test-product test-supplier test-tax ## 🧪 Tests rápidos de servicios principales

#📊 MONITOREO
status: podman-ready ## 📊 Estado de todos los servicios
	$(COMPOSE_LOCAL) ps
status-all: status ## 📊 Alias para status
logs: podman-ready ## 📜 Logs de todos los servicios
	$(COMPOSE_LOCAL) logs -f
health: health-check-extended ## 💚 Verificación de salud completa
#------------------------------------------------------------------------

#🔧 COMANDOS TÉCNICOS - PODMAN Y DOCKER COMPOSE
#------------------------------------------------------------------------
.PHONY: podman-ready
podman-ready: ## 🔍 Verificar que Podman esté listo
	@if ! podman info >/dev/null 2>&1; then \
		echo "⚠️ Podman no responde. Intentando iniciar VM..."; \
		podman machine start >/dev/null 2>&1 || { \
			echo "⚠️ Podman Machine no existe. Inicializando y arrancando..."; \
			podman machine init --now >/dev/null 2>&1; \
		}; \
	fi; \
	podman info >/dev/null 2>&1 || ( echo "❌ No se pudo conectar a Podman. Asegúrate de que la VM esté en marcha."; exit 1 )

#🚀 LEVANTAR SERVICIOS
up-local: podman-ready fix-container-orphans ## 🧪 Infra + Product + Gateway
	@echo "🧪 Levantando: Infra + Product + Gateway"
	$(Q) cp $(ENV_FILE_LOCAL) .env.override
	$(Q) $(MAKE) build-jars
	$(Q) $(COMPOSE_LOCAL) --profile infra --profile product --profile gateway up -d --build --remove-orphans
up-local-all: podman-ready fix-container-orphans
	@echo "🚀 Levantando TODOS los microservicios (paralelo)..."
	$(Q) cp $(ENV_FILE_LOCAL) .env.override
	$(Q) $(MAKE) build-jars-all
	$(Q) $(COMPOSE_LOCAL) --profile infra --profile gateway --profile product --profile supplier --profile tax up -d --build --remove-orphans
	@echo "🔍 Verificando salud de los servicios (espera 30s)..."
	$(Q) sleep 30
	$(Q) $(MAKE) health-check-extended
up-local-all-ordered: podman-ready fix-container-orphans ## 🚀 Stack completo en orden correcto
	@echo "🚀 INICIANDO STACK COMPLETO (ORDENADO)..."
	$(Q) echo "🧹 Limpieza previa..."
	$(Q) $(MAKE) fix-container-orphans
	$(Q) cp $(ENV_FILE_LOCAL) .env.override
	$(Q) $(MAKE) build-jars-all

	@echo "🔨 Construyendo discovery-service..."
	$(Q) podman build --no-cache -f $(ROOT_DIR)/discovery-service/Dockerfile -t discovery-service:latest $(ROOT_DIR)

	@echo "🔧 1/5: Discovery Service..."
	$(Q) $(COMPOSE_LOCAL) --profile infra up -d --build discovery-service
	$(Q) sleep 30

	@echo "🔧 2/5: Config Service..."
	$(Q) $(COMPOSE_LOCAL) --profile infra up -d --build config-service
	$(Q) sleep 30

	# AJUSTE CRÍTICO: Eliminamos los nombres explícitos de los servicios (product-db, etc.)
	# y confiamos en que los perfiles (--profile product, --profile supplier, --profile tax)
	# levantarán las bases de datos definidas en docker-compose-podman.yml.
	@echo "🗄️ 3/5: Bases de datos (Levantando por perfiles)..."
	$(Q) $(COMPOSE_LOCAL) --profile infra --profile product --profile supplier --profile tax up -d
	$(Q) sleep 15

	@echo "🛠️ 4/5: Microservicios (Product, Supplier, Tax)..."
	$(Q) $(COMPOSE_LOCAL) --profile infra --profile product --profile supplier --profile tax up -d --build product-service supplier-service tax-service
	$(Q) sleep 45

	@echo "🌐 5/5: API Gateway..."
	$(Q) $(COMPOSE_LOCAL) --profile infra --profile gateway up -d --build api-gateway

	@echo "🔍 Verificación final (espera 15s)..."
	$(Q) sleep 15
	$(Q) $(MAKE) health-check-extended
	@echo "🎉 STACK COMPLETO LEVANTADO!"
up-local-supplier: podman-ready fix-container-orphans ## 🧪 Infra + Supplier
	@echo "🧹 Configurando entorno supplier..."
	$(Q) cp "$(ENV_FILE_LOCAL_SUPPLIER_RESOLVED)" "$(CURDIR)/.env.override"
	$(Q) cd "$(ROOT_DIR)" && $(GRADLEW) :config-service:bootJar :discovery-service:bootJar :supplier-service:bootJar
	@echo "🦭 Levantando supplier..."
	$(Q) $(COMPOSE_LOCAL_SUPPLIER) --profile infra --profile supplier up -d --build --remove-orphans
up-local-with-tax: podman-ready fix-container-orphans ## 🧪 Infra + Product + Gateway + Tax
	@echo "🧹 Configurando entorno con tax..."
	$(Q) cp $(ENV_FILE_LOCAL) .env.override
	$(Q) cd $(ROOT_DIR) && $(GRADLEW) :config-service:bootJar :discovery-service:bootJar :product-service:bootJar :api-gateway:bootJar :tax-service:bootJar
	@echo "🦭 Levantando servicios con tax..."
	$(Q) $(COMPOSE_LOCAL) --profile infra --profile product --profile gateway --profile tax up -d --build --remove-orphans

#🔻 DETENER SERVICIOS
down-local: podman-ready ## 🔻 Detener servicios locales
	$(COMPOSE_LOCAL) --profile infra --profile product --profile gateway down -v --remove-orphans
down-all: podman-ready ## 🔻 Detener TODOS los servicios
	@echo "🧹 Deteniendo todos los servicios..."
	$(COMPOSE_LOCAL) $(foreach p,$(PROFILES_ALL),--profile $(p)) down -v --remove-orphans

#🔍 MONITOREO Y LOGS
logs-%: podman-ready ## 📜 Logs por perfil
	$(COMPOSE_LOCAL) --profile $* logs -f

health-check-extended: podman-ready ## 💚 Verificación de salud extendida
	@echo "🩺 Verificación de salud EXTENDIDA..."
	@if ! command -v curl >/dev/null; then echo "❌ Error: 'curl' no está instalado. No se puede verificar la salud de los microservicios."; fi

	@echo "🔍 Infraestructura (Health Actuator):"
	$(call check_health, http://localhost:$(DISCOVERY_PORT)/actuator/health, Discovery)
	$(call check_health, http://localhost:$(CONFIG_PORT)/actuator/health, Config)

	@echo "🔍 Bases de datos (pg_isready):"
	$(Q) podman exec $(COMPOSE_PROJECT_NAME)-product-db-1 pg_isready -U product >/dev/null 2>&1 && echo "✅ Product DB" || echo "❌ Product DB"
	$(Q) podman exec $(COMPOSE_PROJECT_NAME)-supplier-db-1 pg_isready -U supplier >/dev/null 2>&1 && echo "✅ Supplier DB" || echo "❌ Supplier DB"
	$(Q) podman exec $(COMPOSE_PROJECT_NAME)-tax-db-1 pg_isready -U tax >/dev/null 2>&1 && echo "✅ Tax DB" || echo "❌ Tax DB"

	@echo "🔍 Microservicios (Health Actuator):"
	$(call check_health, http://localhost:$(PRODUCT_PORT)/actuator/health, Product)
	$(call check_health, http://localhost:$(SUPPLIER_PORT)/actuator/health, Supplier)
	$(call check_health, http://localhost:$(TAX_PORT)/actuator/health, Tax)
	$(call check_health, http://localhost:$(GATEWAY_PORT)/actuator/health, Gateway)
#------------------------------------------------------------------------

#🛠️ MANTENIMIENTO Y LIMPIEZA
#------------------------------------------------------------------------
fix-container-orphans: ## 🧹 Limpiar contenedores huérfanos
	@echo "🧹 Limpiando contenedores huérfanos..."
	$(COMPOSE_LOCAL) down --remove-orphans 2>/dev/null || true
	$(Q) podman system prune -f 2>/dev/null || true
clean-all: podman-ready ## 🧨 Limpieza completa
	@echo "🧨 Limpieza completa..."
	$(Q) $(MAKE) down-all
	$(Q) $(MAKE) clean-podman-prune
	$(Q) $(MAKE) clean-gradle
clean-podman-prune: podman-ready ## 🧹 Limpiar recursos de Podman (imágenes, volúmenes, cache)
	$(Q) podman system prune -a -f --volumes
clean-gradle: ## 🧹 Limpiar caché de Gradle
	$(Q) cd $(ROOT_DIR) && $(GRADLEW) clean
reset-local: clean-all up-local-all-ordered ## 🔄 Reinicio completo del entorno local
#------------------------------------------------------------------------

#🧪 SERVICIOS INDIVIDUALES
#------------------------------------------------------------------------

#🏷️ PRODUCT SERVICE
test-product: ## 🧪 Tests de Product Service
	$(Q) cd $(ROOT_DIR)/product-service && $(GRADLEW) test
run-product-local: ## 🚀 Ejecutar Product Service localmente
	@echo "🚀 Product Service en http://localhost:$(PRODUCT_PORT)"
	$(Q) cd $(ROOT_DIR)/product-service && $(GRADLEW) bootRun --args='--spring.profiles.active=local'
rebuild-product-local: podman-ready ## 🔁 Reconstruir Product Service
	$(Q) cp $(ENV_FILE_LOCAL) .env.override
	$(Q) cd $(ROOT_DIR) && $(GRADLEW) :product-service:bootJar
	$(Q) $(COMPOSE_LOCAL) build product-service
	$(Q) $(COMPOSE_LOCAL) up -d product-service

#🏷️ SUPPLIER SERVICE
test-supplier: ## 🧪 Tests de Supplier Service
	$(Q) cd $(ROOT_DIR)/supplier-service && $(GRADLEW) test
run-supplier-local: ## 🚀 Ejecutar Supplier Service localmente
	@echo "🚀 Supplier Service en http://localhost:$(SUPPLIER_PORT)"
	$(Q) cd $(ROOT_DIR)/supplier-service && $(GRADLEW) bootRun --args='--spring.profiles.active=local'
rebuild-supplier-local: podman-ready ## 🔁 Reconstruir Supplier Service
	$(Q) cp $(ENV_FILE_LOCAL) .env.override
	$(Q) cd $(ROOT_DIR) && $(GRADLEW) :supplier-service:bootJar
	$(Q) $(COMPOSE_LOCAL) build supplier-service
	$(Q) $(COMPOSE_LOCAL) up -d supplier-service

#🏷️ TAX SERVICE
test-tax: ## 🧪 Tests de Tax Service
	$(Q) cd $(ROOT_DIR)/tax-service && $(GRADLEW) test
run-tax-local: ## 🚀 Ejecutar Tax Service localmente
	@echo "🚀 Tax Service en http://localhost:$(TAX_PORT)"
	$(Q) cd $(ROOT_DIR)/tax-service && $(GRADLEW) bootRun --args='--spring.profiles.active=local'
rebuild-tax-local: podman-ready ## 🔁 Reconstruir Tax Service
	$(Q) cp $(ENV_FILE_LOCAL) .env.override
	$(Q) cd $(ROOT_DIR) && $(GRADLEW) :tax-service:bootJar
	$(Q) $(COMPOSE_LOCAL) build tax-service
	$(Q) $(COMPOSE_LOCAL) up -d tax-service

#🏷️ PURCHASE SERVICE
test-purchase: ## 🧪 Tests de Purchase Service
	$(Q) cd $(ROOT_DIR)/purchase-service && $(GRADLEW) test
run-purchase-local: ## 🚀 Ejecutar Purchase Service localmente
	@echo "🚀 Purchase Service en http://localhost:$(PURCHASE_PORT)"
	$(Q) cd $(ROOT_DIR)/purchase-service && $(GRADLEW) bootRun --args='--spring.profiles.active=local'
#------------------------------------------------------------------------

#🗃️ BASE DE DATOS
#------------------------------------------------------------------------
up-db: podman-ready ## 🗃️ Levantar solo bases de datos
	@echo "🗄️ Levantando bases de datos (solo por perfiles)..."
	# AJUSTE: Eliminamos la lista explícita de servicios
	$(Q) $(COMPOSE_LOCAL) --profile infra --profile product --profile supplier --profile tax up -d
reset-db: podman-ready ## 🗃️ Reiniciar base de datos principal
	@echo "🔄 Reiniciando product-db..."
	$(Q) $(COMPOSE_LOCAL) down -v product-db
	$(Q) $(COMPOSE_LOCAL) up -d product-db
tax-db-reset: podman-ready ## 🗃️ Reiniciar base de datos de tax
	@echo "🔄 Reiniciando tax-db y tax-service..."
	$(Q) $(COMPOSE_LOCAL) --profile tax down -v tax-db
	$(Q) cd $(ROOT_DIR) && $(GRADLEW) :tax-service:bootJar
	$(Q) $(COMPOSE_LOCAL) --profile tax up -d tax-db tax-service
#------------------------------------------------------------------------

#🆘 DIAGNÓSTICO AVANZADO Y SOLUCIÓN DE PROBLEMAS
#------------------------------------------------------------------------

#🔍 DIAGNÓSTICO ESPECÍFICO PARA DISCOVERY SERVICE
debug-discovery: podman-ready ## 🐛 Debug del Discovery Service
	@echo "🐛 Iniciando diagnóstico del Discovery Service..."
	@echo "📋 Estado del contenedor:"
	$(Q) podman ps -a --filter name=$(COMPOSE_PROJECT_NAME)-discovery-service-1
	@echo ""
	@echo "📜 Últimos logs (100 líneas):"
	$(Q) podman logs $(COMPOSE_PROJECT_NAME)-discovery-service-1 --tail 100 2>/dev/null || echo "❌ No se pueden obtener logs del discovery-service"
	@echo ""
	@echo "🔍 Verificando recursos de Podman:"
	$(Q) podman system info | grep -E "(CPUs|Memory)" | head -2
fix-discovery: podman-ready ## 🔧 Solucionar problemas del Discovery Service
	@echo "🔧 Aplicando correcciones para Discovery Service..."
	@echo "1. Deteniendo contenedor..."
	$(Q) podman stop $(COMPOSE_PROJECT_NAME)-discovery-service-1 2>/dev/null || true
	$(Q) podman rm $(COMPOSE_PROJECT_NAME)-discovery-service-1 2>/dev/null || true
	@echo "2. Reconstruyendo imagen (desde la raíz)..."
	$(Q) cd $(ROOT_DIR) && $(GRADLEW) :discovery-service:bootJar
	$(Q) podman build --no-cache -f $(ROOT_DIR)/discovery-service/Dockerfile -t discovery-service:latest $(ROOT_DIR)
	@echo "3. Levantando solo discovery service..."
	$(Q) $(COMPOSE_LOCAL) --profile infra up -d discovery-service
	$(Q) sleep 10
	@echo "5. Verificando logs..."
	$(Q) podman logs $(COMPOSE_PROJECT_NAME)-discovery-service-1 --tail 20
check-discovery-health: ## 🔍 Verificar salud específica del Discovery
	@echo "🔍 Verificando salud de Discovery Service..."
	@if curl -s http://localhost:$(DISCOVERY_PORT)/actuator/health >/dev/null; then \
		echo "✅ Discovery Service está saludable"; \
	else \
		echo "❌ Discovery Service no responde"; \
		echo "📜 Últimos logs:"; \
		podman logs $(COMPOSE_PROJECT_NAME)-discovery-service-1 --tail 30 2>/dev/null || echo "No hay logs disponibles"; \
	fi

#📊 DIAGNÓSTICO COMPLETO DEL SISTEMA
diagnose: podman-ready ## 🔍 Diagnóstico completo del sistema
	@echo "🔍 Ejecutando diagnóstico completo del sistema..."
	@echo ""
	@echo "📊 ESTADO DE CONTENEDORES:"
	$(Q) $(COMPOSE_LOCAL) ps
	@echo ""
	@echo "💾 RECURSOS DEL SISTEMA:"
	$(Q) podman stats --no-stream --format "table {{.Name}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.Status}}" 2>/dev/null || echo "⚠️ No se pudieron obtener estadísticas"
	@echo ""
	@echo "🌐 VERIFICACIÓN DE RED:"
	$(Q) podman network ls
	@echo ""
	@echo "📦 VERIFICACIÓN DE IMÁGENES:"
	$(Q) podman images --filter reference="inventory" --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}"
	@echo ""
	@echo "🩺 VERIFICACIÓN DE SALUD:"
	$(Q) $(MAKE) health-check-extended
check-resources: ## 📊 Verificar recursos del sistema
	@echo "📊 Verificando recursos de Podman..."
	@echo "🖥️ CPUs y Memoria:"
	$(Q) podman system info | grep -E "(CPUs|Memory|Disk)" | head -3
	@echo ""
	@echo "💾 Uso de disco:"
	$(Q) podman system df
	@echo ""
	@echo "📈 Contenedores activos:"
	$(Q) podman ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
check-ports: ## 🔍 Verificar puertos en uso
	@echo "🔍 Verificando puertos en uso (solo en el host):"
	@if ! command -v lsof >/dev/null; then echo "⚠️ Advertencia: 'lsof' no está instalado. No se puede verificar la disponibilidad de puertos del host."; exit 1; fi
	@for port in $(DISCOVERY_PORT) $(CONFIG_PORT) $(GATEWAY_PORT) $(PRODUCT_PORT) $(SUPPLIER_PORT) $(TAX_PORT) $(PURCHASE_PORT) 5432; do \
		if lsof -Pi :$$port -sTCP:LISTEN -t >/dev/null; then \
			echo "🔴 Puerto $$port: OCUPADO"; \
		else \
			echo "🟢 Puerto $$port: LIBRE"; \
		fi; \
	done

#🐛 HERRAMIENTAS DE DEBUGGING
service-logs: podman-ready ## 📜 Ver logs de todos los servicios recientes
	@echo "📜 Mostrando últimos 50 líneas de logs de cada servicio activo:"
	@for service in $$( $(COMPOSE_LOCAL) ps --services ); do \
		echo ""; \
		echo "=== $$service ==="; \
		podman logs $(COMPOSE_PROJECT_NAME)-$$service-1 --tail 50 2>/dev/null | tail -20 || echo "No hay logs disponibles"; \
	done
check-container-status: ## 🔍 Ver estado detallado de contenedores
	@echo "🔍 Estado detallado de contenedores:"
	$(Q) podman ps -a --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}\t{{.Image}}"

#🚑 COMANDOS DE EMERGENCIA
emergency-clean: ## 🚑 Limpieza de emergencia (cuando nada funciona)
	@echo "🚑 EJECUTANDO LIMPIEZA DE EMERGENCIA..."
	@echo "⛔️ ¡ADVERTENCIA! Esto eliminará todos los contenedores, imágenes y volúmenes!"
	@read -p "¿Estás seguro? (solo escribe 'SI' para continuar): " confirm; \
	if [ "$$confirm" = "SI" ]; then \
		echo "🧹 Eliminando todos los contenedores..."; \
		podman rm -f $$(podman ps -aq) 2>/dev/null || true; \
		echo "🧹 Eliminando todas las imágenes..."; \
		podman rmi -f $$(podman images -aq) 2>/dev/null || true; \
		echo "🧹 Eliminando todos los volúmenes..."; \
		podman volume rm -f $$(podman volume ls -q) 2>/dev/null || true; \
		echo "🧹 Limpiando sistema..."; \
		podman system prune -a -f --volumes; \
		echo "✅ Limpieza de emergencia completada"; \
	else \
		echo "❌ Limpieza cancelada"; \
	fi
restart-podman-vm: ## 🔄 Reiniciar la máquina virtual de Podman
	@echo "🔄 Reiniciando Podman Machine..."
	$(Q) podman machine stop
	$(Q) sleep 5
	$(Q) podman machine start
	@echo "✅ Podman Machine reiniciada"
sync-podman-desktop: ## 🍎 Sincronizar con Podman Desktop (macOS)
	@echo "🔄 Sincronizando con Podman Desktop..."
	$(Q) podman system connection default podman-machine-default
	@echo "✅ Podman Desktop sincronizado"
	@echo "💡 Si no ves los contenedores, reinicia Podman Desktop completamente"
fix-discovery-chmod: ## 🛠️ Solucionar específicamente el problema de chmod en discovery
	@echo "🛠️ Aplicando fix específico para discovery service..."
	# 1. Dar permisos en el host
	$(Q) chmod +x $(ROOT_DIR)/discovery-service/entrypoint.sh $(ROOT_DIR)/discovery-service/wait-for-it.sh
	# 2. Reconstruir JAR
	$(Q) cd $(ROOT_DIR) && $(GRADLEW) :discovery-service:bootJar
	# 3. Reconstruir imagen sin cache (forzando la copia de los scripts)
	$(Q) podman rmi discovery-service:latest 2>/dev/null || true
	$(Q) podman build --no-cache -f $(ROOT_DIR)/discovery-service/Dockerfile -t discovery-service:latest $(ROOT_DIR)
	# 4. Levantar solo discovery
	$(Q) $(COMPOSE_LOCAL) --profile infra up -d discovery-service
	@echo "✅ Fix aplicado, verificando..."
	$(Q) sleep 10
	$(Q) podman logs $(COMPOSE_PROJECT_NAME)-discovery-service-1 --tail 10
diagnose-chmod: ## 🔍 Diagnóstico específico de problemas chmod
	@echo "🔍 Diagnóstico de problemas chmod..."
	@echo ""
	@echo "📋 Permisos de scripts en host:"
	@for service in discovery-service config-service api-gateway product-service supplier-service tax-service; do \
		if [ -f "$(ROOT_DIR)/$$service/entrypoint.sh" ]; then \
			echo "$$service/entrypoint.sh: $$(ls -l $(ROOT_DIR)/$$service/entrypoint.sh | cut -d' ' -f1)"; \
		else \
			echo "❌ $$service/entrypoint.sh: NO EXISTE"; \
		fi; \
		if [ -f "$(ROOT_DIR)/$$service/wait-for-it.sh" ]; then \
			echo "$$service/wait-for-it.sh: $$(ls -l $(ROOT_DIR)/$$service/wait-for-it.sh | cut -d' ' -f1)"; \
		else \
			echo "❌ $$service/wait-for-it.sh: NO EXISTE"; \
		fi; \
	done
	@echo ""
	@echo "🐳 Estado de contenedores:"
	$(Q) podman ps -a --format "table {{.Names}}\t{{.Status}}\t{{.Image}}"
#------------------------------------------------------------------------

#📖 AYUDA Y DOCUMENTACIÓN
#------------------------------------------------------------------------
help: ## 📖 Mostrar ayuda general
	@echo "🦭 Makefile para Podman - InventoryMS"
	@echo ""
	@echo "🚀 COMANDOS PRINCIPALES:"
	@echo "  make start               - Iniciar servicios base (Infra + Product + Gateway)"
	@echo "  make start-all           - Iniciar todos los servicios (en orden)"
	@echo "  make start-parallel      - Iniciar todos los servicios (en paralelo)"
	@echo "  make stop                - Detener todos los servicios"
	@echo "  make restart             - Reiniciar servicios base"
	@echo "  make restart-all         - Reiniciar todos los servicios"
	@echo "  make dev-full            - Entorno de desarrollo completo (ordenado)"
	@echo ""
	@echo "🛠️ DESARROLLO:"
	@echo "  make build-all           - Construir todos los JARs"
	@echo "  make test-all            - Ejecutar todos los tests"
	@echo "  make status              - Estado de los servicios"
	@echo "  make logs                - Logs de todos los servicios"
	@echo "  make health              - Verificación de salud extendida"
	@echo ""
	@echo "🧹 MANTENIMIENTO:"
	@echo "  make clean-all           - Limpieza completa (detiene, purga podman, limpia gradle)"
	@echo "  make reset-local         - Reinicio completo del entorno local"
	@echo "  make fix-container-orphans - Limpiar contenedores huérfanos"
	@echo ""
	@echo "🔍 DIAGNÓSTICO (TS):"
	@echo "  make diagnose            - Diagnóstico completo del sistema"
	@echo "  make check-ports         - Verificar puertos en uso en el host"
	@echo "  make debug-discovery     - Diagnóstico específico del Discovery Service"
	@echo ""
	@echo "📖 MÁS AYUDA:"
	@echo "  make help-services       - Comandos por servicio"
	@echo "  make help-db             - Comandos de base de datos"
	@echo "  make help-troubleshoot   - Solución de problemas avanzada"

help-services: ## 📖 Ayuda de servicios individuales
	@echo "🧪 SERVICIOS INDIVIDUALES:"
	@echo ""
	@echo "🏷️ Product Service:"
	@echo "  make run-product-local   - Ejecutar localmente (host)"
	@echo "  make test-product        - Ejecutar tests"
	@echo "  make rebuild-product-local - Reconstruir y levantar el contenedor"
	@echo "..."
help-db: ## 📖 Ayuda de bases de datos
	@echo "🗃️ BASES DE DATOS:"
	@echo ""
	@echo "  make up-db               - Levantar solo bases de datos"
	@echo "  make reset-db            - Reiniciar base de datos principal (product-db)"
	@echo "  make tax-db-reset        - Reiniciar base de datos de tax y servicio"
help-troubleshoot: ## 📖 Ayuda de solución de problemas
	@echo "🆘 SOLUCIÓN DE PROBLEMAS ESPECÍFICA:"
	@echo ""
	@echo "🔍 DIAGNÓSTICO:"
	@echo "  make diagnose            - Diagnóstico completo del sistema"
	@echo "  make debug-discovery     - Diagnóstico específico del Discovery Service"
	@echo "  make check-ports         - Verificar puertos en uso"
	@echo "  make service-logs        - Logs recientes de todos los servicios"
	@echo "  make diagnose-chmod      - Diagnóstico de permisos de ejecución (chmod)"
	@echo ""
	@echo "🔧 SOLUCIONES:"
	@echo "  make fix-discovery       - Solucionar problemas del Discovery Service"
	@echo "  make fix-discovery-chmod - Solucionar específicamente problemas de permisos en discovery"
	@echo "  make restart-podman-vm   - Reiniciar la máquina virtual de Podman"
	@echo "  make emergency-clean     - Limpieza completa (¡USAR CON CUIDADO!)"
	@echo ""
	@echo "💡 CONSEJOS:"
	@echo "  • Si un servicio falla al iniciar, usa 'make service-logs' para ver el error."
	@echo "  • Los errores de 'exec format error' suelen ser problemas de permisos. Usa 'make diagnose-chmod' y 'make fix-discovery-chmod'."
.PHONY: help help-services help-db help-troubleshoot start start-all start-parallel \
	stop restart restart-all dev dev-full dev-supplier dev-tax build-all build-jars \
	build-jars-all test-all test-quick status status-all logs health podman-ready \
	up-local up-local-all up-local-all-ordered up-local-supplier up-local-with-tax \
	down-local down-all logs-% health-check-extended fix-container-orphans clean-all \
	clean-podman-prune clean-gradle reset-local test-product run-product-local rebuild-product-local \
	test-supplier run-supplier-local rebuild-supplier-local test-tax run-tax-local rebuild-tax-local \
	test-purchase run-purchase-local up-db reset-db tax-db-reset diagnose check-ports \
	sync-podman-desktop debug-discovery fix-discovery check-discovery-health check-resources \
	service-logs check-container-status emergency-clean restart-podman-vm fix-discovery-chmod diagnose-chmod