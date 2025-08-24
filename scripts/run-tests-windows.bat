@echo off
echo ========================================
echo Running Carbohydrate Calculator Tests
echo ========================================
echo.

:: Check if Java is available
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Java not found. Please run setup-windows.bat first.
    pause
    exit /b 1
)

:: Check if ChromeDriver exists
if not exist "drivers\chromedriver.exe" (
    echo ChromeDriver not found. Please run setup-windows.bat first.
    pause
    exit /b 1
)

echo Compiling Java source code...
javac -cp "lib\*" -d . src\CarbohydrateCalculatorAutomation.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Compilation successful!
echo.
echo Running test suite...
echo.

:: Run the tests
java -cp ".;lib\*" CarbohydrateCalculatorAutomation

if %errorlevel% neq 0 (
    echo.
    echo Test execution completed with errors.
) else (
    echo.
    echo Test execution completed successfully!
)

echo.
echo Press any key to exit...
pause >nul
