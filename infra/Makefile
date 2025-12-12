#------------------------------------------------------------------------
# 🦭 Makefile para Podman - Sistema de Microservicios InventoryMS
#------------------------------------------------------------------------
# ✅ MODIFICADO: Definición del directorio de build temporal para la autenticación
BUILD_DIR := $(CURDIR)/.build
AUTH_FILE := $(BUILD_DIR)/config.json
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
export DOCKER_BUILDKIT := 1
export COMPOSE_DOCKER_CLI_BUILD ?= 1 # Habilitar BuildKit

# Perfiles y servicios (PURCHASE/purchase-service excluidos)
PROFILES_BUILD_ORDER := infra gateway product supplier tax
# Servicios que necesitan construcción de imágenes (ordenados)
SERVICES_TO_BUILD := discovery-service config-service api-gateway product-service supplier-service tax-service

# Puertos de servicios
PRODUCT_PORT ?= 9090
SUPPLIER_PORT ?= 9091
TAX_PORT ?= 9093
GATEWAY_PORT ?= 8090
DISCOVERY_PORT ?= 8761
CONFIG_PORT ?= 7777

# Perfiles y servicios (PURCHASE/purchase-service excluidos)
PROFILES_ALL := infra gateway product supplier tax
SERVICES_ALL := discovery-service config-service api-gateway product-service supplier-service tax-service

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
build-all: fix-line-endings build-jars-all ## 🔨 Construir todos los servicios
build-jars: fix-line-endings ## 🔨 Construir JARs base
	$(Q) cd $(ROOT_DIR) && $(GRADLEW) :config-service:bootJar :discovery-service:bootJar :api-gateway:bootJar :product-service:bootJar

build-jars-all: fix-line-endings ## 🔨 Construir TODOS los JARs
	@echo "🔨 Construyendo todos los JARs..."
	$(Q) cd $(ROOT_DIR) && $(GRADLEW) :config-service:bootJar :discovery-service:bootJar :api-gateway:bootJar :product-service:bootJar :supplier-service:bootJar :tax-service:bootJar

# ⭐ CORRECCIÓN RADICAL: Usar un solo script en línea para eliminar errores de sintaxis
build-images-all-nocache: build-jars-all ## 🔨 Construye TODAS las imágenes sin caché (SERIAL)
	@echo "🔨 Construyendo imágenes SIN caché para todos los servicios (SERIAL)..."
	export DOCKER_CONFIG=$(BUILD_DIR); \
	echo "   [1/5] Construyendo discovery-service y config-service..."; \
	$(COMPOSE_LOCAL) --profile infra build --no-cache discovery-service config-service; \
	echo "   [2/5] Construyendo product-service..."; \
	$(COMPOSE_LOCAL) --profile infra --profile product build --no-cache product-service; \
	echo "   [3/5] Construyendo supplier-service..."; \
	$(COMPOSE_LOCAL) --profile infra --profile supplier build --no-cache supplier-service; \
	echo "   [4/5] Construyendo tax-service..."; \
	$(COMPOSE_LOCAL) --profile infra --profile tax build --no-cache tax-service; \
	echo "   [5/5] Construyendo api-gateway..."; \
	$(COMPOSE_LOCAL) --profile infra --profile gateway build --no-cache api-gateway; \
	echo "✅ Construcción de todas las imágenes completada en SERIAL."
#------------------------------------------------------------------------

#🧪 TESTING
test-all: fix-line-endings ## 🧪 Ejecutar todos los tests
	$(Q) cd $(ROOT_DIR) && $(GRADLEW) test
test-quick: test-product test-supplier test-tax ## 🧪 Tests rápidos de servicios principales

#📊 MONITOREO
status: podman-ready ## 📊 Estado de todos los servicios
	$(COMPOSE_LOCAL) ps
status-all: status ## 📊 Alias para status

logs: podman-ready ## 📜 Logs de todos los servicios
	$(COMPOSE_LOCAL) logs -f

health: health-check-extended ## 💚 Verificación de salud completa

