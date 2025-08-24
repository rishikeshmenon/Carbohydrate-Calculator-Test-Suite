@echo off
echo ========================================
echo Carbohydrate Calculator Test Suite Setup
echo ========================================
echo.

:: Check if Java is installed
echo Checking Java installation...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Java not found. Installing Java JDK 17...
    echo.
    
    :: Install Java using winget
    winget install --id Oracle.JDK.17 --accept-source-agreements --accept-package-agreements
    
    if %errorlevel% neq 0 (
        echo Failed to install Java. Please install manually from: https://adoptium.net/
        pause
        exit /b 1
    )
    
    echo Java installed successfully!
    echo Please restart your terminal and run this script again.
    echo.
    pause
    exit /b 0
) else (
    echo Java is already installed.
    java -version
)

echo.
echo Checking Chrome browser...
reg query "HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\App Paths\chrome.exe" >nul 2>&1
if %errorlevel% neq 0 (
    echo Chrome browser not found. Please install Chrome from: https://www.google.com/chrome/
    pause
    exit /b 1
) else (
    echo Chrome browser found.
)

echo.
echo Setting up ChromeDriver...
if not exist "drivers" mkdir drivers

:: Get Chrome version and download matching ChromeDriver
echo Getting Chrome version...
for /f "tokens=3" %%i in ('reg query "HKEY_CURRENT_USER\Software\Google\Chrome\BLBeacon" /v version 2^>nul ^| findstr /i version') do set CHROME_VERSION=%%i
if "%CHROME_VERSION%"=="" (
    echo Could not determine Chrome version. Using latest stable version.
    set CHROME_VERSION=119.0.6045.105
)

echo Chrome version: %CHROME_VERSION%
echo Downloading ChromeDriver...

:: Extract major version for ChromeDriver
for /f "tokens=1 delims=." %%a in ("%CHROME_VERSION%") do set MAJOR_VERSION=%%a

:: Download ChromeDriver
powershell -Command "& {Invoke-WebRequest -Uri 'https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/%CHROME_VERSION%/win64/chromedriver-win64.zip' -OutFile 'drivers\chromedriver-win64.zip' -UseBasicParsing}"

if exist "drivers\chromedriver-win64.zip" (
    echo Extracting ChromeDriver...
    powershell -Command "& {Expand-Archive -Path 'drivers\chromedriver-win64.zip' -DestinationPath 'drivers\' -Force}"
    
    if exist "drivers\chromedriver-win64\chromedriver.exe" (
        move "drivers\chromedriver-win64\chromedriver.exe" "drivers\"
        rmdir /s /q "drivers\chromedriver-win64"
        del "drivers\chromedriver-win64.zip"
        echo ChromeDriver setup complete!
    ) else (
        echo Failed to extract ChromeDriver.
        pause
        exit /b 1
    )
) else (
    echo Failed to download ChromeDriver.
    pause
    exit /b 1
)

echo.
echo ========================================
echo Setup Complete!
echo ========================================
echo.
echo To run the tests, use:
echo   scripts\run-tests-windows.bat
echo.
echo Or manually:
echo   javac -cp "lib\*" -d . src\CarbohydrateCalculatorAutomation.java
echo   java -cp ".;lib\*" CarbohydrateCalculatorAutomation
echo.
pause
