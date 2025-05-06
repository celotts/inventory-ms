up-local:
	docker compose down -v
	docker compose --env-file .env.local up --build

up-dev:
	docker compose down -v
	docker compose --env-file .env.dev up --build

up-prod:
	docker compose down -v
	docker compose --env-file .env.prod up --build
	cp .env.local .env
	docker compose up --build

build-images:
	docker build -t inventory/discovery-service ../discovery-service
	docker build -t inventory/config-service ../config-service
	docker build -t inventory/product-service ../product-service

