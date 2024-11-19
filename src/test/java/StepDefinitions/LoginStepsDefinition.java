package StepDefinitions;

import Pages.DashboardPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class LoginStepsDefinition {

    static WebDriver driver;
    DashboardPage dashboardPage = new DashboardPage();  // Create an instance of the DashboardPage class

    @Given("the user is on the login page")
    public void the_user_is_on_the_login_page() {
        // Set the ChromeDriver path
        System.setProperty("webdriver.chrome.driver", "/Users/ghazalashahin/Downloads/chromedriver-mac-arm64/chromedriver"); // Update this path

        // Initialize the WebDriver
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();

        // Open the login page
        driver.get("https://www.saucedemo.com/v1/");
    }

    @When("the user enters valid username and password")
    public void the_user_enters_valid_username_and_password() {
        // Use the locators from the DashboardPage class
        WebElement usernameInput = driver.findElement(dashboardPage.usernameField);
        WebElement passwordInput = driver.findElement(dashboardPage.passwordField);

        usernameInput.sendKeys("standard_user");
        passwordInput.sendKeys("secret_sauce");
    }

    @When("clicks the login button")
    public void clicks_the_login_button() {
        // Use the locator for the login button from DashboardPage
        WebElement loginButton = driver.findElement(dashboardPage.loginButton);
        loginButton.click();
    }

    @Then("the user should be logged in")
    public void the_user_should_be_logged_in() {
        // Use the locator for the dashboard logo element from DashboardPage
        WebElement dashboardElement = driver.findElement(dashboardPage.dashboardLogo);
        if (dashboardElement.isDisplayed()) {
            System.out.println("Login successful");
        } else {
            throw new AssertionError("Login failed");
        }
        driver.quit();
    }
}
