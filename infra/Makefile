#------------------------------------------------------------------------
# 🦭 Makefile para Podman - Sistema de Microservicios InventoryMS
#------------------------------------------------------------------------
# ✅ MODIFICADO: Definición del directorio de build temporal para la autenticación
BUILD_DIR := $(abspath $(CURDIR)/.build)
AUTH_FILE := $(BUILD_DIR)/config.json
#------------------------------------------------------------------------
# 📦 Variables de entorno y configuración
#------------------------------------------------------------------------
CURDIR := $(abspath $(dir $(lastword $(MAKEFILE_LIST))))
ROOT_DIR := $(abspath $(CURDIR)/..)
GRADLEW := $(ROOT_DIR)/gradlew
BUILD_DIR  := $(CURDIR)/.build
AUTH_FILE  := $(BUILD_DIR)/config.json

DOCKER_COMPOSE_BIN := /opt/homebrew/bin/docker-compose
COMPOSE_FILE       := docker-compose-podman.yml
COMPOSE_PROJECT_NAME := inventoryms
DOCKERFILE_NAME    := Dockerfile

IMAGE_DB ?= postgres:16

PROFILES_BUILD_ORDER := infra gateway product supplier tax purchase auth
SERVICES_TO_BUILD    := discovery-service config-service api-gateway product-service supplier-service tax-service purchase-service auth-service

# ✅ SOLUCIÓN PARA MAC: Detectar el socket de Podman automáticamente
# Esto evita el error "Is the docker daemon running?"
export DOCKER_HOST ?= unix://$(shell podman machine inspect --format '{{.ConnectionInfo.PodmanSocket.Path}}')

# Esta forma es más segura para que Make no rompa los argumentos
COMPOSE_PODMAN = docker-compose-podman.yml
COMPOSE_DEBUG  = docker-compose-debug.yml

# Archivos de entorno

ENV_FILE_LOCAL 				:= $(abspath $(CURDIR)/.env.local)
ENV_FILE_DEV                := $(abspath $(CURDIR)/.env.dev)
ENV_FILE_PROD               := $(abspath $(CURDIR)/.env.prod)




COMPOSE_FILE_OPTIMIZED := docker-compose-optimizado-podman.yml  # <--- Definir el nuevo archivo

COMPOSE_PROVIDER ?= podman
export DOCKER_BUILDKIT := 0
export COMPOSE_DOCKER_CLI_BUILD ?= 0 # Habilitar BuildKit

# Perfiles y servicios (PURCHASE/purchase-service excluidos)
PROFILES_BUILD_ORDER := infra gateway product supplier tax purchase
# Servicios que necesitan construcción de imágenes (ordenados)
SERVICES_TO_BUILD := discovery-service config-service api-gateway product-service supplier-service tax-service purchase-service

# --- Carga de variables de entorno para Make ---
ifneq ("$(wildcard $(ENV_FILE_LOCAL))","")
    include $(ENV_FILE_LOCAL)
    export $(shell sed 's/=.*//' $(ENV_FILE_LOCAL))
endif


# Puertos de servicios
API_GATEWAY_PORT ?= 8090
CONFIG_PORT      ?= 7777
AUTH_PORT        ?= 8081
DISCOVERY_PORT   ?= 8761
PRODUCT_PORT     ?= 9090
SUPPLIER_PORT    ?= 9091
TAX_PORT         ?= 9092
PURCHASE_PORT    ?= 9093

# --- Puertos de Bases de Datos (Host) ---
PRODUCT_DB_PORT_HOST  ?= 5433
SUPPLIER_DB_PORT_HOST ?= 5434
PURCHASE_DB_PORT_HOST ?= 5435
TAX_DB_PORT_HOST      ?= 5436
AUTH_DB_PORT_HOST     ?= 5437

ifeq ($(ENV), prod)
    DOCKERFILE_NAME := Dockerfile
    STRATEGY_MSG := "🏗️  MODO PRODUCCIÓN: Usando Multi-stage Build (Lento/Interno)"
else
    DOCKERFILE_NAME := Dockerfile.local
    STRATEGY_MSG := "🚀 MODO DESARROLLO: Usando JARs locales (Rápido)"
endif

# Exportamos la variable para que docker-compose-podman.yml pueda leerla
export DOCKERFILE_NAME

# Perfiles y servicios (PURCHASE/purchase-service excluidos)
PROFILES_ALL := infra gateway product supplier tax purchase
SERVICES_ALL := discovery-service config-service api-gateway product-service supplier-service tax-service purchase-service

# Comandos base
RUN := COMPOSE_PROVIDER=$(COMPOSE_PROVIDER) COMPOSE_PROJECT_NAME=$(COMPOSE_PROJECT_NAME)
COMPOSE_BASE := $(RUN) podman compose -f $(COMPOSE_FILE)
# ✅ Forzamos el directorio del proyecto a ROOT_DIR

#COMPOSE_OPTIMIZED := $(RUN) podman compose -f $(CURDIR)/$(COMPOSE_FILE_OPTIMIZED) --env-file $(ENV_FILE_LOCAL) --project-directory $(ROOT_DIR)
COMPOSE_OPTIMIZED := $(RUN) podman compose \
    -f $(abspath $(CURDIR)/$(COMPOSE_FILE_OPTIMIZED)) \
    --env-file $(abspath $(ENV_FILE_LOCAL)) \
    --project-directory $(abspath $(ROOT_DIR))
# Comandos específicos por entorno
COMPOSE_LOCAL := $(COMPOSE_BASE) --env-file $(ENV_FILE_LOCAL)
COMPOSE_LOCAL_SUPPLIER := $(COMPOSE_BASE) --env-file $(ENV_FILE_LOCAL_SUPPLIER_RESOLVED)
COMPOSE_DEV := $(COMPOSE_BASE) --env-file $(ENV_FILE_DEV)
COMPOSE_PROD := $(COMPOSE_BASE) --env-file $(ENV_FILE_PROD)

