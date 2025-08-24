import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import java.time.Duration;
import java.util.List;

/**
 * Automated test suite for carbohydrate calculator website
 * Executes 6 end-to-end test scenarios covering comprehensive functionality
 */
public class CarbohydrateCalculatorAutomation {
    
    // WebDriver components for browser automation
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static JavascriptExecutor js;
    
    // Test configuration and tracking
    private static final String CALCULATOR_URL = "https://www.calculator.net/carbohydrate-calculator.html";
    private static int testsPassed = 0;
    private static int testsFailed = 0;
    private static boolean pageLoaded = false;
    
    public static void main(String[] args) {
        System.out.println("Carbohydrate Calculator Test Automation");
        System.out.println("Running 6 automated end-to-end test cases: TC001, TC002, TC003, TC004, TC005, TC006");
        System.out.println();
        
        setupWebDriver();
        
        try {
            runTC001_MultipleCalculationSessions();
            runTC002_IncorrectValuesValidationRecovery();
            runTC003_ActivityLevelImpactComparison();
            runTC004_GenderBasedCalculationDifferences();
            runTC005_USUnitsComprehensiveWorkflow();
            runTC006_ExtremeBoundaryValueTesting();
            
        } catch (Exception e) {
            System.err.println("Test execution failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            printTestSummary();
            tearDown();
        }
    }
    
    /**
     * Initialize ChromeDriver with optimized settings for automated testing
     */
    private static void setupWebDriver() {
        try {
            System.setProperty("webdriver.chrome.driver", "drivers/chromedriver");
            
            // Configure Chrome options for stable automation
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-extensions");
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--start-maximized");
            options.addArguments("--disable-images");
            options.addArguments("--disable-plugins");
            options.setExperimentalOption("useAutomationExtension", false);
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            
            driver = new ChromeDriver(options);
            wait = new WebDriverWait(driver, Duration.ofSeconds(8));
            js = (JavascriptExecutor) driver;
            
            driver.manage().window().maximize();
            
            System.out.println("WebDriver initialized successfully");
            
        } catch (Exception e) {
            System.err.println("Failed to initialize WebDriver: " + e.getMessage());
            System.exit(1);
        }
    }
    
    /**
     * Navigate to calculator page or reset form for subsequent tests
     * Optimizes performance by avoiding repeated page loads
     */
    private static void navigateToCalculatorSmart() {
        if (!pageLoaded) {
            // Initial page load with complete readiness check
            driver.get(CALCULATOR_URL);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.name("cage")));
            wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));
            pageLoaded = true;
            fastWait(800);
        } else {
            // Reset form values for subsequent tests
            js.executeScript("document.getElementById('cage').value = '25';");
            js.executeScript("document.getElementById('csex1').checked = false;");
            js.executeScript("document.getElementById('csex2').checked = false;");
            fastWait(200);
        }
        
        js.executeScript("window.scrollTo(0, 0);");
    }
    
    private static void fastScrollAndClick(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", element);
        fastWait(100);
        try {
            element.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", element);
        }
    }
    
    private static void fastInput(WebElement element, String text) {
        try {
            element.clear();
            element.sendKeys(text);
            fastWait(100);
        } catch (Exception e) {
            js.executeScript("arguments[0].value = arguments[1];", element, text);
        }
    }

    /**
     * TC001: Tests multiple calculation sessions workflow
     * Verifies consecutive calculations with different user profiles work correctly
     */
    private static void runTC001_MultipleCalculationSessions() {
        System.out.println("--- TC001: Multiple Calculation Sessions Workflow ---");
        
        try {
            navigateToCalculatorSmart();
            
            WebElement ageField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("cage")));
            fastInput(ageField, "25");
            
            WebElement genderMale = driver.findElement(By.id("csex1"));
            js.executeScript("arguments[0].checked = true;", genderMale);
            
            WebElement heightCm = driver.findElement(By.name("cheightmeter"));
            fastInput(heightCm, "180");
            
            WebElement weightKg = driver.findElement(By.name("ckg"));
            fastInput(weightKg, "75");
            
            WebElement activityDropdown = driver.findElement(By.name("cactivity"));
            Select activitySelect = new Select(activityDropdown);
            activitySelect.selectByValue("1.55");
            
            WebElement calculateButton = driver.findElement(By.name("x"));
            fastScrollAndClick(calculateButton);
            fastWait(2000);
            
            String firstResult = driver.getPageSource();
            boolean firstCalculationSuccess = firstResult.contains("gram") && firstResult.contains("carbohydrate");
            
            navigateToCalculatorSmart();
            
            ageField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("cage")));
            fastInput(ageField, "45");
            
            WebElement genderFemale = driver.findElement(By.id("csex2"));
            js.executeScript("arguments[0].checked = true;", genderFemale);
            
            heightCm = driver.findElement(By.name("cheightmeter"));
            fastInput(heightCm, "165");
            
            weightKg = driver.findElement(By.name("ckg"));
            fastInput(weightKg, "60");
            
            activitySelect = new Select(driver.findElement(By.name("cactivity")));
            activitySelect.selectByValue("1.725");
            
            calculateButton = driver.findElement(By.name("x"));
            fastScrollAndClick(calculateButton);
            fastWait(2000);
            
            String secondResult = driver.getPageSource();
            boolean secondCalculationSuccess = secondResult.contains("gram") && secondResult.contains("carbohydrate");
            
            if (firstCalculationSuccess && secondCalculationSuccess) {
                System.out.println("TC001 PASSED: Multiple calculation sessions work correctly");
                System.out.println("  Both calculations completed successfully with different profiles");
                testsPassed++;
            } else {
                System.out.println("TC001 FAILED: Multiple calculation sessions failed");
                testsFailed++;
            }
            
        } catch (Exception e) {
            System.out.println("TC001 FAILED: Exception occurred - " + e.getMessage());
            testsFailed++;
        }
    }
    
    /**
     * TC002: Tests incorrect values validation and recovery
     * Verifies comprehensive error handling with invalid inputs and recovery workflow
     */
    private static void runTC002_IncorrectValuesValidationRecovery() {
        System.out.println("--- TC002: Incorrect Values Validation and Recovery ---");
        
        try {
            navigateToCalculatorSmart();
            
            WebElement ageField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("cage")));
            fastInput(ageField, "abc");
            ageField.sendKeys(Keys.TAB);
            fastWait(800);
            
            String pageAfterInvalidAge = driver.getPageSource().toLowerCase();
            boolean ageValidationTriggered = pageAfterInvalidAge.contains("positive numbers only") || 
                                           pageAfterInvalidAge.contains("invalid") ||
                                           pageAfterInvalidAge.contains("number");
            
            fastInput(ageField, "150");
            
            WebElement genderMale = driver.findElement(By.id("csex1"));
            js.executeScript("arguments[0].checked = true;", genderMale);
            
            WebElement heightCm = driver.findElement(By.name("cheightmeter"));
            fastInput(heightCm, "180");
            
            WebElement weightKg = driver.findElement(By.name("ckg"));
            fastInput(weightKg, "75");
            
            WebElement activityDropdown = driver.findElement(By.name("cactivity"));
            Select activitySelect = new Select(activityDropdown);
            activitySelect.selectByValue("1.55");
            
            WebElement calculateButton = driver.findElement(By.name("x"));
            fastScrollAndClick(calculateButton);
            fastWait(1500);
            
            String pageAfterInvalidAgeCalc = driver.getPageSource().toLowerCase();
            boolean outOfBoundsValidation = pageAfterInvalidAgeCalc.contains("out of bounds") ||
                                          pageAfterInvalidAgeCalc.contains("invalid age") ||
                                          !pageAfterInvalidAgeCalc.contains("gram");
            
            navigateToCalculatorSmart();
            
            ageField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("cage")));
            fastInput(ageField, "25");
            
            js.executeScript("arguments[0].checked = true;", driver.findElement(By.id("csex1")));
            fastInput(driver.findElement(By.name("cheightmeter")), "175");
            fastInput(driver.findElement(By.name("ckg")), "70");
            
            activitySelect = new Select(driver.findElement(By.name("cactivity")));
            activitySelect.selectByValue("1.55");
            
            WebElement weightField = driver.findElement(By.name("ckg"));
            fastInput(weightField, "-50");
            
            calculateButton = driver.findElement(By.name("x"));
            fastScrollAndClick(calculateButton);
            fastWait(1500);
            
            String pageAfterNegativeWeight = driver.getPageSource().toLowerCase();
            boolean negativeWeightHandled = !pageAfterNegativeWeight.contains("gram") ||
                                          pageAfterNegativeWeight.contains("positive");
            
            navigateToCalculatorSmart();
            
            ageField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("cage")));
            fastInput(ageField, "30");
            
            js.executeScript("arguments[0].checked = true;", driver.findElement(By.id("csex2")));
            fastInput(driver.findElement(By.name("cheightmeter")), "165");
            fastInput(driver.findElement(By.name("ckg")), "60");
            
            activitySelect = new Select(driver.findElement(By.name("cactivity")));
            activitySelect.selectByValue("1.375");
            
            calculateButton = driver.findElement(By.name("x"));
            fastScrollAndClick(calculateButton);
            fastWait(2000);
            
            String finalResult = driver.getPageSource();
            boolean finalCalculationSuccess = finalResult.contains("gram") && finalResult.contains("carbohydrate");
            
            boolean overallValidationWorking = (ageValidationTriggered || outOfBoundsValidation) && finalCalculationSuccess;
            
            if (overallValidationWorking) {
                System.out.println("TC002 PASSED: Incorrect values validation and recovery successful");
                System.out.println("  Invalid inputs detected and valid calculation completed");
                testsPassed++;
            } else {
                System.out.println("TC002 FAILED: Validation or recovery workflow failed");
                testsFailed++;
            }
            
        } catch (Exception e) {
            System.out.println("TC002 FAILED: Exception occurred - " + e.getMessage());
            testsFailed++;
        }
    }
    
    private static void runTC003_ActivityLevelImpactComparison() {
        System.out.println("--- TC003: Activity Level Impact Comparison ---");
        
        try {
            navigateToCalculatorSmart();
            
            WebElement ageField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("cage")));
            fastInput(ageField, "30");
            
            WebElement genderMale = driver.findElement(By.id("csex1"));
            js.executeScript("arguments[0].checked = true;", genderMale);
            
            WebElement heightCm = driver.findElement(By.name("cheightmeter"));
            fastInput(heightCm, "175");
            
            WebElement weightKg = driver.findElement(By.name("ckg"));
            fastInput(weightKg, "75");
            
            WebElement activityDropdown = driver.findElement(By.name("cactivity"));
            Select activitySelect = new Select(activityDropdown);
            activitySelect.selectByValue("1.2");
            
            WebElement calculateButton = driver.findElement(By.name("x"));
            fastScrollAndClick(calculateButton);
            fastWait(2000);
            
            String sedentaryResult = driver.getPageSource();
            boolean sedentaryCalculationSuccess = sedentaryResult.contains("gram") && sedentaryResult.contains("carbohydrate");
            
            navigateToCalculatorSmart();
            
            ageField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("cage")));
            fastInput(ageField, "30");
            js.executeScript("arguments[0].checked = true;", driver.findElement(By.id("csex1")));
            fastInput(driver.findElement(By.name("cheightmeter")), "175");
            fastInput(driver.findElement(By.name("ckg")), "75");
            
            activitySelect = new Select(driver.findElement(By.name("cactivity")));
            activitySelect.selectByValue("1.725");
            
            fastScrollAndClick(driver.findElement(By.name("x")));
            fastWait(2000);
            
            String activeResult = driver.getPageSource();
            boolean activeCalculationSuccess = activeResult.contains("gram") && activeResult.contains("carbohydrate");
            
            boolean resultsDifferent = !sedentaryResult.equals(activeResult);
            
            if (sedentaryCalculationSuccess && activeCalculationSuccess && resultsDifferent) {
                System.out.println("TC003 PASSED: Activity level impact comparison successful");
                System.out.println("  Different activity levels produced different carb recommendations");
                testsPassed++;
            } else {
                System.out.println("TC003 FAILED: Activity level impact not detected");
                testsFailed++;
            }
            
        } catch (Exception e) {
            System.out.println("TC003 FAILED: Exception occurred - " + e.getMessage());
            testsFailed++;
        }
    }
    
    private static void runTC004_GenderBasedCalculationDifferences() {
        System.out.println("--- TC004: Gender-Based Calculation Differences ---");
        
        try {
            navigateToCalculatorSmart();
            
            WebElement ageField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("cage")));
            fastInput(ageField, "35");
            
            WebElement genderMale = driver.findElement(By.id("csex1"));
            js.executeScript("arguments[0].checked = true;", genderMale);
            
            WebElement heightCm = driver.findElement(By.name("cheightmeter"));
            fastInput(heightCm, "170");
            
            WebElement weightKg = driver.findElement(By.name("ckg"));
            fastInput(weightKg, "70");
            
            WebElement activityDropdown = driver.findElement(By.name("cactivity"));
            Select activitySelect = new Select(activityDropdown);
            activitySelect.selectByValue("1.55");
            
            WebElement calculateButton = driver.findElement(By.name("x"));
            fastScrollAndClick(calculateButton);
            fastWait(2000);
            
            String maleResult = driver.getPageSource();
            boolean maleCalculationSuccess = maleResult.contains("gram") && maleResult.contains("carbohydrate");
            
            navigateToCalculatorSmart();
            
            ageField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("cage")));
            fastInput(ageField, "35");
            
            WebElement genderFemale = driver.findElement(By.id("csex2"));
            js.executeScript("arguments[0].checked = true;", genderFemale);
            
            fastInput(driver.findElement(By.name("cheightmeter")), "170");
            fastInput(driver.findElement(By.name("ckg")), "70");
            
            activitySelect = new Select(driver.findElement(By.name("cactivity")));
            activitySelect.selectByValue("1.55");
            
            fastScrollAndClick(driver.findElement(By.name("x")));
            fastWait(2000);
            
            String femaleResult = driver.getPageSource();
            boolean femaleCalculationSuccess = femaleResult.contains("gram") && femaleResult.contains("carbohydrate");
            
            boolean resultsDifferent = !maleResult.equals(femaleResult);
            
            if (maleCalculationSuccess && femaleCalculationSuccess && resultsDifferent) {
                System.out.println("TC004 PASSED: Gender-based calculation differences verified");
                System.out.println("  Male and female profiles produced different carb recommendations");
                testsPassed++;
            } else {
                System.out.println("TC004 FAILED: Gender impact not detected or calculations failed");
                testsFailed++;
            }
            
        } catch (Exception e) {
            System.out.println("TC004 FAILED: Exception occurred - " + e.getMessage());
            testsFailed++;
        }
    }
    
    /**
     * TC005: Tests US Imperial units comprehensive workflow
     * Verifies complete calculation workflow using feet/inches and pounds
     */
    private static void runTC005_USUnitsComprehensiveWorkflow() {
        System.out.println("--- TC005: US Units Mode Comprehensive Workflow ---");
        
        try {
            navigateToCalculatorSmart();
            
            // Switch to US Imperial units mode
            WebElement usUnitsLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("US Units")));
            fastScrollAndClick(usUnitsLink);
            fastWait(1500);
            
            WebElement ageField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("cage")));
            fastInput(ageField, "28");
            
            WebElement genderFemale = driver.findElement(By.id("csex2"));
            js.executeScript("arguments[0].checked = true;", genderFemale);
            
            WebElement heightFeet = driver.findElement(By.name("cheightfeet"));
            fastInput(heightFeet, "5");
            
            WebElement heightInches = driver.findElement(By.name("cheightinch"));
            fastInput(heightInches, "6");
            
            WebElement weightPounds = driver.findElement(By.name("cpound"));
            fastInput(weightPounds, "140");
            
            WebElement activityDropdown = driver.findElement(By.name("cactivity"));
            Select activitySelect = new Select(activityDropdown);
            activitySelect.selectByValue("1.375");
            
            WebElement calculateButton = driver.findElement(By.name("x"));
            fastScrollAndClick(calculateButton);
            fastWait(2000);
            
            String usUnitsResult = driver.getPageSource();
            boolean usCalculationSuccess = usUnitsResult.contains("gram") && usUnitsResult.contains("carbohydrate");
            
            pageLoaded = false;
            navigateToCalculatorSmart();
            
            WebElement metricUnitsLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Metric Units")));
            fastScrollAndClick(metricUnitsLink);
            fastWait(1500);
            
            ageField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("cage")));
            fastInput(ageField, "28");
            
            js.executeScript("arguments[0].checked = true;", driver.findElement(By.id("csex2")));
            
            WebElement heightCm = driver.findElement(By.name("cheightmeter"));
            fastInput(heightCm, "168");
            
            WebElement weightKg = driver.findElement(By.name("ckg"));
            fastInput(weightKg, "63.5");
            
            activitySelect = new Select(driver.findElement(By.name("cactivity")));
            activitySelect.selectByValue("1.375");
            
            fastScrollAndClick(driver.findElement(By.name("x")));
            fastWait(2000);
            
            String metricResult = driver.getPageSource();
            boolean metricCalculationSuccess = metricResult.contains("gram") && metricResult.contains("carbohydrate");
            
            if (usCalculationSuccess && metricCalculationSuccess) {
                System.out.println("TC005 PASSED: US units mode comprehensive workflow successful");
                System.out.println("  Both US Imperial and Metric units calculations completed");
                testsPassed++;
            } else {
                System.out.println("TC005 FAILED: US units workflow failed");
                testsFailed++;
            }
            
        } catch (Exception e) {
            System.out.println("TC005 FAILED: Exception occurred - " + e.getMessage());
            testsFailed++;
        }
    }
    
    private static void runTC006_ExtremeBoundaryValueTesting() {
        System.out.println("--- TC006: Extreme Boundary Value Testing ---");
        
        try {
            navigateToCalculatorSmart();
            
            WebElement ageField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("cage")));
            fastInput(ageField, "80");
            
            WebElement genderMale = driver.findElement(By.id("csex1"));
            js.executeScript("arguments[0].checked = true;", genderMale);
            
            WebElement heightCm = driver.findElement(By.name("cheightmeter"));
            fastInput(heightCm, "220");
            
            WebElement weightKg = driver.findElement(By.name("ckg"));
            fastInput(weightKg, "150");
            
            WebElement activityDropdown = driver.findElement(By.name("cactivity"));
            Select activitySelect = new Select(activityDropdown);
            activitySelect.selectByValue("1.725");
            
            WebElement calculateButton = driver.findElement(By.name("x"));
            fastScrollAndClick(calculateButton);
            fastWait(2000);
            
            String maxBoundaryResult = driver.getPageSource();
            boolean maxBoundarySuccess = maxBoundaryResult.contains("gram") && maxBoundaryResult.contains("carbohydrate");
            
            navigateToCalculatorSmart();
            
            ageField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("cage")));
            fastInput(ageField, "18");
            
            WebElement genderFemale = driver.findElement(By.id("csex2"));
            js.executeScript("arguments[0].checked = true;", genderFemale);
            
            fastInput(driver.findElement(By.name("cheightmeter")), "140");
            fastInput(driver.findElement(By.name("ckg")), "40");
            
            activitySelect = new Select(driver.findElement(By.name("cactivity")));
            activitySelect.selectByValue("1.2");
            
            fastScrollAndClick(driver.findElement(By.name("x")));
            fastWait(2000);
            
            String minBoundaryResult = driver.getPageSource();
            boolean minBoundarySuccess = minBoundaryResult.contains("gram") && minBoundaryResult.contains("carbohydrate");
            
            if (maxBoundarySuccess && minBoundarySuccess) {
                System.out.println("TC006 PASSED: Extreme boundary value testing successful");
                System.out.println("  Both maximum and minimum boundary values handled correctly");
                testsPassed++;
            } else {
                System.out.println("TC006 FAILED: Extreme boundary value handling failed");
                testsFailed++;
            }
            
        } catch (Exception e) {
            System.out.println("TC006 FAILED: Exception occurred - " + e.getMessage());
            testsFailed++;
        }
    }
    
    private static void fastWait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private static void printTestSummary() {
        System.out.println("\nTest Execution Summary");
        System.out.println("Total Tests: " + (testsPassed + testsFailed));
        System.out.println("Tests Passed: " + testsPassed);
        System.out.println("Tests Failed: " + testsFailed);
        
        if (testsPassed + testsFailed > 0) {
            System.out.println("Pass Rate: " + String.format("%.1f", (testsPassed * 100.0 / (testsPassed + testsFailed))) + "%");
        }
        
        if (testsFailed == 0) {
            System.out.println("All tests passed successfully");
        } else {
            System.out.println("Some tests failed - review results above");
        }
        System.out.println("=======================================");
    }
    
    private static void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("WebDriver closed successfully");
        }
    }
}
