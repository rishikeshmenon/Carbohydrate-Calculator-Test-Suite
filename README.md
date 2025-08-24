# Carbohydrate Calculator Test Automation

Automated test suite for the carbohydrate calculator website using Selenium WebDriver and Java. This project includes a comprehensive test suite of 26 test cases, with 6 automated end-to-end scenarios demonstrating advanced testing capabilities covering multiple user scenarios and validation workflows.

## Getting Started

### Prerequisites
- Java 8 or higher
- Chrome browser

### Installation and Setup

1. Clone the repository:
```bash
git clone <repository-url>
cd <directory here>
```

2. Install dependencies and setup ChromeDriver:
```bash
chmod +x setup.sh
./setup.sh
```

3. Run the test automation:
```bash
chmod +x run_automation.sh
./run_automation.sh
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
Interview/
├── src/
│   └── CarbohydrateCalculatorAutomation.java
├── lib/
│   └── [Selenium JAR files]
├── drivers/
│   └── chromedriver
├── setup.sh
├── run_automation.sh
└── README.md
```

## Troubleshooting

- Ensure Chrome browser is installed and up to date
- Verify Java is properly installed with `java -version`
- Check that all dependencies are downloaded by running `./setup.sh`
- If tests fail, verify internet connectivity to access the calculator website