PURCHASE_SERVICE_NAME := purchase-service

# Extraer valores de tu .env.local
#D_USER := $(shell grep "^DOCKER_USERNAME=" .env.local | cut -d'=' -f2)
#D_PASS := $(shell grep "^DOCKER_PASSWORD=" .env.local | cut -d'=' -f2)

D_USER := $(shell grep "^DOCKER_USERNAME=" .env.local | cut -d'=' -f2 | tr -d '\r' | xargs)
D_PASS := $(shell grep "^DOCKER_PASSWORD=" .env.local | cut -d'=' -f2 | tr -d '\r' | xargs)
#------------------------------------------------------------------------
# 📦 Carga de variables de entorno para Make
#------------------------------------------------------------------------
ifneq ("$(wildcard .env.local)","")
	include .env.local
	export $(shell sed 's/=.*//' .env.local)
endif

ifeq ($(wildcard $(ENV_FILE_LOCAL)),)
	$(error ERROR: El archivo .env.local NO existe en $(CURDIR))
endif
# Variables de utilidad para salida limpia
Q := @
ifndef VERBOSE
.SILENT:
endif

define check_health_url
	@count=0; \
	echo "   🔍 Verificando salud en $(1)..."; \
	until curl -s -f $(1) > /dev/null; do \
		count=$$((count + 1)); \
		if [ $$count -ge 20 ]; then \
			echo "   ❌ ERROR: $(2) nunca arrancó en la URL $(1)"; \
			exit 1; \
		fi; \
		echo "   ⏳ $(2) está iniciando... (intento $$count/20)"; \
		sleep 3; \
		done; \
		echo "   ✅ $(2) está UP y respondiendo!"
endef

define check_health
	@count=0; \
	echo "   🔍 Verificando estado de $(2)..."; \
	until [ "$$(podman inspect --format='{{.State.Status}}' $(COMPOSE_PROJECT_NAME)-$(2)-service-1 2>/dev/null)" = "running" ] || \
	      [ "$$(podman inspect --format='{{.State.Status}}' $(2)-service 2>/dev/null)" = "running" ]; do \
		count=$$((count + 1)); \
		if [ $$count -ge 5 ]; then \
			echo "   ⚠️ $(2) ya debería estar corriendo. Forzando paso para desbloquear red..."; \
			break; \
		fi; \
		echo "   ⏳ $(2) esperando contenedor... (intento $$count/5)"; \
		sleep 2; \
	done; \
	echo "   ✅ Mircoservico [$(2)] validado y listo!"
endef

define print_row
	NAME=$$(echo $(1) | cut -d':' -f1); \
	PORT=$$(echo $(1) | cut -d':' -f2); \
	RAW_STATUS=$$(podman inspect -f '{{.State.Status}}' $${NAME} 2>/dev/null || echo "down"); \
	RAW_HEALTH=$$(podman inspect -f '{{.State.Health.Status}}' $${NAME} 2>/dev/null || echo "n/a"); \
	STATUS=$$(echo $$RAW_STATUS | tr '[:lower:]' '[:upper:]'); \
	HEALTH_VAL=$$(echo $$RAW_HEALTH | tr '[:lower:]' '[:upper:]'); \
	if [ "$$HEALTH_VAL" = "HEALTHY" ]; then \
		HEALTH="\033[0;32m$$HEALTH_VAL\033[0m"; \
	else \
		HEALTH="\033[0;31m$$HEALTH_VAL\033[0m"; \
	fi; \
	printf "| %-20s | %-12s | %-10s | %-24b |\n" "$$NAME" "$$STATUS" "$$PORT" "$$HEALTH"
endef


#🎯 COMANDOS PRINCIPALES - FLUJOS DE TRABAJO ESENCIALES
#------------------------------------------------------------------------
#🚀 INICIAR SERVICIOS
start: up-local ## 🚀 Iniciar servicios base (Infra + Product + Gateway)


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
	$(Q) cd $(ROOT_DIR) && $(GRADLEW) :config-service:bootJar :discovery-service:bootJar :api-gateway:bootJar :product-service:bootJar :auth-service:bootJar :tax-service:bootJar :supplier-service:bootJar :purchase-service:bootJar

