#!/bin/bash

echo "=== Carbohydrate Calculator Test Automation ==="
echo ""

if [ ! -f "drivers/chromedriver" ]; then
    echo "ChromeDriver not found!"
    echo "Please download ChromeDriver from: https://chromedriver.chromium.org/"
    echo "Place it as: drivers/chromedriver"
    echo "Make it executable: chmod +x drivers/chromedriver"
    exit 1
fi

if [ ! -d "lib" ] || [ ! -f "lib/selenium-api-4.15.0.jar" ]; then
    echo "Selenium dependencies not found!"
    echo "Please ensure the lib/ directory contains all Selenium JAR files"
    exit 1
fi

echo "Prerequisites check passed"
echo ""

export PATH=$PATH:$(pwd)/drivers

echo "Compiling Java automation file..."
javac -cp "lib/*" -d . src/CarbohydrateCalculatorAutomation.java

if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

echo "Compilation successful"
echo ""

echo "Starting test automation..."
echo "Browser will open automatically"
echo ""

java -cp ".:lib/*" CarbohydrateCalculatorAutomation

echo ""
echo "Automation completed"
