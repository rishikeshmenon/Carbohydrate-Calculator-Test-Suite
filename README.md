# Carbohydrate Calculator Test Automation

Automated test suite for the carbohydrate calculator website using Selenium WebDriver and Java. This project includes a comprehensive test suite of 26 test cases, with 6 automated end-to-end scenarios demonstrating advanced testing capabilities covering multiple user scenarios and validation workflows.

## Prerequisites

- Java 8 or higher
- Chrome browser
- ChromeDriver (matching your Chrome version)

## Dependencies

All required JAR files are included in the lib/ directory:

- selenium-api-4.15.0.jar
- selenium-chrome-driver-4.15.0.jar
- selenium-chromium-driver-4.15.0.jar
- selenium-http-4.15.0.jar
- selenium-json-4.15.0.jar
- selenium-manager-4.15.0.jar
- selenium-os-4.15.0.jar
- selenium-remote-driver-4.15.0.jar
- selenium-support-4.15.0.jar
- byte-buddy-1.14.5.jar
- commons-exec-1.3.jar
- failsafe-3.3.2.jar
- gson-2.10.1.jar
- guava-32.1.2-jre.jar
- opentelemetry-api-1.28.0.jar
- opentelemetry-context-1.28.0.jar

## Setup

1. Clone the repository
2. Download ChromeDriver from https://chromedriver.chromium.org/ that matches your Chrome version
3. Place ChromeDriver in the drivers/ directory
4. Make sure Java JDK is installed

## Running the Tests

Compile and run the automation:

**Windows:**
```
javac -cp "lib\*" -d . src\CarbohydrateCalculatorAutomation.java
java -cp ".;lib\*" CarbohydrateCalculatorAutomation
```

**Mac/Linux:**
```
javac -cp "lib/*" -d . src/CarbohydrateCalculatorAutomation.java
java -cp ".:lib/*" CarbohydrateCalculatorAutomation
```

## Test Cases Summary

The automation suite includes 6 comprehensive end-to-end test scenarios:

### TC001: Multiple Calculation Sessions Workflow
Tests consecutive calculations with different user profiles to verify session management and form handling across multiple submissions without page refresh.

### TC002: Incorrect Values Validation and Recovery
Validates comprehensive error handling by testing various invalid inputs (non-numeric age, out-of-bounds values, negative weights) and verifies successful recovery with valid data.

### TC003: Activity Level Impact Comparison
Compares calculation results across different activity levels (sedentary vs very active) using identical user data to verify the impact of activity level on carbohydrate recommendations.

### TC004: Gender-Based Calculation Differences
Tests the same physical parameters with different gender selections to verify that the calculator applies appropriate gender-based formulas and produces different results.

### TC005: US Units Mode Comprehensive Workflow
Tests the complete workflow using US Imperial units (feet/inches for height, pounds for weight) and compares results with equivalent metric values to verify unit system functionality.

### TC006: Extreme Boundary Value Testing
Validates calculator behavior with extreme but valid inputs including maximum age (80), very tall height (220cm), high weight (150kg), and minimum values to test edge case handling.

## Sample Output

```
=== Carbohydrate Calculator Test Automation ===
Running 6 automated end-to-end test cases: TC001, TC002, TC003, TC004, TC005, TC006

WebDriver initialized successfully
--- TC001: Multiple Calculation Sessions Workflow ---
TC001 PASSED: Multiple calculation sessions work correctly
  Both calculations completed successfully with different profiles

--- TC002: Incorrect Values Validation and Recovery ---
TC002 PASSED: Incorrect values validation and recovery successful
  Invalid inputs detected and valid calculation completed

--- TC003: Activity Level Impact Comparison ---
TC003 PASSED: Activity level impact comparison successful
  Different activity levels produced different carb recommendations

--- TC004: Gender-Based Calculation Differences ---
TC004 PASSED: Gender-based calculation differences verified
  Male and female profiles produced different carb recommendations

--- TC005: US Units Mode Comprehensive Workflow ---
TC005 PASSED: US units mode comprehensive workflow successful
  Both US Imperial and Metric units calculations completed

--- TC006: Extreme Boundary Value Testing ---
TC006 PASSED: Extreme boundary value testing successful
  Both maximum and minimum boundary values handled correctly

Test Execution Summary
Total Tests: 6
Tests Passed: 6
Tests Failed: 0
Pass Rate: 100.0%
All tests passed successfully
=======================================
WebDriver closed successfully
```

## Technical Implementation

- **Language**: Java
- **Testing Framework**: Selenium WebDriver
- **Browser**: Chrome with ChromeDriver
- **Performance**: Optimized navigation reduces execution time
- **Reliability**: Robust element interaction with wait strategies

## Project Structure

```
Carbohydrate-Calculator-Test-Suite/
├── src/
│   └── CarbohydrateCalculatorAutomation.java
├── lib/
│   ├── selenium-api-4.15.0.jar
│   ├── selenium-chrome-driver-4.15.0.jar
│   ├── selenium-chromium-driver-4.15.0.jar
│   ├── selenium-http-4.15.0.jar
│   ├── selenium-json-4.15.0.jar
│   ├── selenium-manager-4.15.0.jar
│   ├── selenium-os-4.15.0.jar
│   ├── selenium-remote-driver-4.15.0.jar
│   ├── selenium-support-4.15.0.jar
│   ├── byte-buddy-1.14.5.jar
│   ├── commons-exec-1.3.jar
│   ├── failsafe-3.3.2.jar
│   ├── gson-2.10.1.jar
│   ├── guava-32.1.2-jre.jar
│   ├── opentelemetry-api-1.28.0.jar
│   └── opentelemetry-context-1.28.0.jar
├── drivers/
│   └── chromedriver
├── Test Document.xlsx
└── README.md
```

## Troubleshooting

- Ensure Chrome browser is installed and up to date
- Verify Java is properly installed with `java -version`
- Make sure ChromeDriver version matches your Chrome browser version
- If tests fail, verify internet connectivity to access the calculator website