build-jars-all: fix-line-endings
	@echo "🔨 Construyendo todos los JARs desde la raíz del proyecto..."
	$(Q) cd $(ROOT_DIR) && ./gradlew clean :config-service:bootJar :discovery-service:bootJar \
    	:api-gateway:bootJar :product-service:bootJar :supplier-service:bootJar \
      	:tax-service:bootJar :purchase-service:bootJar :auth-service:bootJar
	@echo "✅ JARs generados exitosamente."
	$(Q) ls -l $(ROOT_DIR)/discovery-service/build/libs/*.jar || (echo "❌ Error: JAR de Discovery no encontrado"; exit 1)

build-images: podman-auth ## 🏗️ Construir imágenes
	@echo "======================================================================"
	@echo "🌍 AMBIENTE: $(ENV)"
	@echo "💡 $(STRATEGY_MSG)"
	@echo "📄 USANDO: $(DOCKERFILE_NAME)"
	@echo "======================================================================"
	$(DOCKER_COMPOSE_BIN) -f $(COMPOSE_FILE) build \
		--build-arg DOCKER_USERNAME=$(DOCKER_USERNAME) \
		--build-arg DOCKER_PASSWORD=$(DOCKER_PASSWORD) \
	$(SERVICES_TO_BUILD)

build-images-all-nocache: build-jars-all ## 🔨 Construye TODAS las imágenes sin caché (SERIAL)
	@echo "🔨 Construyendo imágenes SIN caché para todos los servicios (SERIAL)..."
	export DOCKER_CONFIG=$(BUILD_DIR); \
	echo "   [1/6] Construyendo discovery-service y config-service..."; \
	$(COMPOSE_LOCAL) --profile infra build --no-cache \
		--build-arg DOCKER_USERNAME=$(D_USER) --build-arg DOCKER_PASSWORD=$(D_PASS) \
		discovery-service config-service; \
	echo "   [2/6] Construyendo product-service..."; \
	$(COMPOSE_LOCAL) --profile infra --profile product build --no-cache \
		--build-arg DOCKER_USERNAME=$(D_USER) --build-arg DOCKER_PASSWORD=$(D_PASS) \
		product-service; \
	echo "   [3/6] Construyendo supplier-service..."; \
	$(COMPOSE_LOCAL) --profile infra --profile supplier build --no-cache \
		--build-arg DOCKER_USERNAME=$(D_USER) --build-arg DOCKER_PASSWORD=$(D_PASS) \
		supplier-service; \
	echo "   [4/6] Construyendo tax-service..."; \
	$(COMPOSE_LOCAL) --profile infra --profile tax build --no-cache \
		--build-arg DOCKER_USERNAME=$(D_USER) --build-arg DOCKER_PASSWORD=$(D_PASS) \
		tax-service; \
	echo "   [5/6] Construyendo purchase-service..."; \
        $(COMPOSE_LOCAL) --profile infra --profile purchase build --no-cache \
           --build-arg DOCKER_USERNAME=$(D_USER) --build-arg DOCKER_PASSWORD=$(D_PASS) \
           purchase-service; \
	echo "   [6/6] Construyendo api-gateway..."; \
        $(COMPOSE_LOCAL) --profile infra --profile gateway build --no-cache \
		--build-arg DOCKER_USERNAME=$(D_USER) --build-arg DOCKER_PASSWORD=$(D_PASS) \
		api-gateway; \
	echo "   [7/7] Construyendo auth-service..."; \
        $(COMPOSE_LOCAL) --profile infra --profile auth build --no-cache \
           --build-arg DOCKER_USERNAME=$(D_USER) --build-arg DOCKER_PASSWORD=$(D_PASS) \
           auth-service; \
    echo "✅ Construcción de todas las imágenes completada."

#------------------------------------------------------------------------

#🧪 TESTING
test-all: fix-line-endings ## 🧪 Ejecutar todos los tests
	$(Q) cd $(ROOT_DIR) && $(GRADLEW) test
test-quick: test-product test-supplier test-tax ## 🧪 Tests rápidos de servicios principales

#📊 MONITOREO
status: podman-ready ## 📊 Estado de todos los servicios
	$(COMPOSE_LOCAL) ps
status-all: status ## 📊 Alias para status

# 1. Levanta solo lo que no cambia (Bases de datos + Discovery + Config)
# Se hace una sola vez al iniciar el día
up-infra: podman-ready
	@echo "🚀 Levantando infraestructura base..."
	$(COMPOSE_BASE) up -d db_product db_supplier db_tax db_purchase discovery-service config-service
	@echo "⏳ Esperando 15s para estabilización..."
	@sleep 15
	$(COMPOSE_BASE) ps


up-debug:
	$(DOCKER_COMPOSE_BASE) -f $(FILE_DEBUG) up -d

# 2. Levanta un MS específico instantáneamente (sin reconstruir todo)
# Uso: make up-ms s=product-service
up-ms:
	$(DOCKER_COMPOSE) up -d $(s)

# 3. Re-compila y levanta solo UN microservicio (lo que usarás el 90% del tiempo)
# Uso: make dev-ms s=product-service
dev-ms:
	./gradlew :$(s):build -x test
	$(DOCKER_COMPOSE) up -d --build $(s)

logs: podman-ready ## 📜 Logs de todos los servicios
	$(COMPOSE_LOCAL) logs -f

health: health-check-extended ## 💚 Verificación de salud completa

# --- Autenticación Automática ---
#podman-auth2:
#	@echo "🔑 Configurando autenticación automática para Podman..."
#	@# Extraemos las credenciales directamente del archivo .env.local
#	$(eval DOCKER_USER := $(shell grep DOCKER_USERNAME $(ENV_FILE_LOCAL) | cut -d'=' -f2))
#	$(eval DOCKER_PASS := $(shell grep DOCKER_PASSWORD $(ENV_FILE_LOCAL) | cut -d'=' -f2))
#	@echo "Iniciando sesión en docker.io como $(DOCKER_USER)..."
#	@echo "$(DOCKER_PASS)" | podman login docker.io -u "$(DOCKER_USER)" --password-stdin

#podman-auth1: ## 🔑 Autenticar en Docker Hub usando .env.local
#	@echo "🔑 Configurando autenticación automática para Podman..."
#	-podman logout docker.io > /dev/null 2>&1
#	$(eval DOCKER_USER := $(shell grep "^DOCKER_USERNAME=" $(ENV_FILE_LOCAL) | cut -d'=' -f2 | tr -d '\r' | xargs))
#	$(eval DOCKER_PASS := $(shell grep "^DOCKER_PASSWORD=" $(ENV_FILE_LOCAL) | cut -d'=' -f2 | tr -d '\r' | xargs))
#	@if [ -z "$(DOCKER_USER)" ] || [ -z "$(DOCKER_PASS)" ]; then \
#		echo "❌ Error: Credenciales no encontradas en $(ENV_FILE_LOCAL)"; \
#		exit 1; \
#	fi
#	@echo "Iniciando sesión en docker.io como $(DOCKER_USER)..."
#	@echo "$(DOCKER_PASS)" | podman login docker.io -u "$(DOCKER_USER)" --password-stdin || (echo "❌ Error de login"; exit 1)

up-product:
	@echo "🚀 Iniciando Product Service..."
	$(DOCKER_COMPOSE_BIN) --env-file $(ENV_FILE_LOCAL) -f $(COMPOSE_FILE) --profile product up -d

up-tax:
	@echo "🚀 Iniciando Tax Service..."
	$(DOCKER_COMPOSE_BIN) --env-file $(ENV_FILE_LOCAL) -f $(COMPOSE_FILE) --profile tax up -d

.PHONY: up-purchase
up-purchase:
	@echo "🚀 Iniciando Purchase Service con entorno consolidado..."
	$(DOCKER_COMPOSE_BIN) --env-file $(ENV_FILE_LOCAL) -f $(COMPOSE_FILE) --profile purchase up -d


podman-auth: ## 🔑 Autenticar y asegurar imagen base
	@mkdir -p $(BUILD_DIR)
	@echo "==========================================================================================================================================================================="
	@echo "🔑 Configurando autenticación automática para Podman..."
	@echo "==========================================================================================================================================================================="
	@# Logout silencioso para refrescar sesión
	-@podman logout docker.io > /dev/null 2>&1
	$(eval DOCKER_USER := $(shell grep "^DOCKER_USERNAME=" $(ENV_FILE_LOCAL) | cut -d'=' -f2 | tr -d '\r\n ' ))
	$(eval DOCKER_PASS := $(shell grep "^DOCKER_PASSWORD=" $(ENV_FILE_LOCAL) | cut -d'=' -f2 | tr -d '\r\n ' ))
	@if [ -z "$(DOCKER_USER)" ] || [ -z "$(DOCKER_PASS)" ]; then \
		echo "❌ Error: Credenciales no encontradas en $(ENV_FILE_LOCAL)"; \
		exit 1; \
	fi
	@echo "Iniciando sesión en docker.io como $(DOCKER_USER)..."
	@echo "$(DOCKER_PASS)" | podman login docker.io -u "$(DOCKER_USER)" --password-stdin --authfile $(AUTH_FILE) || (echo "❌ Error de login"; exit 1)
	@echo "✅ Sesión guardada en $(AUTH_FILE)"
	@echo "📥 Asegurando imagen base Java 21..."
	@podman pull --authfile $(AUTH_FILE) eclipse-temurin:21-jdk-jammy || (echo "❌ Error al descargar imagen base"; exit 1)
	@echo "✅ Imagen base Java 21 lista y actualizada."
	@echo "==========================================================================================================================================================================="
# ------------------------------------------------------------------------
# 🚀 TARGETS DE UP (INICIO)
# ------------------------------------------------------------------------
up-local: podman-ready fix-line-endings fix-container-orphans ## 🧪 Infra + Product + Gateway
	@# Logout silencioso para refrescar sesión
	-@podman logout docker.io > /dev/null 2>&1
	$(eval DOCKER_USER := $(shell grep "^DOCKER_USERNAME=" $(ENV_FILE_LOCAL) | cut -d'=' -f2 | tr -d '\r' | xargs))
	$(eval DOCKER_PASS := $(shell grep "^DOCKER_PASSWORD=" $(ENV_FILE_LOCAL) | cut -d'=' -f2 | tr -d '\r' | xargs))
	@if [ -z "$(DOCKER_USER)" ] || [ -z "$(DOCKER_PASS)" ]; then \
		echo "❌ Error: Credenciales no encontradas en $(ENV_FILE_LOCAL)"; \
			exit 1; \
	fi
	@echo "🧪 Levantando: Infra + Product + Gateway"
	$(Q) cp $(ENV_FILE_LOCAL) .env.override
	$(Q) $(MAKE) build-jars
	$(Q) $(COMPOSE_LOCAL) --profile infra --profile product --profile gateway up -d --build --remove-orphans


.PHONY: test-purchase run-purchase-local rebuild-purchase-local purchase-db-reset
.PHONY: test-purchase run-purchase-local rebuild-purchase-local purchase-db-reset

up-local-all-ordered: podman-auth podman-ready fix-line-endings
	@# 1. Guardar inicio en un archivo temporal
	@date +%s > .deploy_start
	@date +%s > .start_time
	@$(MAKE) clean-env
	@echo "======================================================================================"
	@echo "🚀 INICIANDO DESPLIEGUE (ESTRATEGIA ANTI-COLAPSO)"
	@echo "======================================================================================"

	@echo "🔨 Compilando JARs..."
	@$(MAKE) build-jars-all

	$(eval CMD := $(DOCKER_COMPOSE_BIN) --env-file $(ENV_FILE_LOCAL) -f $(COMPOSE_FILE) -p $(COMPOSE_PROJECT_NAME) --profile infra --profile product --profile supplier --profile tax --profile purchase --profile gateway --profile auth)

	@echo "🏗️  1. Infraestructura..."
	@$(CMD) up -d --build discovery-service config-service
	@sleep 10
	$(call check_health_url,http://localhost:$(DISCOVERY_PORT)/actuator/health,Discovery)

	@echo "🗄️  2. Bases de Datos..."
	@$(CMD) up -d db_product db_supplier db_tax db_purchase db_auth
	@sleep 15

	@echo "🚀 3. Servicios de Negocio..."
	@for service in product-service auth-service supplier-service tax-service purchase-service; do \
       echo "   -> Iniciando $$service..."; \
       $(CMD) up -d --build $$service; \
       sleep 5; \
    done

	@echo "🌐 4. API Gateway..."
	@$(CMD) up -d --build api-gateway
	@echo "⏳ Esperando 30s a que el Gateway registre las rutas..."
	@sleep 30

	@$(MAKE) health

	@echo ""
	@echo "========================================================================"
	@echo "                RESUMEN FINAL DE DESPLIEGUE"
	@echo "========================================================================"
	@printf "| %-20s | %-12s | %-10s | %-15s |\n" "SERVICIO" "ESTADO" "PUERTO" "HEALTH"

	@echo "----------------------- INFRAESTRUCTURA & CORE -------------------------"
	@$(foreach svc,discovery-service:$(DISCOVERY_PORT) config-service:$(CONFIG_PORT) api-gateway:$(API_GATEWAY_PORT),$(call print_row,$(svc));)

	@echo "--------------------------- BASES DE DATOS -----------------------------"
	@$(foreach svc,db_product:$(PRODUCT_DB_PORT_HOST) db_supplier:$(SUPPLIER_DB_PORT_HOST) db_tax:$(TAX_DB_PORT_HOST) db_purchase:$(PURCHASE_DB_PORT_HOST) db_auth:$(AUTH_DB_PORT_HOST),$(call print_row,$(svc));)

	@echo "-------------------------- MICROSERVICIOS ------------------------------"
	@$(foreach svc,product-service:$(PRODUCT_PORT) supplier-service:$(SUPPLIER_PORT) tax-service:$(TAX_PORT) purchase-service:$(PURCHASE_PORT) auth-service:$(AUTH_PORT),$(call print_row,$(svc));)

	@echo "========================================================================"
	@END_VAL=$$(date +%s); \
	START_VAL=$$(cat .start_time 2>/dev/null); \
	if [ -z "$$START_VAL" ]; then START_VAL=$$END_VAL; fi; \
	DIFF=$$(( END_VAL - START_VAL )); \
	H=$$(( DIFF / 3600 )); \
	M=$$(( (DIFF % 3600) / 60 )); \
	S=$$(( DIFF % 60 )); \
	printf "⏱️  Tiempo total de despliegue: %02d:%02d:%02d\n" $$H $$M $$S; \
	rm -f .start_time
	@echo "✅ Proceso completado exitosamente."

.PHONY: clean-env
clean-env: ## 🧹 Limpieza atómica: Contenedores, Volúmenes, Redes y Huérfanos
	@echo "======================================================================"
	@echo "🧹 LIMPIANDO ENTORNO (Proyecto: $(COMPOSE_PROJECT_NAME))"
	@echo "======================================================================"
	@# 1. Bajada estándar con borrado de volúmenes (-v)
	-@$(DOCKER_COMPOSE_BIN) --env-file $(ENV_FILE_LOCAL) -f $(COMPOSE_FILE) -p $(COMPOSE_PROJECT_NAME) --profile auth down -v --remove-orphans 2>/dev/null || true

	@# 2. Limpieza de contenedores que coincidan con el nombre del proyecto (Fuerza bruta)
	@echo "🧨 Eliminando residuos por nombre de proyecto..."
	-@podman rm -f $$(podman ps -aq --filter "name=$(COMPOSE_PROJECT_NAME)") 2>/dev/null || true

	@# 3. Borrar la red específica si quedó colgada
	-@podman network rm $$(podman network ls --filter "name=$(COMPOSE_PROJECT_NAME)" --format "{{.Name}}") 2>/dev/null || true

	@# 4. Limpieza de volúmenes huérfanos del sistema
	@echo "💾 Purgando volúmenes no utilizados..."
	-@podman volume prune -f 2>/dev/null || true
	@echo "✅ Entorno listo para construcción fresca."

.PHONY: clean-docker
clean-docker:
	@echo "Stopping and removing all containers for $(COMPOSE_PROJECT_NAME)..."
	@$(DOCKER_COMPOSE_BIN) -p $(COMPOSE_PROJECT_NAME) down --volumes --remove-orphans
	@echo "Removing network if exists..."
	@podman network rm inventory-net || true
	@rm -f .deploy_start .start_time

.PHONY: build-purchase
build-purchase: ## 🏗️ Construir imagen Docker de Purchase Service
	@echo "==========================================================================================================================================================================="
	@echo "🚀 Construyendo $(PURCHASE_SERVICE_NAME)..."
	@echo "==========================================================================================================================================================================="
	# Usamos las variables de entorno cargadas desde .env.local
	DOCKER_BUILDKIT=0 COMPOSE_DOCKER_CLI_BUILD=0 $(DOCKER_COMPOSE_BIN) -f $(COMPOSE_FILE) --profile purchase build --no-cache \
	   --build-arg DOCKER_USERNAME=$(DOCKER_USERNAME) \
	   --build-arg DOCKER_PASSWORD=$(DOCKER_PASSWORD) \
	   $(PURCHASE_SERVICE_NAME)
	@echo "✅ Construcción de $(PURCHASE_SERVICE_NAME) finalizada."

.PHONY: ensure-script-exec
ensure-script-exec:
	# 🚨 Comando crítico: Garantizar permisos de ejecución al script antes de usarlo.
	$(Q) chmod +x ensure_podman_ready.sh 2>/dev/null || true

.PHONY: down-purchase
down-purchase:
	@echo "Deteniendo $(PURCHASE_SERVICE_NAME)..."
	$(DOCKER_COMPOSE) --profile purchase stop $(PURCHASE_SERVICE_NAME)

.PHONY: logs-purchase
logs-purchase:
	$(DOCKER_COMPOSE) logs -f $(PURCHASE_SERVICE_NAME)

# ------------------------------------------------------------------------
# Targets de Herramientas y Diagnóstico (Versión FINAL que NO se Bloquea ni Causa Sintaxis)
# ------------------------------------------------------------------------
.PHONY: podman-ready
podman-ready: ## 🔍 Confirma el estado y asume que el Login fue manual.
	@echo "⚙️ El entorno de Podman está siendo verificado..."
	@echo "✅ Autenticación y Máquina Podman confirmadas manualmente. Continuando con la construcción."
	# Comprobación simple para asegurar que Podman está disponible
	@podman machine ls >/dev/null 2>&1 || { echo "🚨 ERROR: Podman machine no responde. Ejecute 'podman machine start' manualmente."; exit 1; }

up-local-supplier: docker-login podman-ready fix-line-endings fix-container-orphans ## 🧪 Infra + Supplier
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
down-local: podman-ready ## 🔻 Detener servicios locales y limpiar red SIN errores
	@echo "🧹 Paso 1: Aniquilando contenedores rebeldes (Fuerza bruta)..."
	@# Primero matamos todo lo que use la red para dejar el camino libre
	-@podman ps -a --filter "network=inventory-net" --format "{{.ID}}" | xargs -r podman rm -f >/dev/null 2>&1

	@echo "🛑 Paso 2: Ejecutando cleanup de Docker Compose..."
	@# Ahora Compose no dará error de 'Resource in use' porque ya no hay contenedores
	-@$(COMPOSE_LOCAL) --profile infra --profile product --profile supplier --profile tax --profile purchase --profile gateway --profile auth down -v --remove-orphans >/dev/null 2>&1

	@echo "🌐 Paso 3: Eliminación final de red y temporales..."
	-@podman network rm inventory-net 2>/dev/null || true
	#-@rm -f .start_time .deploy_start
	-@rm -f .start_time .deploy_start 2>/dev/null || true
	@echo "✨ Limpieza completada exitosamente. Todo despejado."

down-all: podman-ready ## 🔻 Detener TODOS los servicios y borrar rastro
	@echo "🧹 Deteniendo todos los perfiles..."
	@$(COMPOSE_LOCAL) $(foreach p,$(PROFILES_ALL),--profile $(p)) down -v --remove-orphans || true
	@$(MAKE) force-clean-network

force-clean-network:
	@echo "🔍 Buscando contenedores huérfanos en la red inventory-net..."
	@# Elimina cualquier contenedor que use la red del proyecto, sin importar el perfil
	@podman ps -a --filter "network=inventory-net" --format "{{.ID}}" | xargs -r podman rm -f
	@echo "🌐 Eliminando red inventory-net..."
	@podman network rm inventory-net 2>/dev/null || true
	@echo "✨ Limpieza completada."

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

health-check-extended2: podman-ready ## 💚 Verificación de salud extendida
	@echo "🩺 Verificación de salud EXTENDIDA..."
	@if ! command -v curl >/dev/null; then echo "❌ Error: 'curl' no está instalado. No se puede verificar la salud de los microservicios."; fi

	@echo "🔍 Infraestructura (Health Actuator):"
	$(call check_health, http://localhost:$(DISCOVERY_PORT)/actuator/health, Discovery)
	$(call check_health, http://localhost:$(CONFIG_PORT)/actuator/health, Config)

	@echo "🔍 Bases de datos (pg_isready):"
	# Se usa el nombre completo del contenedor para mayor robustez
	#$(Q) podman exec db_product pg_isready -U postgres >/dev/null 2>&1 && echo "✅ Product DB"
	#$(Q) podman exec db_supplier pg_isready -U postgres >/dev/null 2>&1 && echo "✅ Supplier DB"
	#$(Q) podman exec db_tax pg_isready -U postgres >/dev/null 2>&1 && echo "✅ Tax DB"
	$(Q) podman exec db_product pg_isready -U postgres >/dev/null 2>&1 && echo "✅ Product DB"
	$(Q) podman exec db_supplier pg_isready -U postgres >/dev/null 2>&1 && echo "✅ Supplier DB"
	$(Q) podman exec db_tax pg_isready -U postgres >/dev/null 2>&1 && echo "✅ Tax DB"
	$(Q) podman exec db_purchase pg_isready -U postgres >/dev/null 2>&1 && echo "✅ Purchase DB"
	@echo "🔍 Microservicios (Health Actuator):"
	$(call check_health, http://localhost:$(PRODUCT_PORT)/actuator/health, Product)
	$(call check_health, http://localhost:$(SUPPLIER_PORT)/actuator/health, Supplier)
	$(call check_health, http://localhost:$(TAX_PORT)/actuator/health, Tax)
	$(call check_health, http://localhost:$(PURCHASE_PORT)/actuator/health, Purchase)
	$(call check_health,$(AUTH_PORT),Auth)
	$(call check_health, http://localhost:$(GATEWAY_PORT)/actuator/health, Gateway)

health-check-extended: podman-ready ## 💚 Verificación de salud extendida
	@echo "🩺 Verificación de salud EXTENDIDA..."
	@if ! command -v curl >/dev/null; then echo "❌ Error: 'curl' no está instalado."; exit 1; fi

	@echo "🔍 Infraestructura:"
	$(call check_health,$(DISCOVERY_PORT),Discovery)
	$(call check_health,$(CONFIG_PORT),Config)

	@echo "🔍 Bases de datos (pg_isready):"
	-@podman exec db_product pg_isready -U postgres >/dev/null 2>&1 && echo "   ✅ Product DB" || echo "   ❌ Product DB"
	-@podman exec db_supplier pg_isready -U postgres >/dev/null 2>&1 && echo "   ✅ Supplier DB" || echo "   ❌ Supplier DB"
	-@podman exec db_tax pg_isready -U postgres >/dev/null 2>&1 && echo "   ✅ Tax DB" || echo "   ❌ Tax DB"
	-@podman exec db_purchase pg_isready -U postgres >/dev/null 2>&1 && echo "   ✅ Purchase DB" || echo "   ❌ Purchase DB"
	-@podman exec db_auth pg_isready -U postgres >/dev/null 2>&1 && echo "   ✅ Auth DB" || echo "   ❌ Auth DB"

	@echo "🔍 Microservicios (Health Actuator):"
	$(call check_health,$(PRODUCT_PORT),Product)
	$(call check_health,$(SUPPLIER_PORT),Supplier)
	$(call check_health,$(TAX_PORT),Tax)
	$(call check_health,$(PURCHASE_PORT),Purchase)
	$(call check_health,$(API_GATEWAY_PORT),Gateway)
	$(call check_health,$(AUTH_PORT),Auth)

#------------------------------------------------------------------------
#🛠️ MANTENIMIENTO Y LIMPIEZA
#------------------------------------------------------------------------
fix-line-endings: ## 🛠️ Garantizar terminaciones de línea LF para gradlew
	@echo "🛠️ Corrigiendo terminaciones de línea (dos2unix gradlew)..."
	$(Q) dos2unix $(GRADLEW) 2>/dev/null || \
	{ echo "⚠️ Advertencia: 'dos2unix' no está instalado. Asegúrate de que gradlew usa terminaciones LF."; }

clean-all: podman-ready fix-line-endings ## 🧨 Limpieza completa
	@echo "🛑 Deteniendo servicios..."
	@$(DOCKER_COMPOSE_BIN) -p $(COMPOSE_PROJECT_NAME) down --remove-orphans
	@echo "🧹 Limpiando servicios específicos..."
	@podman rm -f auth-service db_auth 2>/dev/null || true
	@# No elimines la red manualmente; 'down' ya lo intenta.
	@# Si falla, es porque algo sigue vivo. Usamos 'true' para no romper el Makefile.
	@echo "🌐 Verificando red..."
	@podman network rm $(NETWORK_NAME) 2>/dev/null || true
	@rm -f .start_time
	@echo "✨ Sistema listo para nuevo despliegue."

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
docker-login:
	@echo "🔑 Forzando login de Podman/Docker con credenciales de .env.local..."
	@if [ -z "$(DOCKER_USERNAME)" ] || [ -z "$(DOCKER_PASSWORD)" ]; then \
		echo "🔴 ERROR: DOCKER_USERNAME o DOCKER_PASSWORD no definidos en .env.local."; \
		exit 1; \
	fi
	# Usa pipe para pasar la password. Esto es menos seguro, pero es la única manera de automatizar sin prompt.
	echo "$(DOCKER_PASSWORD)" | podman login -u "$(DOCKER_USERNAME)" --password-stdin docker.io
	@echo "✅ Login completado."
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
test-purcharse: ## 🧪 Tests de Supplier Service
	$(Q) cd $(ROOT_DIR)/purcharse-service && $(GRADLEW) test

run-purchase-local: fix-line-endings ## 🚀 Ejecutar Supplier Service localmente
	@echo "🚀 Purcharse Service en http://localhost:$(PURCHARSE_PORT)"
	$(Q) cd $(ROOT_DIR)/purcharse-service && $(GRADLEW) bootRun --args='--spring.profiles.active=local'

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

rebuild-purchase-local: podman-ready fix-line-endings ## 🔁 Reconstruir Tax Service
	$(Q) cp $(ENV_FILE_LOCAL) .env.override
	$(Q) cd $(ROOT_DIR) && $(GRADLEW) :purchase-service:bootJar
	$(Q) $(COMPOSE_LOCAL) --profile purchase build purchase-service
	$(Q) $(COMPOSE_LOCAL) --profile infra --profile purchase up -d purchase-service

# El Purchase Service ha sido eliminado por no estar desarrollado.
#------------------------------------------------------------------------

#🗃️ BASE DE DATOS
#------------------------------------------------------------------------
up-db: podman-ready ## 🗃️ Levantar solo bases de datos
	@echo "🗄️ Levantando bases de datos (solo por perfiles)..."
	$(Q) $(COMPOSE_LOCAL) --profile infra --profile auth up -d db_product db_supplier db_tax db_purchase db_auth
reset-db: podman-ready ## 🗃️ Reiniciar base de datos principal
	@echo "🔄 Reiniciando product-db..."
	$(Q) $(COMPOSE_LOCAL) down -v db_product
	$(Q) $(COMPOSE_LOCAL) --profile infra up -d db_product
	@echo "🔄 Reiniciando tax-db..."
	$(Q) $(COMPOSE_LOCAL) down -v db_tax
	$(Q) $(COMPOSE_LOCAL) --profile infra up -d db_tax
	@echo "🔄 Reiniciando supplier-db..."
	$(Q) $(COMPOSE_LOCAL) down -v supplier_tax
	$(Q) $(COMPOSE_LOCAL) --profile infra up -d db_supplier
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
		$(Q) podman logs $(COMPOSE_PROJECT_NAME)_$$service_1 --tail 50 2>/dev/null | tail -20 || echo "No hay logs disponibles"; \
	done

check-container-status: ## 🔍 Ver estado detallado de contenedores
	@echo "🔍 Estado detallado de contenedores:"
	$(Q) podman ps -a --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}\t{{.Image}}"

#🚑 COMANDOS DE EMERGENCIA
# --- 🧨 SUPER LIMPIEZA DE EMERGENCIA (UNIFICADA) ---
emergency-clean: podman-ready ## 🧨 Borrado total y profundo (Contenedores, Imágenes, Volúmenes y Redes)
	@echo "🧨 ADVERTENCIA: Se eliminarán TODOS los contenedores, imágenes y volúmenes de Podman."
	@read -p "⚠️ ¿Estás seguro de realizar esta limpieza profunda? (Escribe 'SI' para continuar): " confirm; \
	if [ "$$confirm" = "SI" ]; then \
		echo "🧹 Iniciando limpieza total..."; \
		podman compose -f docker-compose-podman.yml down --volumes --remove-orphans 2>/dev/null || true; \
		echo "🛑 Deteniendo y eliminando contenedores..."; \
		podman stop $$(podman ps -aq) 2>/dev/null || true; \
		podman rm -f $$(podman ps -aq) 2>/dev/null || true; \
		echo "🖼️ Eliminando imágenes..."; \
		podman rmi -f $$(podman images -aq) 2>/dev/null || true; \
		echo "📜 Limpiando redes y volúmenes..."; \
		podman network prune -f; \
		podman volume prune -f; \
		podman system prune -a -f --volumes; \
		echo "✅ Sistema totalmente limpio."; \
		$(MAKE) podman-auth; \
	else \
		echo "❌ Limpieza cancelada por el usuario."; \
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
	@for service in discovery-service config-service api-gateway product-service supplier-service tax-service purchase-service; do \
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


.PHONY: up-local-all-optimized
up-local-all-optimized: podman-auth podman-ready fix-line-endings ## 🚀 Iniciar stack optimizado
	@cp $(ENV_FILE_LOCAL) $(ROOT_DIR)/.env.local 2>/dev/null || true
	@cp $(ENV_FILE_LOCAL) $(CURDIR)/.env.local 2>/dev/null || true
	@START_SECONDS=$$(date +%s); \
  	START_TIME=$$(date +"%Y-%m-%d %H:%M:%S"); \
  	echo "=========================================================================="; \
	echo "🚀 INICIO OPTIMIZADO: $$START_TIME"; \
	echo "=========================================================================="; \
	\
	echo "🔨 Paso 1: Compilando JARs..."; \
	$(MAKE) build-jars-all; \
	\
	ALL_PROFILES="--profile infra --profile product --profile supplier --profile tax --profile purchase --profile gateway"; \
	\
	echo "🔧 Paso 2: Infraestructura y Bases de Datos..."; \
	podman build -t discovery-service:latest -f $(ROOT_DIR)/discovery-service/Dockerfile $(ROOT_DIR); \
	podman build -t config-service:latest -f $(ROOT_DIR)/config-service/Dockerfile $(ROOT_DIR); \
	$(COMPOSE_OPTIMIZED) $$ALL_PROFILES up -d discovery-service config-service db_product db_supplier db_tax db_purchase; \
	\
	echo "⏳ Esperando a que Eureka y Config Server estén saludables..."; \
	MAX_RETRIES=30; \
	COUNT=0; \
	until [ $$COUNT -ge $$MAX_RETRIES ] || (curl -s http://localhost:8761/actuator/health | grep UP > /dev/null && curl -s http://localhost:7777/actuator/health | grep UP > /dev/null); do \
		printf "."; \
		sleep 3; \
		COUNT=$$((COUNT+1)); \
	done; \
	echo "\n✅ Infraestructura lista."; \
	\
	echo "🛠️ Paso 3: Construyendo y Levantando Microservicios..."; \
	podman build -t product-service:latest -f $(ROOT_DIR)/product-service/Dockerfile $(ROOT_DIR); \
	podman build -t supplier-service:latest -f $(ROOT_DIR)/supplier-service/Dockerfile $(ROOT_DIR); \
	podman build -t tax-service:latest -f $(ROOT_DIR)/tax-service/Dockerfile $(ROOT_DIR); \
	podman build -t purchase-service:latest -f $(ROOT_DIR)/purchase-service/Dockerfile $(ROOT_DIR); \
	$(COMPOSE_OPTIMIZED) $$ALL_PROFILES up -d --no-build product-service supplier-service tax-service purchase-service; \
	\
	echo "🌐 Paso 4: API Gateway..."; \
	podman build -t api-gateway:latest -f $(ROOT_DIR)/api-gateway/Dockerfile $(ROOT_DIR); \
	$(COMPOSE_OPTIMIZED) $$ALL_PROFILES up -d --no-build api-gateway; \
	\
	echo "=========================================================================="; \
	echo "📊 ESTADO FINAL"; \
	podman ps --format "{{.Names}}\t{{.Status}}\t{{.Ports}}" | grep -E "service|db|discovery|config|gateway"; \
	\
	END_SECONDS=$$(date +%s); \
	DURATION_SECONDS=$$((END_SECONDS - START_SECONDS)); \
	MINUTES=$$(( (DURATION_SECONDS % 3600) / 60 )); \
	SECONDS=$$((DURATION_SECONDS % 60)); \
	printf "⏳ TIEMPO TOTAL: %02d:%02d\n" $$MINUTES $$SECONDS; \
	echo "=========================================================================="; \
	$(MAKE) health-check-extended
#🏷️ AUTH SERVICE
test-auth: ## 🧪 Tests de Auth Service
	$(Q) cd $(ROOT_DIR)/auth-service && $(GRADLEW) test

run-auth-local: fix-line-endings ## 🚀 Ejecutar Auth Service localmente
	@echo "🚀 Auth Service en http://localhost:$(AUTH_PORT)"
	$(Q) cd $(ROOT_DIR)/auth-service && $(GRADLEW) bootRun --args='--spring.profiles.active=local'

rebuild-auth-local: podman-ready fix-line-endings ## 🔁 Reconstruir Auth Service
	$(Q) cp $(ENV_FILE_LOCAL) .env.override
	$(Q) cd $(ROOT_DIR) && $(GRADLEW) :auth-service:bootJar
	$(Q) $(COMPOSE_LOCAL) --profile infra --profile auth build auth-service
	$(Q) $(COMPOSE_LOCAL) --profile infra --profile auth up -d auth-service

auth-db-reset: podman-ready fix-line-endings ## 🗃️ Reiniciar base de datos de auth
	@echo "🔄 Reiniciando auth-db..."
	$(Q) $(COMPOSE_LOCAL) --profile auth down -v db_auth
	$(Q) $(COMPOSE_LOCAL) --profile infra --profile auth up -d db_auth

.PHONY: help help-services help-db help-troubleshoot start start-all \
    stop restart restart-all dev dev-full dev-supplier dev-tax build-all build-jars \
    build-jars-all test-all test-quick status status-all logs health podman-ready ensure-script-exec \
    up-local up-local-all up-local-all-ordered up-local-supplier up-local-with-tax \
    down-local down-all logs-% health-check-extended fix-container-orphans clean-all \
    clean-podman-prune clean-gradle reset-local test-product run-product-local rebuild-product-local \
    test-supplier run-supplier-local rebuild-supplier-local test-tax run-tax-local rebuild-tax-local \
    test-purchase run-purchase-local rebuild-purchase-local \
    test-auth run-auth-local rebuild-auth-local auth-db-reset \
    up-db reset-db tax-db-reset diagnose check-ports \
    sync-podman-desktop debug-discovery fix-discovery check-discovery-health check-resources \
    service-logs check-container-status emergency-clean restart-podman-vm fix-discovery-chmod \
    diagnose-chmod build-images-all-nocache fix-line-endings podman-auth