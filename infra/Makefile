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

# ✅ LISTA ORDENADA DE SERVICIOS DE NEGOCIO (Para el contador de progreso de despliegue)
BUSINESS_SERVICES := product-service auth-service supplier-service tax-service purchase-service

# ✅ LISTA PARA HEALTH CHECK (Nombre:Puerto)
HEALTH_CHECK_LIST := Product:$(PRODUCT_PORT) Supplier:$(SUPPLIER_PORT) Tax:$(TAX_PORT) Purchase:$(PURCHASE_PORT) Auth:$(AUTH_PORT) Gateway:$(API_GATEWAY_PORT)

# ✅ SOLUCIÓN PARA MAC: Detectar el socket de Podman automáticamente
export DOCKER_HOST ?= unix://$(shell podman machine inspect --format '{{.ConnectionInfo.PodmanSocket.Path}}')

# Archivos de entorno
ENV_FILE_LOCAL   			:= $(abspath $(CURDIR)/.env.local)
ENV_FILE_SECURE  			:= $(abspath $(CURDIR)/.env)
ENV_FILE_DEV                := $(abspath $(CURDIR)/.env.dev)
ENV_FILE_PROD               := $(abspath $(CURDIR)/.env.prod)
ENV_FILE_LOCAL_SUPPLIER     := $(abspath $(CURDIR)/.env.local-supplier)

# Resolución de archivos de entorno
ENV_SUPPLIER_FALLBACK ?= $(ENV_FILE_LOCAL)
ENV_FILE_LOCAL_SUPPLIER_RESOLVED := $(if $(wildcard $(ENV_FILE_LOCAL_SUPPLIER)),$(ENV_FILE_LOCAL_SUPPLIER),$(ENV_SUPPLIER_FALLBACK))

COMPOSE_PROVIDER ?= podman
export DOCKER_BUILDKIT := 0
export COMPOSE_DOCKER_CLI_BUILD ?= 0

# --- Carga de variables de entorno para Make ---
# Intentamos cargar .env.local si existe, si no, .env
ifneq ("$(wildcard $(ENV_FILE_LOCAL))","")
    include $(ENV_FILE_LOCAL)
    export $(shell sed 's/=.*//' $(ENV_FILE_LOCAL))
endif

ifneq ("$(wildcard $(ENV_FILE_SECURE))","")
    include $(ENV_FILE_SECURE)
    export $(shell sed 's/=.*//' $(ENV_FILE_SECURE))
endif

# Puertos de servicios (con valores por defecto si no se cargaron del env)
API_GATEWAY_PORT ?= 8090
CONFIG_PORT      ?= 7777
AUTH_PORT        ?= 8080
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

export DOCKERFILE_NAME

# Comandos base
RUN := COMPOSE_PROVIDER=$(COMPOSE_PROVIDER) COMPOSE_PROJECT_NAME=$(COMPOSE_PROJECT_NAME)
COMPOSE_BASE := $(RUN) podman compose -f $(COMPOSE_FILE)

# Comandos específicos por entorno
# Usamos el archivo .env seguro por defecto para docker-compose
COMPOSE_LOCAL := $(COMPOSE_BASE) --env-file $(ENV_FILE_SECURE)
COMPOSE_LOCAL_SUPPLIER := $(COMPOSE_BASE) --env-file $(ENV_FILE_LOCAL_SUPPLIER_RESOLVED)

# Extraer credenciales de Docker de forma segura (silenciando errores si no existen)
D_USER := $(shell grep "^DOCKER_USERNAME=" $(ENV_FILE_LOCAL) 2>/dev/null | cut -d'=' -f2 | tr -d '\r' | xargs)
D_PASS := $(shell grep "^DOCKER_PASSWORD=" $(ENV_FILE_LOCAL) 2>/dev/null | cut -d'=' -f2 | tr -d '\r' | xargs)

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
	echo "   🔍 [$(3)/$(4)] Verificando estado de $(2)..."; \
	retry_count=0; \
	max_retries=5; \
	until [ "$$(podman inspect --format='{{.State.Status}}' $(COMPOSE_PROJECT_NAME)-$(2)-service-1 2>/dev/null)" = "running" ] || \
	      [ "$$(podman inspect --format='{{.State.Status}}' $(2)-service 2>/dev/null)" = "running" ]; do \
		retry_count=$$((retry_count + 1)); \
		if [ $$retry_count -ge $$max_retries ]; then \
			echo "   ⚠️ $(2) no parece estar corriendo tras $$max_retries intentos."; \
			break; \
		fi; \
		echo "   ⏳ $(2) esperando contenedor... (intento $$retry_count/$$max_retries)"; \
		sleep 2; \
	done; \
	echo "   ✅ Microservicio [$(2)] validado y listo!"
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


#🎯 COMANDOS PRINCIPALES
#------------------------------------------------------------------------
start: up-local ## 🚀 Iniciar servicios base
stop: down-all ## 🛑 Detener todos los servicios
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

