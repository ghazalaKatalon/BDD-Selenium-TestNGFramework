package StepDefinitions;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Pages.DashboardPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class loginStep {

    static WebDriver driver;
    DashboardPage dashboardPage = new DashboardPage();

    // Initialize WebDriver before each test
    @Before
    public void setup() {
        // Set the ChromeDriver path
        System.setProperty("webdriver.chrome.driver", "/Users/ghazalashahin/Downloads/chromedriver-mac-arm64/chromedriver");

        // Initialize the WebDriver
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Given("the user is on the login page")
    public void the_user_is_on_the_login_page() throws InterruptedException {
        // Open the login page
        driver.get("https://www.saucedemo.com/v1/");
        Thread.sleep(1000);
        
    }

    @When("the user enters valid username and password")
    public void the_user_enters_valid_username_and_password() {
        // Use the locators from the DashboardPage class
    	  WebElement usernameField = driver.findElement(dashboardPage.usernameField);
          WebElement passwordField = driver.findElement(dashboardPage.passwordField);
     
     // Clear the fields
        usernameField.clear();
        passwordField.clear();
        
     // enter the data
        usernameField.sendKeys("standard_user");
        passwordField.sendKeys("secret_sauce");
    }

    @When("clicks the login button")
    public void clicks_the_login_button() {
        // Use the locator for the login button from DashboardPage
        WebElement loginButton = driver.findElement(dashboardPage.loginButton);
        loginButton.click();
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Then("the user should be logged in")
    public void the_user_should_be_logged_in() {
    	
    	WebElement dashboardLogo = driver.findElement(dashboardPage.dashboardLogo);

    	// Wait for presence of an element using the By locator from the DashboardPage
    	new WebDriverWait(driver, Duration.ofSeconds(10)).until(
    	    ExpectedConditions.presenceOfElementLocated(dashboardPage.dashboardLogo)
    	);
    	
    	
    	
     
        if (dashboardLogo.isDisplayed()) {
            System.out.println("Login successful");
        } else {
            throw new AssertionError("Login failed");
        }
     // Close the browser
        
        //testing purpose
        
     /*   driver.navigate().back();
        driver.quit();
        driver.navigate().forward();
        WebElement element = driver.findElement(By.xpath("h1"));
        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();*/
    }

    // Quit WebDriver after each test
    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
