up-local:
	docker compose --env-file .env.local up --build

up-dev:
	docker compose --env-file .env.dev up --build

up-prod:
	docker compose --env-file .env.prod up --buiup-local:
	cp .env.local .env
	docker compose up --build
