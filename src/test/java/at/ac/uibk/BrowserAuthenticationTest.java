package at.ac.uibk;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.sun.xml.internal.ws.server.DraconianValidationErrorHandler;

public class BrowserAuthenticationTest {

	private static final int MAX_PAGE_LOADING_TIME = 30000;
	private static final int MAX_IMPLICIT_WAIT = 10000;
	private static final int MAX_IMPLICIT_WAIT_JS = 5000;

	private WebDriver driver;

	private static final String URL_PAGE = "http://webinfo-myshop.herokuapp.com/#/login";

	private static final String ID_USERNAME = "username";
	private static final String USERNAME = "Pati2";
	private static final String WRONG_USERNAME = "Pati3";
	private static final String ID_PASSWORD = "password";
	private static final String PASSWORD = "admin";
	private static final String LOGIN_BUTTON_XPATH = "//input[@type=\"submit\"]";

	@BeforeMethod
	public void startBrowser() {

		driver = new FirefoxDriver();
		driver.manage().timeouts().pageLoadTimeout(MAX_PAGE_LOADING_TIME, TimeUnit.MILLISECONDS)
				.implicitlyWait(MAX_IMPLICIT_WAIT, TimeUnit.MILLISECONDS)
				.setScriptTimeout(MAX_IMPLICIT_WAIT_JS, TimeUnit.MILLISECONDS);

	}

	@AfterMethod
	public void closeBrowser() {
		if (driver != null) {
			driver.close();
		}
	}

	@Test
	public void login() {

		Assert.assertTrue(doLogin(USERNAME));

	}

	@Test
	public void loginFail() {

		Assert.assertFalse(doLogin(WRONG_USERNAME));

	}

	private boolean doLogin(String username) {

		driver.get(URL_PAGE);

		// enter username
		final WebElement usernameElement = driver.findElement(By.id(ID_USERNAME));
		usernameElement.sendKeys(username);

		// enter password
		final WebElement passwordElement = driver.findElement(By.id(ID_PASSWORD));
		passwordElement.sendKeys(PASSWORD);

		// get login button element
		final WebElement button = driver.findElement(By.xpath(LOGIN_BUTTON_XPATH));
		button.click();

		// wait
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return driver.getPageSource().contains(username);

	}
}
