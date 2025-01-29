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
    		


    // Example action: Checking if the element is displayed
    if (loginLogo.isDisplayed()) {
        System.out.println("Login logo is visible on the page.");
    } else {
        System.out.println("Login logo is not visible on the page.");
    }


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
    public void the_user_is_on_the_login_page()  {
       driver.get("https://www.saucedemo.com/v1/"); 
       new WebDriverWait(driver, Duration.ofSeconds(10)).until(
    		    ExpectedConditions.presenceOfElementLocated(By.id("user-name"))
    		);

    }

    @When("the user enters valid username and password")
    public void the_user_enters_valid_username_and_password() { 
    	  WebElement usernameField = driver.findElement(By.id("user-name"));
          WebElement passwordField = driver.findElement(dashboardPage.passwordField);
        usernameField.clear();
        passwordField.clear();
   usernameField.sendKeys("standard_user");
        passwordField.sendKeys("secret_sauce");
    }

    @Then("clicks the login button")
    public void clicks_the_login_button() {
        WebElement loginButton = driver.findElement(dashboardPage.loginButton);
        loginButton.click();
      
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
    }
      
 
    @After
    public void tearDown() {
        if (driver != null) {
           // driver.quit();
        }
    }
}
