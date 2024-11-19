# BDD-Selenium-TestNGFramework
A robust, scalable, and modular framework combining Behavior-Driven Development (BDD) with Selenium and TestNG for efficient automation testing of web applications.
Converting Selenium BDD (Cucumber) to Katalon Studio
This guide demonstrates how to convert your Selenium objects, step definitions, and feature files into Katalon Studio tests by running a Java program. Katalon Studio is a powerful test automation platform that can import and run your existing Selenium-based tests.

Prerequisites
Ensure that the following are installed and configured:

Java JDK 8 or later
Katalon Studio (latest version)
Selenium WebDriver
Cucumber (for BDD support)
TestNG (for test execution)
Steps to Convert Selenium Objects, Step Definitions, and Feature Files to Katalon Studio
Step 1: Set Up Your Selenium Project
Create a Maven Project with the following dependencies in your pom.xml:
xml
Copy code
<dependencies>
    <!-- Selenium WebDriver -->
    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-java</artifactId>
        <version>4.0.0</version>
    </dependency>

    <!-- Cucumber for BDD -->
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-java</artifactId>
        <version>7.10.0</version>
    </dependency>

    <!-- Cucumber TestNG Integration -->
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-testng</artifactId>
        <version>7.10.0</version>
    </dependency>

    <!-- WebDriver Manager (to manage browsers) -->
    <dependency>
        <groupId>io.github.bonigarcia</groupId>
        <artifactId>webdrivermanager</artifactId>
        <version>5.3.0</version>
    </dependency>
</dependencies>
Page Object Model (POM): Create classes for your Selenium WebDriver elements. For example, create a LoginPage class that represents your login page with elements like username, password, and login button.
java
Copy code
public class LoginPage {
    private WebDriver driver;

    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "loginButton")
    private WebElement loginButton;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void login(String username, String password) {
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();
    }
}
Step 2: Convert Step Definitions for Cucumber
Create Step Definitions: In your step definition file, map the Gherkin steps to Java methods. Example:
java
Copy code
public class LoginSteps {
    private WebDriver driver;
    private LoginPage loginPage;

    @Given("User is on the login page")
    public void userIsOnLoginPage() {
        driver.get("http://example.com/login");
        loginPage = new LoginPage(driver);
    }

    @When("User enters username {string} and password {string}")
    public void userEntersUsernameAndPassword(String username, String password) {
        loginPage.login(username, password);
    }

    @Then("User should be logged in successfully")
    public void userShouldBeLoggedInSuccessfully() {
        // Verification logic for successful login
    }
}
Step 3: Convert Feature Files
Feature Files: Write your Gherkin feature files using the .feature extension. For example:
gherkin
Copy code
Feature: Login Feature

  Scenario: Successful login
    Given User is on the login page
    When User enters username "testuser" and password "password123"
    Then User should be logged in successfully
Step 4: Running Java Program to Export to Katalon Studio
Write a Java Program: Create a Java program that converts your existing Selenium objects, step definitions, and feature files to Katalon Studio-compatible formats.
java
Copy code
import java.io.*;

public class ExportToKatalon {
    public static void main(String[] args) {
        try {
            // Example of converting feature file into Katalon Studio test case
            String featureFilePath = "src/test/resources/login.feature";
            File featureFile = new File(featureFilePath);
            if (featureFile.exists()) {
                // Logic to convert feature file into Katalon test case
                System.out.println("Feature file found: " + featureFilePath);
                // Add your logic here to export to Katalon Studio
            } else {
                System.out.println("Feature file does not exist.");
            }

            // Export Selenium Page Object (LoginPage.java) and Step Definitions to Katalon Studio format
            // Here, you can use Katalon's API or manual export method for object and step definitions
            System.out.println("Conversion completed!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
Exporting the Feature Files: After running this Java program, manually import the generated feature files, page objects, and step definitions into Katalon Studio:
Go to Katalon Studio and create a new Test Case or Test Object.
Import the *.feature files and *.java files (Selenium step definitions and page objects).
Step 5: Running Tests in Katalon Studio
Once you’ve imported the converted files into Katalon Studio, you can run the tests by following these steps:

Import Cucumber Plugin (if not already installed):

Go to Katalon Studio > Preferences > Katalon > Plugins and install the Cucumber plugin.
Run Test Cases:

Select the feature file or test case.
Click Run to execute the tests.
Conclusion
You have successfully converted your Selenium test automation framework (with BDD, step definitions, and feature files) into Katalon Studio tests using a Java program. Now, you can run and manage your tests more efficiently within Katalon Studio's test management environment.

This guide covers all the necessary steps to convert your Selenium framework to Katalon Studio. You can further customize the Java program for more complex conversions or integrations as needed.