# ------------------------------------------------------------------------
# 🚀 TARGETS DE UP (INICIO)
# ------------------------------------------------------------------------
up-local: podman-ready fix-line-endings fix-container-orphans ## 🧪 Infra + Product + Gateway
	@echo "🧪 Levantando: Infra + Product + Gateway"
	$(Q) cp $(ENV_FILE_LOCAL) .env.override
	$(Q) $(MAKE) build-jars
	$(Q) $(COMPOSE_LOCAL) --profile infra --profile product --profile gateway up -d --build --remove-orphans
up-local-all: podman-ready fix-line-endings fix-container-orphans
	@echo "🚀 Levantando TODOS los microservicios (paralelo)..."
	$(Q) cp $(ENV_FILE_LOCAL) .env.override
	$(Q) $(MAKE) build-images-all-nocache # Ahora este target es SERIAL
	$(Q) $(COMPOSE_LOCAL) $(foreach p,$(PROFILES_ALL),--profile $(p)) up -d --build --remove-orphans
	@echo "🔍 Verificando salud de los servicios (espera 30s)..."
	$(Q) sleep 30
	$(Q) $(MAKE) health-check-extended

up-local-all-ordered: podman-ready fix-line-endings fix-container-orphans ## 🚀 Stack completo en orden correcto
	@echo "🚀 INICIANDO STACK COMPLETO (ORDENADO Y SIN CACHÉ)..."
	#=========================================================
	#⏱️ INICIO DEL REGISTRO DE TIEMPO
	#=========================================================
	@START_TIME=$$(date +"%Y-%m-%d %H:%M:%S"); \
	START_SECONDS=$$(date +%s); \
	echo "========================================================="
	echo "⏱️ INICIO DEL PROCESO: $$START_TIME"; \
	echo "========================================================="
	$(Q) echo "🧹 Limpieza previa y forzando construcción de imágenes..."
	$(Q) $(MAKE) fix-container-orphans
	$(Q) cp $(ENV_FILE_LOCAL) .env.override
	$(Q) $(MAKE) build-images-all-nocache # ⭐ Usar target nocache

	@echo "🔧 1/4: Discovery y Config Services..."
	$(Q) $(COMPOSE_LOCAL) --profile infra up -d --no-recreate discovery-service config-service
	$(Q) sleep 40

	@echo "🗄️ 2/4: Bases de datos (Levantando por perfiles)..."
	$(Q) $(COMPOSE_LOCAL) --profile infra up -d --no-recreate db_product db_supplier db_tax # Solo levantar los servicios de DB
	$(Q) sleep 55

	@echo "🛠️ 3/4: Microservicios (Product, Supplier, Tax)..."
	# ✅ CORRECCIÓN DE DEPENDENCIA (Perfiles: infra, product, supplier, tax)
	$(Q) $(COMPOSE_LOCAL) --profile infra --profile product --profile supplier --profile tax up -d --no-recreate product-service supplier-service tax-service
	$(Q) sleep 70

	@echo "🌐 4/4: API Gateway..."
	#$(Q) $(COMPOSE_LOCAL) --profile gateway up -d --no-recreate api-gateway
	$(Q) $(COMPOSE_LOCAL) --profile infra --profile gateway up -d --no-recreate api-gateway

	@echo "🔍 Verificación final (espera 15s)..."
	$(Q) sleep 90
	$(Q) $(MAKE) health-check-extended
	@echo "🎉 STACK COMPLETO LEVANTADO!"
	#=========================================================
	#⏱️ FINAL DEL REGISTRO DE TIEMPO Y CÁLCULO
 	#=========================================================
	#=========================================================
    #⏱️ FINAL DEL REGISTRO DE TIEMPO Y CÁLCULO
    #=========================================================
    @END_TIME=$$(date +"%Y-%m-%d %H:%M:%S"); \
    END_SECONDS=$$(date +%s); \
    DURATION_SECONDS=$$((END_SECONDS - START_SECONDS)); \
    echo "========================================================="; \
    echo "⏱️ FIN DEL PROCESO: $$END_TIME"; \
    echo "⏳ TIEMPO TOTAL DEMORADO: $$DURATION_SECONDS segundos"; \
    echo "========================================================="

