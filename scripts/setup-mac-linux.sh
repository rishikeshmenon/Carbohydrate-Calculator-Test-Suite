#!/bin/bash

echo "========================================"
echo "Carbohydrate Calculator Test Suite Setup"
echo "========================================"
echo

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if Java is installed
print_status "Checking Java installation..."
if command -v java &> /dev/null; then
    print_status "Java is already installed:"
    java -version
else
    print_warning "Java not found. Attempting to install..."
    
    # Detect OS and install Java
    if [[ "$OSTYPE" == "darwin"* ]]; then
        # macOS
        if command -v brew &> /dev/null; then
            print_status "Installing Java using Homebrew..."
            brew install openjdk@17
            if [ $? -eq 0 ]; then
                print_status "Java installed successfully!"
                print_warning "You may need to link Java: sudo ln -sfn /opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk"
            else
                print_error "Failed to install Java via Homebrew. Please install manually from: https://adoptium.net/"
                exit 1
            fi
        else
            print_error "Homebrew not found. Please install Homebrew first or install Java manually from: https://adoptium.net/"
            exit 1
        fi
    elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
        # Linux
        if command -v apt-get &> /dev/null; then
            print_status "Installing Java using apt..."
            sudo apt-get update
            sudo apt-get install -y openjdk-17-jdk
        elif command -v yum &> /dev/null; then
            print_status "Installing Java using yum..."
            sudo yum install -y java-17-openjdk-devel
        elif command -v dnf &> /dev/null; then
            print_status "Installing Java using dnf..."
            sudo dnf install -y java-17-openjdk-devel
        else
            print_error "Package manager not supported. Please install Java manually from: https://adoptium.net/"
            exit 1
        fi
        
        if [ $? -eq 0 ]; then
            print_status "Java installed successfully!"
        else
            print_error "Failed to install Java. Please install manually from: https://adoptium.net/"
            exit 1
        fi
    else
        print_error "Unsupported operating system. Please install Java manually from: https://adoptium.net/"
        exit 1
    fi
    
    print_warning "Please restart your terminal and run this script again."
    exit 0
fi

echo
print_status "Checking Chrome browser..."
if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS
    if [ -d "/Applications/Google Chrome.app" ]; then
        print_status "Chrome browser found."
    else
        print_error "Chrome browser not found. Please install Chrome from: https://www.google.com/chrome/"
        exit 1
    fi
elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
    # Linux
    if command -v google-chrome &> /dev/null || command -v chromium-browser &> /dev/null; then
        print_status "Chrome/Chromium browser found."
    else
        print_error "Chrome browser not found. Please install Chrome from: https://www.google.com/chrome/"
        exit 1
    fi
fi

echo
print_status "Setting up ChromeDriver..."
mkdir -p drivers

# Get Chrome version
if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS
    CHROME_VERSION=$(defaults read /Applications/Google\ Chrome.app/Contents/Info.plist CFBundleShortVersionString 2>/dev/null)
elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
    # Linux
    if command -v google-chrome &> /dev/null; then
        CHROME_VERSION=$(google-chrome --version | grep -oE "[0-9]+\.[0-9]+\.[0-9]+")
    elif command -v chromium-browser &> /dev/null; then
        CHROME_VERSION=$(chromium-browser --version | grep -oE "[0-9]+\.[0-9]+\.[0-9]+")
    fi
fi

if [ -z "$CHROME_VERSION" ]; then
    print_warning "Could not determine Chrome version. Using latest stable version."
    CHROME_VERSION="119.0.6045.105"
fi

print_status "Chrome version: $CHROME_VERSION"
print_status "Downloading ChromeDriver..."

# Download ChromeDriver
if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS - check if Intel or Apple Silicon
    if [[ $(uname -m) == "arm64" ]]; then
        ARCH="mac_arm64"
    else
        ARCH="mac64"
    fi
    CHROMEDRIVER_URL="https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/$CHROME_VERSION/$ARCH/chromedriver-$ARCH.zip"
else
    # Linux
    CHROMEDRIVER_URL="https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/$CHROME_VERSION/linux64/chromedriver-linux64.zip"
fi

# Download and extract ChromeDriver
if curl -L -o "drivers/chromedriver.zip" "$CHROMEDRIVER_URL"; then
    print_status "Extracting ChromeDriver..."
    cd drivers
    unzip -o chromedriver.zip
    rm chromedriver.zip
    
    # Make executable
    chmod +x chromedriver*
    
    # Move to drivers directory
    if [[ "$OSTYPE" == "darwin"* ]]; then
        mv chromedriver-* drivers/
        mv drivers/chromedriver drivers/
        rmdir drivers/drivers
    else
        mv chromedriver-* drivers/
        mv drivers/chromedriver drivers/
        rmdir drivers/drivers
    fi
    
    cd ..
    print_status "ChromeDriver setup complete!"
else
    print_error "Failed to download ChromeDriver."
    exit 1
fi

echo
echo "========================================"
echo "Setup Complete!"
echo "========================================"
echo
echo "To run the tests, use:"
echo "  ./scripts/run-tests-mac-linux.sh"
echo
echo "Or manually:"
echo "  javac -cp 'lib/*' -d . src/CarbohydrateCalculatorAutomation.java"
echo "  java -cp '.:lib/*' CarbohydrateCalculatorAutomation"
echo
