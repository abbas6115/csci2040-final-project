#!/bin/bash

APP_NAME="Filmbase"
# Update this path if your JAR name differs in pom.xml
JAR_PATH="target/csci2040-final-project-1.0-SNAPSHOT.jar"

echo "------------------------------------------"
echo "  $APP_NAME Build System"
echo "------------------------------------------"

# Helper function to launch the app
launch() {
    echo "[LAUNCH] Starting $APP_NAME..."
    ./mvnw spring-boot:run
}

# 1. Handle "run" logic separately
if [ "$1" == "run" ]; then
    if [ -f "$JAR_PATH" ]; then
        echo "[INFO] Build found. Skipping compilation..."
        launch
        exit 0
    else
        echo "[INFO] No build found. Triggering full build with TESTS..."
        set -- "test" # Redirects "run" to "test" logic below
    fi
fi

# 2. Handle standard commands
case "$1" in
    "test")
        echo "[MODE] Full Build WITH Unit Tests"
        ./mvnw clean package -Pproduction
        ;;
    "notest")
        echo "[MODE] Fast Build (Skipping Tests)"
        ./mvnw clean package -Pproduction -DskipTests
        ;;
    "clean")
        echo "[MODE] Cleaning project..."
        ./mvnw clean
        exit 0
        ;;
    *)
        echo "ERROR: Invalid or missing command."
        echo "Usage: ./build.sh [test | notest | run | clean]"
        exit 1
        ;;
esac

# 3. Final execution check
if [ $? -eq 0 ]; then
    echo "------------------------------------------"
    echo "Build Successful."

    # If the user started with 'run' (or was redirected there), launch now
    if [ "$1" == "test" ] || [ "$1" == "notest" ]; then
        launch
    fi
else
    echo "------------------------------------------"
    echo "Build FAILED. Check the logs above."
    exit 1
fi