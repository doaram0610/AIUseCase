#!/bin/bash
# chmod +x run-local.sh run-dev.sh run-prod.sh
# ./run-local.sh
# ./run-dev.sh
# ./run-prod.sh
# ===== 운영 모드 실행 =====
export SPRING_PROFILES_ACTIVE=prod
echo "Running Spring Boot in PROD mode..."
../gradlew bootRun