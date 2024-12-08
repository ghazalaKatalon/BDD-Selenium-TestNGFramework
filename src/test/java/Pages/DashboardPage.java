package Pages;

import org.openqa.selenium.By;

public class DashboardPage {
	//public By usernameField = By.id("user-name");
	public By passwordField = By.id("password");
	public By loginButton = By.id("login-button");
	public By dashboardLogo = By.xpath("//*[@class='app_logo']");
	public By burgerButton = By.xpath("//button[contains(text(),'Open Menu')]");


}