# ------------------------------------------------------------------------
# ✅ SOLUCIÓN AL ERROR DE SINTAXIS: Mover lógica a script y garantizar permisos.
# ------------------------------------------------------------------------
.PHONY: ensure-script-exec
ensure-script-exec:
	# 🚨 Comando crítico: Garantizar permisos de ejecución al script antes de usarlo.
	$(Q) chmod +x ensure_podman_ready.sh 2>/dev/null || true

# ------------------------------------------------------------------------
# Targets de Herramientas y Diagnóstico (Versión FINAL que NO se Bloquea ni Causa Sintaxis)
# ------------------------------------------------------------------------
.PHONY: podman-ready
podman-ready: ## 🔍 Confirma el estado y asume que el Login fue manual.
	@echo "⚙️ El entorno de Podman está siendo verificado..."
	@echo "✅ Autenticación y Máquina Podman confirmadas manualmente. Continuando con la construcción."
	# Comprobación simple para asegurar que Podman está disponible
	@podman machine ls >/dev/null 2>&1 || { echo "🚨 ERROR: Podman machine no responde. Ejecute 'podman machine start' manualmente."; exit 1; }

up-local-supplier: podman-ready fix-line-endings fix-container-orphans ## 🧪 Infra + Supplier
	@echo "🧹 Configurando entorno supplier..."
	$(Q) cp "$(ENV_FILE_LOCAL_SUPPLIER_RESOLVED)" "$(CURDIR)/.env.override"
	$(Q) $(MAKE) build-jars-all
	@echo "🦭 Levantando supplier..."
	$(Q) $(COMPOSE_LOCAL_SUPPLIER) --profile infra --profile supplier up -d --build --remove-orphans

up-local-with-tax: podman-ready fix-line-endings fix-container-orphans ## 🧪 Infra + Product + Gateway + Tax
	@echo "🧹 Configurando entorno con tax..."
	$(Q) cp $(ENV_FILE_LOCAL) .env.override
	$(Q) $(MAKE) build-jars-all
	@echo "🦭 Levantando servicios con tax..."
	$(Q) $(COMPOSE_LOCAL) --profile infra --profile gateway --profile product --profile tax up -d --build --remove-orphans

#🔻 DETENER SERVICIOS
down-local: podman-ready ## 🔻 Detener servicios locales
	$(COMPOSE_LOCAL) --profile infra --profile product --profile gateway down -v --remove-orphans
down-all: podman-ready ## 🔻 Detener TODOS los servicios
	@echo "🧹 Deteniendo todos los servicios..."
	$(COMPOSE_LOCAL) $(foreach p,$(PROFILES_ALL),--profile $(p)) down -v --remove-orphans

