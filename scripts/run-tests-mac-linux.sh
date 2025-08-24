#!/bin/bash

echo "========================================"
echo "Running Carbohydrate Calculator Tests"
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

# Check if Java is available
if ! command -v java &> /dev/null; then
    print_error "Java not found. Please run setup-mac-linux.sh first."
    exit 1
fi

# Check if ChromeDriver exists
if [[ ! -f "drivers/chromedriver" ]]; then
    print_error "ChromeDriver not found. Please run setup-mac-linux.sh first."
    exit 1
fi

# Make sure ChromeDriver is executable
chmod +x drivers/chromedriver

print_status "Compiling Java source code..."
if javac -cp "lib/*" -d . src/CarbohydrateCalculatorAutomation.java; then
    print_status "Compilation successful!"
else
    print_error "Compilation failed!"
    exit 1
fi

echo
print_status "Running test suite..."
echo

# Run the tests
if java -cp ".:lib/*" CarbohydrateCalculatorAutomation; then
    echo
    print_status "Test execution completed successfully!"
else
    echo
    print_warning "Test execution completed with errors."
fi

echo
echo "Press Enter to exit..."
read
