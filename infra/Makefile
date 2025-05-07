# Variables
ENV_FILE_LOCAL ?= .env.local
ENV_FILE_DEV ?= .env.dev
ENV_FILE_PROD ?= .env.prod
# Levantar con archivo .env actual (puedes exportar ENV_FILE para cambiarlo)

up-local:
	cp $(ENV_FILE_LOCAL) .env
	docker compose down -v
	docker compose --env-file $(ENV_FILE_LOCAL) up --build

up-dev:
	cp $(ENV_FILE_DEV) .env
	docker compose down -v
	docker compose --env-file $(ENV_FILE_DEV) up --build

up-prod:
	cp $(ENV_FILE_PROD) .env
	docker compose down -v
	docker compose --env-file $(ENV_FILE_PROD) up --build


build-images:
	docker build -t inventory/discovery-service ../discovery-service
	docker build -t inventory/config-service ../config-service
	docker build -t inventory/product-service ../product-service