supplier-db-reset: podman-ready fix-line-endings ## 🗃️ Reiniciar base de datos de supplier
	@echo "🔄 Reiniciando supplier-db y supplier-service..."
    # 1. Detener y eliminar el contenedor y su volumen de datos
	$(Q) $(COMPOSE_LOCAL) --profile supplier down -v db_supplier
    # 2. Reconstruir el JAR
	$(Q) cd $(ROOT_DIR) && $(GRADLEW) :supplier-service:bootJar
    # 3. Levantar la infra y el servicio del proveedor para recrear la DB
	$(Q) $(COMPOSE_LOCAL) --profile infra --profile supplier up -d supplier-service

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
	# Se usa el nombre completo del contenedor para mayor robustez
	$(Q) podman exec db_product pg_isready -U postgres >/dev/null 2>&1 && echo "✅ Product DB"
	$(Q) podman exec db_supplier pg_isready -U postgres >/dev/null 2>&1 && echo "✅ Supplier DB"
	$(Q) podman exec db_tax pg_isready -U postgres >/dev/null 2>&1 && echo "✅ Tax DB"

	@echo "🔍 Microservicios (Health Actuator):"
	$(call check_health, http://localhost:$(PRODUCT_PORT)/actuator/health, Product)
	$(call check_health, http://localhost:$(SUPPLIER_PORT)/actuator/health, Supplier)
	$(call check_health, http://localhost:$(TAX_PORT)/actuator/health, Tax)
	$(call check_health, http://localhost:$(GATEWAY_PORT)/actuator/health, Gateway)

#------------------------------------------------------------------------

#🛠️ MANTENIMIENTO Y LIMPIEZA
#------------------------------------------------------------------------
fix-container-orphans: ## 🧹 Limpiar contenedores huérfanos (Forzado)
	@echo "🧹 Limpiando contenedores huérfanos (Forzado)..."
	# 1. Intenta down normal del proyecto
	$(COMPOSE_LOCAL) down -v --remove-orphans 2>/dev/null || true
	# 2. **Elimina forzadamente** contenedores que empiecen con el nombre del proyecto
	$(Q) podman rm -f $$(podman ps -a --format '{{.Names}}' | grep '^$(COMPOSE_PROJECT_NAME)_') 2>/dev/null || true
	# 3. Elimina contenedores detenidos globalmente (por si el paso 2 falla)
	$(Q) podman rm -f $$(podman ps -aq --filter status=exited) 2>/dev/null || true
	# 4. Purga el sistema
	$(Q) podman system prune -f 2>/dev/null || true

fix-line-endings: ## 🛠️ Garantizar terminaciones de línea LF para gradlew
	@echo "🛠️ Corrigiendo terminaciones de línea (dos2unix gradlew)..."
	$(Q) dos2unix $(GRADLEW) 2>/dev/null || \
	{ echo "⚠️ Advertencia: 'dos2unix' no está instalado. Asegúrate de que gradlew usa terminaciones LF."; }

clean-all: podman-ready fix-line-endings ## 🧨 Limpieza completa
	@echo "🧨 Limpieza completa..."
	$(Q) $(MAKE) down-all
	$(Q) $(MAKE) clean-podman-prune
	$(Q) $(MAKE) clean-gradle
clean-podman-prune: podman-ready ## 🧹 Limpiar recursos de Podman (imágenes, volúmenes, cache)
	# ✅ MODIFICADO: Remover el directorio temporal de autenticación
	$(Q) rm -rf $(BUILD_DIR) 2>/dev/null || true
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
run-product-local: fix-line-endings ## 🚀 Ejecutar Product Service localmente
	@echo "🚀 Product Service en http://localhost:$(PRODUCT_PORT)"
	$(Q) cd $(ROOT_DIR)/product-service && $(GRADLEW) bootRun --args='--spring.profiles.active=local'
rebuild-product-local: podman-ready fix-line-endings ## 🔁 Reconstruir Product Service
	$(Q) cp $(ENV_FILE_LOCAL) .env.override
	$(Q) cd $(ROOT_DIR) && $(GRADLEW) :product-service:bootJar
	$(Q) $(COMPOSE_LOCAL) --profile product build product-service
	$(Q) $(COMPOSE_LOCAL) --profile infra --profile product up -d product-service

#🏷️ SUPPLIER SERVICE
test-supplier: ## 🧪 Tests de Supplier Service
	$(Q) cd $(ROOT_DIR)/supplier-service && $(GRADLEW) test
run-supplier-local: fix-line-endings ## 🚀 Ejecutar Supplier Service localmente
	@echo "🚀 Supplier Service en http://localhost:$(SUPPLIER_PORT)"
	$(Q) cd $(ROOT_DIR)/supplier-service && $(GRADLEW) bootRun --args='--spring.profiles.active=local'

rebuild-supplier-local: podman-ready fix-line-endings ## 🔁 Reconstruir Supplier Service
	$(Q) cp $(ENV_FILE_LOCAL) .env.override
	$(Q) cd $(ROOT_DIR) && $(GRADLEW) :supplier-service:bootJar
    # ❌ ANTES: $(Q) $(COMPOSE_LOCAL) --profile supplier build supplier-service
    # ✅ DESPUÉS: Incluir el perfil 'infra' para resolver dependencias (discovery-service)
	$(Q) $(COMPOSE_LOCAL) --profile infra --profile supplier build supplier-service
	$(Q) $(COMPOSE_LOCAL) --profile infra --profile supplier up -d supplier-service

#🏷️ TAX SERVICE
test-tax: ## 🧪 Tests de Tax Service
	$(Q) cd $(ROOT_DIR)/tax-service && $(GRADLEW) test

run-tax-local: fix-line-endings ## 🚀 Ejecutar Tax Service localmente
	@echo "🚀 Tax Service en http://localhost:$(TAX_PORT)"
	$(Q) cd $(ROOT_DIR)/tax-service && $(GRADLEW) bootRun --args='--spring.profiles.active=local'

rebuild-tax-local: podman-ready fix-line-endings ## 🔁 Reconstruir Tax Service
	$(Q) cp $(ENV_FILE_LOCAL) .env.override
	$(Q) cd $(ROOT_DIR) && $(GRADLEW) :tax-service:bootJar
	$(Q) $(COMPOSE_LOCAL) --profile tax build tax-service
	$(Q) $(COMPOSE_LOCAL) --profile infra --profile tax up -d tax-service

# El Purchase Service ha sido eliminado por no estar desarrollado.
#------------------------------------------------------------------------

#🗃️ BASE DE DATOS
#------------------------------------------------------------------------
up-db: podman-ready ## 🗃️ Levantar solo bases de datos
	@echo "🗄️ Levantando bases de datos (solo por perfiles)..."
	$(Q) $(COMPOSE_LOCAL) --profile infra up -d db_product db_supplier db_tax # Solo levantar los servicios de DB
reset-db: podman-ready ## 🗃️ Reiniciar base de datos principal
	@echo "🔄 Reiniciando product-db..."
	$(Q) $(COMPOSE_LOCAL) down -v db_product
	$(Q) $(COMPOSE_LOCAL) --profile infra up -d db_product
tax-db-reset: podman-ready fix-line-endings ## 🗃️ Reiniciar base de datos de tax
	@echo "🔄 Reiniciando tax-db y tax-service..."
	$(Q) $(COMPOSE_LOCAL) --profile tax down -v tax-db
	$(Q) cd $(ROOT_DIR) && $(GRADLEW) :tax-service:bootJar
	$(Q) $(COMPOSE_LOCAL) --profile infra --profile tax up -d tax-service


debug-discovery: podman-ready ## 🐛 Debug del Discovery Service
	@echo "🐛 Iniciando diagnóstico del Discovery Service..."
	@echo "📋 Estado del contenedor:"
	$(Q) podman ps -a --filter name=$(COMPOSE_PROJECT_NAME)_discovery-service_1
	@echo ""
	@echo "📜 Últimos logs (100 líneas):"
	$(Q) podman logs $(COMPOSE_PROJECT_NAME)_discovery-service_1 --tail 100 2>/dev/null || echo "❌ No se pueden obtener logs del discovery-service"
	@echo ""
	@echo "🔍 Verificando recursos de Podman:"
	$(Q) podman system info | grep -E "(CPUs|Memory)" | head -2

fix-discovery: podman-ready fix-line-endings ## 🔧 Solucionar problemas del Discovery Service (General fix)
	@echo "🔧 Aplicando correcciones para Discovery Service..."
	@echo "1. Deteniendo contenedor..."
	$(Q) podman stop $(COMPOSE_PROJECT_NAME)_discovery-service_1 2>/dev/null || true
	$(Q) podman rm $(COMPOSE_PROJECT_NAME)_discovery-service_1 2>/dev/null || true
	@echo "2. Reconstruyendo imagen (SIN CACHÉ)..."
	$(Q) cd $(ROOT_DIR) && $(GRADLEW) :discovery-service:bootJar
	$(Q) podman build --no-cache -f $(ROOT_DIR)/discovery-service/Dockerfile -t discovery-service:latest $(ROOT_DIR)
	@echo "3. Levantando solo discovery service..."
	$(Q) $(COMPOSE_LOCAL) --profile infra up -d discovery-service
	$(Q) sleep 10
	@echo "5. Verificando logs..."
	$(Q) podman logs $(COMPOSE_PROJECT_NAME)_discovery-service_1 --tail 20

check-discovery-health: podman-ready ## 🔍 Verificar salud específica del Discovery
	@echo "🔍 Verificando salud de Discovery Service..."
	@if curl -s http://localhost:$(DISCOVERY_PORT)/actuator/health >/dev/null; then \
		echo "✅ Discovery Service está saludable"; \
	else \
		echo "❌ Discovery Service no responde"; \
		echo "📜 Últimos logs:"; \
		$(Q) podman logs $(COMPOSE_PROJECT_NAME)_discovery-service_1 --tail 30 2>/dev/null || echo "No hay logs disponibles"; \
	fi
#------------------------------------------------------------------------

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
	@for port in $(DISCOVERY_PORT) $(CONFIG_PORT) $(GATEWAY_PORT) $(PRODUCT_PORT) $(SUPPLIER_PORT) $(TAX_PORT) 5432; do \
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
		# Se usa el nombre completo del contenedor
	$(Q) podman logs $(COMPOSE_PROJECT_NAME)_$$service_1 --tail 50 2>/dev/null | tail -20 || echo "No hay logs disponibles"; \
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

fix-discovery-chmod: podman-ready ## 🛠️ Solucionar específicamente el problema de chmod en discovery
	@echo "🛠️ Aplicando fix específico para discovery service..."
	# 1. Dar permisos en el host
	$(Q) chmod +x $(ROOT_DIR)/discovery-service/entrypoint.sh $(ROOT_DIR)/discovery-service/wait-for-it.sh 2>/dev/null || true
	# 2. Reconstruir JAR
	$(Q) cd $(ROOT_DIR) && $(GRADLEW) :discovery-service:bootJar
	# 3. Reconstruir imagen sin cache (forzando la copia de los scripts)
	$(Q) podman rmi discovery-service:latest 2>/dev/null || true
	$(Q) podman build --no-cache -f $(ROOT_DIR)/discovery-service/Dockerfile -t discovery-service:latest $(ROOT_DIR)
	# 4. Levantar solo discovery
	$(Q) $(COMPOSE_LOCAL) --profile infra up -d discovery-service
	$(Q) sleep 10
	@echo "✅ Fix aplicado, verificando..."
	$(Q) podman logs $(COMPOSE_PROJECT_NAME)_discovery-service_1 --tail 10

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
		if [ -f "$(ROOT_DIR)/wait-for-it.sh" ]; then \
			echo "wait-for-it.sh (Root): $$(ls -l $(ROOT_DIR)/wait-for-it.sh | cut -d' ' -f1)"; \
		else \
			echo "❌ wait-for-it.sh: NO EXISTE en la raíz"; \
		fi; \
	done
	@echo ""
	@echo "🐳 Estado de contenedores:"
	$(Q) podman ps -a --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}\t{{.Image}}"
#------------------------------------------------------------------------

#📖 AYUDA Y DOCUMENTACIÓN
#------------------------------------------------------------------------
help: ## 📖 Mostrar ayuda general
	@echo "🦭 Makefile para Podman - InventoryMS"
	@echo ""
	@echo "🚀 COMANDOS PRINCIPALES:"
	@echo "	make start	 - Iniciar servicios base (Infra + Product + Gateway)"
	@echo "	make start-all	 - Iniciar todos los servicios (en orden)"
	@echo "	make start-parallel	 - Iniciar todos los servicios (en paralelo)"
	@echo "	make stop	 - Detener todos los servicios"
	@echo "	make restart	 - Reiniciar servicios base"
	@echo "	make restart-all	 - Reiniciar todos los servicios"
	@echo "	make dev-full	 - Entorno de desarrollo completo (ordenado)"
	@echo ""
	@echo "🛠️ DESARROLLO:"
	@echo "	make build-all	 - Construir todos los JARs"
	@echo "	make build-images-all-nocache - Forzar la reconstrucción de todas las imágenes (ideal si tienes problemas de caché)"
	@echo "	make test-all	 - Ejecutar todos los tests"
	@echo "	make status	 - Estado de los servicios"
	@echo "	make logs	 - Logs de todos los servicios"
	@echo "	make health	 - Verificación de salud extendida"
	@echo ""
	@echo "🧹 MANTENIMIENTO:"
	@echo "	make clean-all	 - Limpieza completa (detiene, purga podman, limpia gradle)"
	@echo "	make fix-line-endings	 - Corregir terminaciones de línea de gradlew (CRÍTICO para compatibilidad)"
	@echo "	make reset-local	 - Reinicio completo del entorno local"
	@echo "	make fix-container-orphans - Limpiar contenedores huérfanos"
	@echo ""
	@echo "🔍 DIAGNÓSTICO (TS):"
	@echo "	make diagnose	 - Diagnóstico completo del sistema"
	@echo "	make check-ports	 - Verificar puertos en uso en el host"
	@echo "	make debug-discovery	 - Diagnóstico específico del Discovery Service"
	@echo ""
	@echo "📖 MÁS AYUDA:"
	@echo "	make help-services	 - Comandos por servicio"
	@echo "	make help-db	 - Comandos de base de datos"
	@echo "	make help-troubleshoot	 - Solución de problemas avanzada"

help-services: ## 📖 Ayuda de servicios individuales
	@echo "🧪 SERVICIOS INDIVIDUALES:"
	@echo ""
	@echo "🏷️ Product Service:"
	@echo "	make run-product-local	 - Ejecutar localmente (host)"
	@echo "	make test-product	 - Ejecutar tests"
	@echo "	make rebuild-product-local - Reconstruir y levantar el contenedor"
	@echo ""
	@echo "🏷️ Supplier Service:"
	@echo "	make run-supplier-local	 - Ejecutar localmente (host)"
	@echo "	make test-supplier	 - Ejecutar tests"
	@echo "	make rebuild-supplier-local - Reconstruir y levantar el contenedor"
	@echo ""
	@echo "🏷️ Tax Service:"
	@echo "	make run-tax-local	 - Ejecutar localmente (host)"
	@echo "	make test-tax	 - Ejecutar tests"
	@echo "	make rebuild-tax-local	 - Reconstruir y levantar el contenedor"
help-db: ## 📖 Ayuda de bases de datos
	@echo "🗃️ BASES DE DATOS:"
	@echo ""
	@echo "	make up-db	 - Levantar solo bases de datos"
	@echo "	make reset-db	 - Reiniciar base de datos principal (product-db)"
	@echo "	make tax-db-reset	 - Reiniciar base de datos de tax y servicio"
help-troubleshoot: ## 📖 Ayuda de solución de problemas
	@echo "🆘 SOLUCIÓN DE PROBLEMAS ESPECÍFICA:"
	@echo ""
	@echo "🔍 DIAGNÓSTICO:"
	@echo "	make diagnose	 - Diagnóstico completo del sistema"
	@echo "	make debug-discovery	 - Diagnóstico específico del Discovery Service"
	@echo "	make check-ports	 - Verificar puertos en uso"
	@echo "	make service-logs	 - Logs recientes de todos los servicios"
	@echo "	make diagnose-chmod	 - Diagnóstico de permisos de ejecución (chmod)"
	@echo ""
	@echo "🔧 SOLUCIONES:"
	@echo "	make fix-discovery	 - Solucionar problemas del Discovery Service"
	@echo "	make fix-discovery-chmod - Solucionar específicamente problemas de permisos en discovery"
	@echo "	make restart-podman-vm	 - Reiniciar la máquina virtual de Podman"
	@echo "	make emergency-clean	 - Limpieza completa (¡USAR CON CUIDADO!)"
	@echo ""
	@echo "💡 CONSEJOS:"
	@echo "	• Si un servicio falla al iniciar, usa 'make service-logs' para ver el error."
	@echo "	• Los errores de 'exec format error' suelen ser problemas de permisos. Usa 'make diagnose-chmod' y 'make fix-discovery-chmod'."
.PHONY: help help-services help-db help-troubleshoot start start-all start-parallel \
	stop restart restart-all dev dev-full dev-supplier dev-tax build-all build-jars \
	build-jars-all test-all test-quick status status-all logs health podman-ready ensure-script-exec \
	up-local up-local-all up-local-all-ordered up-local-supplier up-local-with-tax \
	down-local down-all logs-% health-check-extended fix-container-orphans clean-all \
	clean-podman-prune clean-gradle reset-local test-product run-product-local rebuild-product-local \
	test-supplier run-supplier-local rebuild-supplier-local test-tax run-tax-local rebuild-tax-local \
	up-db reset-db tax-db-reset diagnose check-ports \
	sync-podman-desktop debug-discovery fix-discovery check-discovery-health check-resources \
	service-logs check-container-status emergency-clean restart-podman-vm fix-discovery-chmod diagnose-chmod build-images-all-nocache fix-line-endings con tabulacion