#!/bin/bash
# chmod +x run-local.sh run-dev.sh run-prod.sh
# ./run-local.sh
# ./run-dev.sh
# ./run-prod.sh
# ===== 로컬 모드 실행 =====
export SPRING_PROFILES_ACTIVE=local
echo "Running Spring Boot in LOCAL mode..."
../gradlew bootRun
