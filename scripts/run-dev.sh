#!/bin/bash
# chmod +x run-local.sh run-dev.sh run-prod.sh
# ./run-local.sh
# ./run-dev.sh
# ./run-prod.sh
# ===== 개발 모드 실행 =====
export SPRING_PROFILES_ACTIVE=dev
echo "Running Spring Boot in DEV mode..."
../gradlew bootRun
