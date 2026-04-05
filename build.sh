#!/bin/bash

APP_NAME="Filmbase"

echo "------------------------------------------"
echo "  $APP_NAME Build System"
echo "------------------------------------------"

# Check the arguments
if [ -z "$1" ]; then
    echo "------------------------------------------"
    echo "ERROR: No command provided."
    echo "Usage: ./build.sh [test | notest]"
    exit 1

elif [ "$1" == "test" ]; then
    echo "[MODE] Full Build WITH Unit Tests"
    ./mvnw clean package -Pproduction

elif [ "$1" == "notest" ]; then
    echo "[MODE] Fast Build (Skipping Tests)"
    echo "------------------------------------------"
    ./mvnw clean package -Pproduction -DskipTests

else
    echo "------------------------------------------"
    echo "ERROR: '$1' is not a valid command."
    echo "Usage: ./build.sh [test | notest]"
    exit 1
fi

# Only run the app if the compilation above was successful
if [ $? -eq 0 ]; then
    echo "Build Successful. Launching..."
    ./mvnw spring-boot:run
else
    echo "Build FAILED. Check the logs above."
    exit 1
fi