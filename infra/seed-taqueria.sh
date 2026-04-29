#!/bin/bash
# seed-taqueria.sh - Carga inicial de datos para la Taquería

BASE_URL="http://localhost:8081/api/v1"
PRODUCT_BASE_URL="http://localhost:9090/api/v1"
ADMIN_USERNAME="admin"
ADMIN_EMAIL="admin@taqueria.com"
ADMIN_PASSWORD="password123"

echo "1. 👤 Intentando registrar usuario Admin..."
REGISTER_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/register" \
     -H "Content-Type: application/json" \
     -d '{
       "username": "'"$ADMIN_USERNAME"'",
       "email": "'"$ADMIN_EMAIL"'",
       "password": "'"$ADMIN_PASSWORD"'",
       "role": ["admin"]
     }')

# Verificar si el registro fue exitoso o si el usuario ya existe
if echo "$REGISTER_RESPONSE" | grep -q "User registered successfully!"; then
    echo "✅ Usuario Admin registrado."
elif echo "$REGISTER_RESPONSE" | grep -q "User already exists"; then
    echo "⚠️ Usuario Admin ya existe."
else
    echo "❌ Error al registrar usuario Admin: $REGISTER_RESPONSE"
    # No salimos, intentamos login por si ya existía
fi

echo -e "\n2. 🔑 Intentando Login..."
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
     -H "Content-Type: application/json" \
     -d '{
       "username": "'"$ADMIN_USERNAME"'",
       "password": "'"$ADMIN_PASSWORD"'"
     }')

echo "Respuesta del servidor (Login): $LOGIN_RESPONSE"

# Extraemos el token
TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.data.token // .data.accessToken')

if [ "$TOKEN" == "null" ] || [ -z "$TOKEN" ]; then
    echo "❌ Error: No se pudo obtener el token. Verifica las credenciales o el estado del Auth Service."
    exit 1
fi

echo -e "\n✅ TOKEN OBTENIDO: ${TOKEN:0:20}..."

echo -e "\n3. 🛒 Verificando estado de PRODUCT-SERVICE..."
PRODUCT_HEALTH=$(curl -s -f http://localhost:9090/actuator/health)
if echo "$PRODUCT_HEALTH" | grep -q "UP"; then
    echo "✅ PRODUCT-SERVICE: UP"
else
    echo "❌ PRODUCT-SERVICE: DOWN - $PRODUCT_HEALTH"
fi

echo -e "\n4. 💸 Verificando estado de SALES-SERVICE..."
SALES_HEALTH=$(curl -s -f http://localhost:8086/actuator/health)
if echo "$SALES_HEALTH" | grep -q "UP"; then
    echo "✅ SALES-SERVICE: UP"
else
    echo "❌ SALES-SERVICE: DOWN - $SALES_HEALTH"
fi

echo -e "\n5. ➕ Creando Categoría (Tacos)..."
CATEGORY_RESPONSE=$(curl -s -X POST "$PRODUCT_BASE_URL/categories" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name": "Tacos", "description": "Todos los tipos de tacos"}')

echo "Respuesta del servidor (Categoría): $CATEGORY_RESPONSE"
CATEGORY_ID=$(echo "$CATEGORY_RESPONSE" | jq -r '.data.id // empty')

if [ -z "$CATEGORY_ID" ]; then
    echo "❌ Error: No se pudo crear la categoría o el ID es nulo."
    exit 1
fi
echo "✅ Categoría Creada ID: $CATEGORY_ID"

echo -e "\n6. 🌮 Creando Producto (Taco al Pastor) con Stock Inicial de 100..."
PRODUCT_RESPONSE=$(curl -s -X POST "$PRODUCT_BASE_URL/products" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Taco al Pastor",
    "code": "TACO-001",
    "categoryId": "'"$CATEGORY_ID"'",
    "unitPrice": 15.50,
    "initialStock": 100
  }')

echo "Respuesta del servidor (Producto): $PRODUCT_RESPONSE"
PRODUCT_ID=$(echo "$PRODUCT_RESPONSE" | jq -r '.data.id // empty')

if [ -z "$PRODUCT_ID" ]; then
    echo "❌ Error: No se pudo crear el producto o el ID es nulo."
    exit 1
fi
echo "✅ Producto Creado ID: $PRODUCT_ID"

echo -e "\nUsa este Token para tus pruebas:"
echo "Authorization: Bearer $TOKEN"
