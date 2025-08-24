#!/bin/bash

echo "=== Carbohydrate Calculator Automation Setup ==="
echo ""

# Check if Java is installed
echo "Checking Java installation..."
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo "Please install Java JDK 8 or higher and try again"
    echo ""
    echo "Installation instructions:"
    echo "  macOS: brew install openjdk@11"
    echo "  Ubuntu/Debian: sudo apt install openjdk-11-jdk"
    echo "  Windows: Download from https://adoptopenjdk.net/"
    exit 1
fi

echo "Java found: $(java -version 2>&1 | head -n 1)"

# Check if javac is available
if ! command -v javac &> /dev/null; then
    echo "ERROR: Java compiler (javac) not found"
    echo "Please install Java JDK (not just JRE) and try again"
    exit 1
fi

echo "Java compiler found: $(javac -version 2>&1)"
echo ""

# Create directories if they don't exist
echo "Creating project directories..."
mkdir -p drivers
mkdir -p lib

# Check if Selenium dependencies exist
echo "Checking Selenium dependencies..."
if [ ! -d "lib" ] || [ $(ls lib/*.jar 2>/dev/null | wc -l) -lt 10 ]; then
    echo "ERROR: Selenium dependencies missing from lib/ directory"
    echo "The lib/ directory should contain all required JAR files"
    echo "Please ensure all Selenium JAR files are present in the lib/ directory"
    exit 1
fi

echo "Found $(ls lib/*.jar | wc -l) JAR files in lib/ directory"
echo ""

# Download ChromeDriver if not present
if [ ! -f "drivers/chromedriver" ]; then
    echo "ChromeDriver not found. Attempting to download..."
    
    # Detect OS
    OS=""
    if [[ "$OSTYPE" == "linux-gnu"* ]]; then
        OS="linux64"
    elif [[ "$OSTYPE" == "darwin"* ]]; then
        OS="mac-x64"
        # Check for ARM64 Mac
        if [[ $(uname -m) == "arm64" ]]; then
            OS="mac-arm64"
        fi
    elif [[ "$OSTYPE" == "msys" ]] || [[ "$OSTYPE" == "win32" ]]; then
        OS="win32"
    else
        echo "ERROR: Unsupported operating system: $OSTYPE"
        echo "Please manually download ChromeDriver from https://chromedriver.chromium.org/"
        echo "Place it in the drivers/ directory and make it executable"
        exit 1
    fi
    
    echo "Detected OS: $OS"
    
    # Try to get Chrome version
    CHROME_VERSION=""
    if command -v google-chrome &> /dev/null; then
        CHROME_VERSION=$(google-chrome --version | grep -oP '\d+\.\d+\.\d+\.\d+' | head -1)
    elif command -v google-chrome-stable &> /dev/null; then
        CHROME_VERSION=$(google-chrome-stable --version | grep -oP '\d+\.\d+\.\d+\.\d+' | head -1)
    elif command -v chromium-browser &> /dev/null; then
        CHROME_VERSION=$(chromium-browser --version | grep -oP '\d+\.\d+\.\d+\.\d+' | head -1)
    elif [[ "$OSTYPE" == "darwin"* ]]; then
        # macOS Chrome location
        if [ -f "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome" ]; then
            CHROME_VERSION=$("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome" --version | grep -oE '[0-9]+\.[0-9]+\.[0-9]+\.[0-9]+')
        fi
    fi
    
    if [ -z "$CHROME_VERSION" ]; then
        echo "WARNING: Could not detect Chrome version automatically"
        echo "Please manually download ChromeDriver from:"
        echo "  https://chromedriver.chromium.org/"
        echo ""
        echo "1. Check your Chrome version: chrome://version/"
        echo "2. Download matching ChromeDriver"
        echo "3. Place as: drivers/chromedriver"
        echo "4. Make executable: chmod +x drivers/chromedriver"
        echo ""
        exit 1
    fi
    
    echo "Detected Chrome version: $CHROME_VERSION"
    
    # Extract major version
    MAJOR_VERSION=$(echo $CHROME_VERSION | cut -d. -f1)
    echo "Chrome major version: $MAJOR_VERSION"
    
    # Download ChromeDriver
    DOWNLOAD_URL="https://chromedriver.storage.googleapis.com/LATEST_RELEASE_$MAJOR_VERSION"
    echo "Getting latest ChromeDriver version for Chrome $MAJOR_VERSION..."
    
    if command -v curl &> /dev/null; then
        CHROMEDRIVER_VERSION=$(curl -s $DOWNLOAD_URL)
    elif command -v wget &> /dev/null; then
        CHROMEDRIVER_VERSION=$(wget -qO- $DOWNLOAD_URL)
    else
        echo "ERROR: Neither curl nor wget found"
        echo "Please install curl or wget, or manually download ChromeDriver"
        exit 1
    fi
    
    if [ -z "$CHROMEDRIVER_VERSION" ]; then
        echo "ERROR: Could not determine ChromeDriver version"
        echo "Please manually download from https://chromedriver.chromium.org/"
        exit 1
    fi
    
    echo "Downloading ChromeDriver version: $CHROMEDRIVER_VERSION"
    
    CHROMEDRIVER_URL="https://chromedriver.storage.googleapis.com/$CHROMEDRIVER_VERSION/chromedriver_$OS.zip"
    
    # Download ChromeDriver
    cd drivers
    if command -v curl &> /dev/null; then
        curl -L -o chromedriver.zip "$CHROMEDRIVER_URL"
    else
        wget -O chromedriver.zip "$CHROMEDRIVER_URL"
    fi
    
    if [ $? -ne 0 ]; then
        echo "ERROR: Failed to download ChromeDriver"
        echo "Please manually download from:"
        echo "  $CHROMEDRIVER_URL"
        cd ..
        exit 1
    fi
    
    # Extract ChromeDriver
    if command -v unzip &> /dev/null; then
        unzip -o chromedriver.zip
    else
        echo "ERROR: unzip not found"
        echo "Please install unzip or manually extract chromedriver.zip"
        cd ..
        exit 1
    fi
    
    # Clean up
    rm -f chromedriver.zip
    
    # Make executable
    chmod +x chromedriver
    
    cd ..
    
    echo "ChromeDriver downloaded and installed successfully"
else
    echo "ChromeDriver already exists"
fi

# Make chromedriver executable
chmod +x drivers/chromedriver

echo ""
echo "Making scripts executable..."
chmod +x *.sh

echo ""
echo "Verifying setup..."

# Test Java compilation
echo "Testing Java compilation..."
javac -cp "lib/*" -d . src/CarbohydrateCalculatorAutomation.java
if [ $? -eq 0 ]; then
    echo "Java compilation: SUCCESS"
    rm -f *.class
else
    echo "Java compilation: FAILED"
    echo "Please check that all JAR files are present in lib/ directory"
    exit 1
fi

# Verify ChromeDriver
echo "Testing ChromeDriver..."
if ./drivers/chromedriver --version &> /dev/null; then
    echo "ChromeDriver: SUCCESS ($(./drivers/chromedriver --version))"
else
    echo "ChromeDriver: FAILED"
    echo "Please ensure ChromeDriver is compatible with your Chrome version"
    exit 1
fi

echo ""
echo "=== Setup Complete ==="
echo ""
echo "✓ Java JDK installed and working"
echo "✓ Selenium dependencies verified"
echo "✓ ChromeDriver installed and executable"
echo "✓ Scripts are executable"
echo ""
echo "Ready to run tests! Execute: ./run_automation.sh"
echo ""