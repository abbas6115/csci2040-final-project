#!/bin/bash

APP_NAME="Filmbase"
# Update this path if your JAR name differs in pom.xml
VERSION=$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout)
JAR_PATH="target/csci2040-final-project-$VERSION.jar"

REAL_SECRET_KEY=""

export API_KEY="$REAL_SECRET_KEY"

echo "------------------------------------------"
echo "  $APP_NAME Build System"
echo "------------------------------------------"

# function that launches the app
launch() {
    echo "[LAUNCH] Starting $APP_NAME..."
    # Check if JAR exists to run directly, otherwise use mvnw
    if [ -f "$JAR_PATH" ]; then
        java -jar "$JAR_PATH"
    else
        ./mvnw spring-boot:run
    fi
}

# run logic
# store command in variable to allow redirection if the JAR is missing
CMD="$1"

if [ "$CMD" == "run" ]; then
    if [ -f "$JAR_PATH" ]; then
        echo "[INFO] Build found. Skipping compilation..."
        launch
        exit 0
    else
        echo "[INFO] No build found. Triggering full build with TESTS..."
        CMD="test"
    fi
fi

# handles standard command logic
# test - cleans and packages with tests
# notest - cleans and packages without testing
# clean - cleans target directory
# run - runs the file
case "$CMD" in
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

# Final execution check
if [ $? -eq 0 ]; then
    echo "------------------------------------------"
    echo "Build Successful."

    # If the user started with 'run' (or was redirected there), launch now
    if [[ "$1" == "run" ]]; then
        launch
    fi
else
    echo "------------------------------------------"
    echo "Build FAILED. Check the logs above."
    exit 1
fi