#🔐 AUTENTICACIÓN
podman-auth: ## 🔑 Autenticar y asegurar imagen base
	@mkdir -p $(BUILD_DIR)
	@# Eliminamos la creación incondicional del archivo vacío que causaba el error JSON
	@echo "==========================================================================================================================================================================="
	@echo "🔑 Configurando autenticación automática para Podman..."
	@echo "==========================================================================================================================================================================="
	-@podman logout docker.io > /dev/null 2>&1
	@if [ -n "$(D_USER)" ] && [ -n "$(D_PASS)" ]; then \
		echo "Iniciando sesión en docker.io como $(D_USER)..."; \
		echo "$(D_PASS)" | podman login docker.io -u "$(D_USER)" --password-stdin --authfile $(AUTH_FILE) || echo "⚠️ Login fallido (continuando sin login)..."; \
	else \
		echo "⚠️ No se encontraron credenciales en .env.local. Saltando login."; \
	fi
	@echo "📥 Asegurando imagen base Java 21..."
	@# Usamos --authfile solo si el archivo existe
	@if [ -f "$(AUTH_FILE)" ]; then \
		podman pull eclipse-temurin:21-jdk-jammy --authfile $(AUTH_FILE) || echo "⚠️ Error descarga imagen base (puede que ya exista)"; \
	else \
		podman pull eclipse-temurin:21-jdk-jammy || echo "⚠️ Error descarga imagen base (puede que ya exista)"; \
	fi
	@echo "✅ Imagen base Java 21 lista."

docker-login:
	@echo "🔑 Forzando login de Podman/Docker con credenciales de .env.local..."
	@if [ -z "$(D_USER)" ] || [ -z "$(D_PASS)" ]; then \
		echo "🔴 ERROR: DOCKER_USERNAME o DOCKER_PASSWORD no definidos en .env.local."; \
		exit 1; \
	fi
	# Usa pipe para pasar la password. Esto es menos seguro, pero es la única manera de automatizar sin prompt.
	echo "$(D_PASS)" | podman login -u "$(D_USER)" --password-stdin docker.io
	@echo "✅ Login completado."

