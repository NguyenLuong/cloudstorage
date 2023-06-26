package com.udacity.jwdnd.course1.cloudstorage;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
		 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}

	@Test
	public void unauthorizedUserAccessHomePage() {
		driver.get("http://localhost:" + this.port + "/home");

		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}


	@Test
	public void loginAndLogout() {
		doMockSignUp("URL","Test","LILO","123");
		doLogIn("LILO", "123");

		Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logoutDiv")));
		WebElement logoutButton = driver.findElement(By.id("logout-btn"));
		logoutButton.click();

		Assertions.assertEquals("http://localhost:" + this.port + "/login?logout", driver.getCurrentUrl());
	}

	@Test
	public void createNote() {
		doMockSignUp("URL","Test","CN","123");
		doLogIn("CN", "123");
		doCreateNote();
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("process-success")));
		WebElement viewNoteTitle = driver.findElement(By.id("note-title-0"));
		WebElement viewNoteDes = driver.findElement(By.id("note-des-0"));

		Assertions.assertEquals("TestNote", viewNoteTitle.getText());
		Assertions.assertEquals("1. Do something.", viewNoteDes.getText());
	}

	@Test
	public void editNote() {
		doMockSignUp("URL","Test","EN","123");
		doLogIn("EN", "123");
		doCreateNote();
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("process-success")));
		WebElement editNoteBtn = driver.findElement(By.id("edit-note-btn-0"));
		editNoteBtn.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement inputNoteTitle = driver.findElement(By.id("note-title"));
		inputNoteTitle.click();
		inputNoteTitle.clear();
		inputNoteTitle.sendKeys("ChangedTestNote");

		WebElement saveNoteBtn = driver.findElement(By.id("save-note-btn"));
		saveNoteBtn.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("process-success")));
		WebElement viewNoteTitle = driver.findElement(By.id("note-title-0"));

		Assertions.assertEquals("ChangedTestNote", viewNoteTitle.getText());
	}

	@Test
	public void deleteNote() {
		doMockSignUp("URL","Test","DN","123");
		doLogIn("DN", "123");
		doCreateNote();
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("process-success")));
		WebElement delBtn = driver.findElement(By.id("del-note-btn-0"));
		delBtn.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("process-success")));

		Assertions.assertThrows(NoSuchElementException.class, () -> {
			driver.findElement(By.id("del-note-btn-0"));
		} );

	}

	private void doCreateNote() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement noteTab = driver.findElement(By.id("nav-notes-tab"));
		noteTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new-note-btn")));
		WebElement createNewNoteBtn = driver.findElement(By.id("new-note-btn"));
		createNewNoteBtn.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement inputNoteTitle = driver.findElement(By.id("note-title"));
		inputNoteTitle.click();
		inputNoteTitle.sendKeys("TestNote");

		WebElement inputNoteDes = driver.findElement(By.id("note-description"));
		inputNoteDes.click();
		inputNoteDes.sendKeys("1. Do something.");

		WebElement saveNoteBtn = driver.findElement(By.id("save-note-btn"));
		saveNoteBtn.click();
	}

	private void doCreateCredential() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement creTab = driver.findElement(By.id("nav-credentials-tab"));
		creTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new-credential-btn")));
		WebElement createNewCreBtn = driver.findElement(By.id("new-credential-btn"));
		createNewCreBtn.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement inputCreUrl = driver.findElement(By.id("credential-url"));
		inputCreUrl.click();
		inputCreUrl.sendKeys("google.com");

		WebElement inputCreUsername = driver.findElement(By.id("credential-username"));
		inputCreUsername.click();
		inputCreUsername.sendKeys("luong");

		WebElement inputCrePassword = driver.findElement(By.id("credential-password"));
		inputCrePassword.click();
		inputCrePassword.sendKeys("12345");

		WebElement saveCreBtn = driver.findElement(By.id("save-credential-btn"));
		saveCreBtn.click();
	}

	@Test
	public void createCredential() {
		doMockSignUp("URL","Test","CC","123");
		doLogIn("CC", "123");
		doCreateCredential();
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("process-success")));
		WebElement viewCreUrl = driver.findElement(By.id("credential-url-0"));
		WebElement viewCreUsername = driver.findElement(By.id("credential-username-0"));
		WebElement viewCrePassword = driver.findElement(By.id("credential-password-0"));

		Assertions.assertTrue("google.com".equals(viewCreUrl.getText()));
		Assertions.assertTrue("luong".equals(viewCreUsername.getText()));
		Assertions.assertFalse("12345".equals(viewCrePassword.getText()));
	}

	@Test
	public void editCredential() {
		doMockSignUp("URL","Test","EC","123");
		doLogIn("EC", "123");
		doCreateCredential();
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("process-success")));
		WebElement editCreBtn = driver.findElement(By.id("edit-credential-btn-0"));
		editCreBtn.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement inputCrePassword = driver.findElement(By.id("credential-password"));
		Assertions.assertTrue("12345".equals(inputCrePassword.getAttribute("value")));

		WebElement inputCreUrl = driver.findElement(By.id("credential-url"));
		inputCreUrl.click();
		inputCreUrl.clear();
		inputCreUrl.sendKeys("udacity.com");

		WebElement saveCreBtn = driver.findElement(By.id("save-credential-btn"));
		saveCreBtn.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("process-success")));
		WebElement viewCreUrl = driver.findElement(By.id("credential-url-0"));

		Assertions.assertEquals("udacity.com", viewCreUrl.getText());
	}

	@Test
	public void deleteCredential() {
		doMockSignUp("URL","Test","EC","123");
		doLogIn("EC", "123");
		doCreateCredential();
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("process-success")));
		WebElement editDelBtn = driver.findElement(By.id("del-credential-btn-0"));
		editDelBtn.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("process-success")));

		Assertions.assertThrows(NoSuchElementException.class, () -> {
			driver.findElement(By.id("del-credential-btn-0"));
		} );
	}
 }
