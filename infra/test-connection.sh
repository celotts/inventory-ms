#!/bin/bash
# test-connection.sh - Verificador real de conectividad

echo "======================================================"
echo " 🌮 VERIFICADOR DE TAQUERÍA (Paciencia por favor...)"
echo "======================================================"

check_service() {
    local name=$1
    local url=$2
    printf "🔍 Probando %-15s... " "$name"
    if curl -s -f "$url" > /dev/null; then
        echo "✅ OK"
    else
        echo "❌ No responde aún"
    fi
}

echo "PASO 1: Infraestructura"
check_service "Discovery" "http://localhost:8761"
check_service "Config" "http://localhost:7777/actuator/health"

echo -e "\nPASO 2: Negocio"
check_service "Auth" "http://localhost:8081/actuator/health"
check_service "Sales" "http://localhost:8086/actuator/health"
check_service "Product" "http://localhost:9090/actuator/health"
check_service "Gateway" "http://localhost:8090/actuator/health"

echo -e "\n======================================================"
echo "Si Sales está ✅ OK, ya puedes empezar a vender."
echo "======================================================"