#🚀 DESPLIEGUE COMPLETO
up-local-all-ordered: podman-auth podman-ready fix-line-endings
	@$(MAKE) clean-env
	@date +%s > .start_time # <-- MOVIDO: Se crea DESPUÉS de limpiar
	@echo "======================================================================================"
	@echo "🚀 INICIANDO DESPLIEGUE (ESTRATEGIA ANTI-COLAPSO)"
	@echo "======================================================================================"

	@echo "🔨 Compilando JARs..."
	@$(MAKE) build-jars-all

	$(eval CMD := $(DOCKER_COMPOSE_BIN) --env-file $(abspath $(ENV_FILE_LOCAL)) -f $(abspath $(COMPOSE_FILE)) -p $(COMPOSE_PROJECT_NAME) --profile infra --profile product --profile supplier --profile tax --profile purchase --profile gateway --profile auth)

	@echo "🏗️  1. Infraestructura..."
	@$(CMD) up -d --build discovery-service config-service
	@sleep 10
	$(call check_health_url,http://localhost:$(DISCOVERY_PORT)/actuator/health,Discovery)

	@echo "🗄️  2. Bases de Datos..."
	@$(CMD) up -d db_product db_supplier db_tax db_purchase db_auth
	@sleep 15

	@echo "🚀 3. Servicios de Negocio..."
	@total=$(words $(BUSINESS_SERVICES)); \
	count=1; \
	for service in $(BUSINESS_SERVICES); do \
	   printf "   ⏳ [%d/%d] Iniciando %s...\n" $$count $$total $$service; \
	   $(CMD) up -d --build $$service; \
	   sleep 5; \
	   count=$$((count + 1)); \
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
	@printf "| %-20s | %-12s | %-10s | %-24s |\n" "SERVICIO" "ESTADO" "PUERTO" "HEALTH"

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

up-local: podman-ready fix-line-endings fix-container-orphans ## 🧪 Infra + Product + Gateway
	@echo "🧪 Levantando: Infra + Product + Gateway"
	$(Q) cp $(ENV_FILE_LOCAL) .env.override
	$(Q) $(MAKE) build-jars
	$(Q) $(COMPOSE_LOCAL) --profile infra --profile product --profile gateway up -d --build --remove-orphans

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

#🧹 LIMPIEZA
clean-env:
	@echo "======================================================================"
	@echo "🧹 LIMPIANDO ENTORNO Y VOLÚMENES (Full Reset)"
	@echo "======================================================================"
	# Detener con perfiles y borrar volúmenes (-v)
	-@$(DOCKER_COMPOSE_BIN) -f $(COMPOSE_FILE) -p $(COMPOSE_PROJECT_NAME) --profile infra --profile product --profile supplier --profile tax --profile purchase --profile gateway --profile auth down -v --remove-orphans 2>/dev/null || true
	@echo "🧨 Eliminando contenedores y redes residuales..."
	-@podman rm -f $$(podman ps -aq --filter "name=$(COMPOSE_PROJECT_NAME)") 2>/dev/null || true
	-@podman network rm inventory-net 2>/dev/null || true
	@echo "💾 Purgando volúmenes de sistema..."
	-@podman volume prune -f 2>/dev/null || true
	@rm -f .start_time 2>/dev/null || true
	@echo "✅ Entorno 100% limpio."

clean-all: podman-ready fix-line-endings ## 🧨 Limpieza completa
	@echo "🛑 Deteniendo servicios..."
	@$(DOCKER_COMPOSE_BIN) -p $(COMPOSE_PROJECT_NAME) down --remove-orphans
	@echo "🧹 Limpiando servicios específicos..."
	@podman rm -f auth-service db_auth 2>/dev/null || true
	@echo "🌐 Verificando red..."
	@podman network rm $(NETWORK_NAME) 2>/dev/null || true
	@rm -f .start_time
	@echo "✨ Sistema listo para nuevo despliegue."

clean-podman-prune: podman-ready ## 🧹 Limpiar recursos de Podman
	$(Q) rm -rf $(BUILD_DIR) 2>/dev/null || true
	$(Q) podman system prune -a -f --volumes

clean-gradle: ## 🧹 Limpiar caché de Gradle
	$(Q) cd $(ROOT_DIR) && $(GRADLEW) clean

reset-local: clean-all up-local-all-ordered ## 🔄 Reinicio completo

#🛠️ UTILIDADES
fix-line-endings:
	@echo "🛠️ Corrigiendo terminaciones de línea..."
	$(Q) dos2unix $(GRADLEW) 2>/dev/null || true

podman-ready:
	@echo "⚙️ Verificando Podman..."
	@podman machine ls >/dev/null 2>&1 || { echo "🚨 ERROR: Podman machine no responde."; exit 1; }

health-check-extended: podman-ready
	@echo "🩺 Verificación de salud EXTENDIDA..."
	@echo "🔍 Infraestructura:"
	$(call check_health,$(DISCOVERY_PORT),Discovery,1,2)
	$(call check_health,$(CONFIG_PORT),Config,2,2)

	@echo "🔍 Bases de datos (pg_isready):"
	-@podman exec db_product pg_isready -U postgres >/dev/null 2>&1 && echo "   ✅ Product DB" || echo "   ❌ Product DB"
	-@podman exec db_supplier pg_isready -U postgres >/dev/null 2>&1 && echo "   ✅ Supplier DB" || echo "   ❌ Supplier DB"
	-@podman exec db_tax pg_isready -U postgres >/dev/null 2>&1 && echo "   ✅ Tax DB" || echo "   ❌ Tax DB"
	-@podman exec db_purchase pg_isready -U postgres >/dev/null 2>&1 && echo "   ✅ Purchase DB" || echo "   ❌ Purchase DB"
	-@podman exec db_auth pg_isready -U postgres >/dev/null 2>&1 && echo "   ✅ Auth DB" || echo "   ❌ Auth DB"

	@echo "🔍 Microservicios (Health Actuator):"
	@total=$(words $(HEALTH_CHECK_LIST)); \
	count=1; \
	for item in $(HEALTH_CHECK_LIST); do \
		NAME=$$(echo $$item | cut -d':' -f1); \
		PORT=$$(echo $$item | cut -d':' -f2); \
		$(call check_health,$$PORT,$$NAME,$$count,$$total); \
		count=$$((count + 1)); \
	done

down-all: podman-ready
	@echo "🧹 Deteniendo todos los perfiles..."
	@$(COMPOSE_LOCAL) --profile infra --profile product --profile supplier --profile tax --profile purchase --profile gateway --profile auth down -v --remove-orphans || true
	@podman network rm inventory-net 2>/dev/null || true

# 1. Levanta solo lo que no cambia (Bases de datos + Discovery + Config)
up-infra: podman-ready
	@echo "🚀 Levantando infraestructura base..."
	$(COMPOSE_BASE) up -d db_product db_supplier db_tax db_purchase discovery-service config-service
	@echo "⏳ Esperando 15s para estabilización..."
	@sleep 15
	$(COMPOSE_BASE) ps

# 2. Levanta un MS específico instantáneamente
up-ms:
	$(DOCKER_COMPOSE) up -d $(s)

# 3. Re-compila y levanta solo UN microservicio
dev-ms:
	./gradlew :$(s):build -x test
	$(DOCKER_COMPOSE) up -d --build $(s)

logs: podman-ready ## 📜 Logs de todos los servicios
	$(COMPOSE_LOCAL) logs -f

health: health-check-extended ## 💚 Verificación de salud completa

status: podman-ready ## 📊 Estado de todos los servicios
	$(COMPOSE_LOCAL) ps
status-all: status ## 📊 Alias para status

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
