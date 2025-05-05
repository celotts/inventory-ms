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

up-portainer: build-images
	@echo "🔐 Autenticando y desplegando stack en Portainer..."
	@PORTAINER_URL=$$(grep PORTAINER_URL .env.local | cut -d= -f2); \
	PORTAINER_USER=$$(grep PORTAINER_USER .env.local | cut -d= -f2); \
	PORTAINER_PASSWORD=$$(grep PORTAINER_PASSWORD .env.local | cut -d= -f2); \
	TOKEN=$$(curl -s -X POST "$$PORTAINER_URL/api/auth" \
		-H "Content-Type: application/json" \
		-d "{\"Username\": \"$$PORTAINER_USER\", \"Password\": \"$$PORTAINER_PASSWORD\"}" | jq -r .jwt); \
	if [ "$$TOKEN" = "null" ] || [ -z "$$TOKEN" ]; then \
		echo "❌ Error: Token inválido. Verifica las credenciales."; \
		exit 1; \
	fi; \
	ENDPOINT_ID=$$(curl -s -H "Authorization: Bearer $$TOKEN" "$$PORTAINER_URL/api/endpoints" | jq -r '.[] | select(.Name=="dind-remote") | .Id'); \
    if [ -z "$$ENDPOINT_ID" ]; then \
      echo "❌ No se pudo obtener el endpoint 'dind-remote'. Verifica que esté registrado en Portainer."; \
      exit 1; \
    fi; \
	STACK_ID=$$(curl -s -H "Authorization: Bearer $$TOKEN" "$$PORTAINER_URL/api/stacks" | jq '.[] | select(.Name=="inventory_ms") | .Id'); \
	STACK_CONTENT=$$(jq -Rs . < docker-compose.portainer.yml); \
	if [ -z "$$STACK_ID" ]; then \
		echo "📦 Stack no existe. Creando..."; \
		curl -s -X POST "$$PORTAINER_URL/api/stacks?type=1&method=string&endpointId=$$ENDPOINT_ID" \
			-H "Authorization: Bearer $$TOKEN" \
			-H "Content-Type: application/json" \
			-d "{\"Name\": \"inventory_ms\", \"StackFileContent\": $$STACK_CONTENT, \"Env\": [], \"Prune\": true}" > /dev/null; \
		echo "✅ Stack creado correctamente."; \
	else \
		echo "🔁 Stack ya existe. Actualizando..."; \
		curl -s -X PUT "$$PORTAINER_URL/api/stacks/$$STACK_ID/string" \
			-H "Authorization: Bearer $$TOKEN" \
			-H "Content-Type: application/json" \
			-d "{\"StackFileContent\": $$STACK_CONTENT, \"Env\": [], \"Prune\": true}" > /dev/null; \
		echo "✅ Stack actualizado correctamente."; \
	fi