package com.SeleniumPracticeSpace;

import java.time.Duration;
import java.util.Arrays;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumPracticeSpace {
	static WebDriver driver;

	// Page Factory
	@FindBy(xpath = "//input[@aria-controls = 'pr_id_1_list']")
	public static WebElement fromXpath;

	@FindBy(xpath = "//input[@aria-controls = 'pr_id_2_list']")
	public static WebElement toXpath;

	@FindBy(xpath = "//p-calendar[contains(@dateformat , 'dd')]//input")
	public static WebElement dateXpath;

	@FindBy(xpath = "//p-dropdown[@id = 'journeyClass']")
	public static WebElement classXpath;

	@FindBy(xpath = "// p-dropdown[@id = 'journeyQuota']")
	public static WebElement quotoXpath;

	@FindBy(xpath = "//button[@label = 'Find Trains']")
	public static WebElement searchBtnXpath;

	@FindBy(xpath = "//img[@id = 'disha-banner-close']")
	public static WebElement askDishPopUpXpath;

	// Dynamic Xpath
	private static String fromToSuggestionsXpath = "//span[contains(text() , '%s')]/parent :: li";
	private static String classDropdownXpath = "//li[contains(@aria-label , '%s')]";
	private static String quotoDropdownXpath = "//li[@aria-label = '%s']";
	private static String seatAvailabilityRefreshBtnXpath = "(//strong[contains(text() , '%s')]/following :: strong[contains(text() , '%s')]/following :: span)[1]";

	public static void main(String[] args) throws InterruptedException {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("start-maximized");
		options.addArguments("--disable-notifications");
		options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
		options.addArguments(
				"user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
		driver = new ChromeDriver(options);
		driver.manage().deleteAllCookies();
		driver.get("https://www.irctc.co.in/nget/train-search");
		// Initialize PageFactory after initializing the driver
		PageFactory.initElements(driver, SeleniumPracticeSpace.class);

		// From - To
		selectFromAndTo("LTT", "PRYJ");

		// Select Class - SL , 3E ,3A ,2A ,1A
		selectClass("SL");

		// Select Quoto - GENERAL , TATKAL , PREMIUM TATKAL , LOWER BERTH/SR.CITIZEN ,
		// LADIES
		selectQuoto("GENERAL");

		// Select Date
		selectDate("03/01/2024");

		// closeAskDishaPopUp();

		// Search button
		clickOnSearchBtn();

		// close Ask Dish Big Pop up on Right Side Corner
		// closeAskDishaPopUp();

		// Scroll to the train and click on Seat Refresh button
		clickOnSeatAvailabilityRefreshBtn("22183", "SL");

	}

	// Select From and To
	private static void selectFromAndTo(String From, String To) {
		// From
		waitClickableSendKeys(fromXpath, From, 20);
		waitClickableClick(getDynamicXpath(fromToSuggestionsXpath, From), 5);

		// TO
		waitClickableSendKeys(toXpath, To, 5);
		waitClickableClick(getDynamicXpath(fromToSuggestionsXpath, To), 5);
	}

	// Select Date
	private static void selectDate(String date) {
		dateXpath.sendKeys(Keys.CONTROL + "a");
		dateXpath.sendKeys(Keys.BACK_SPACE);
		dateXpath.sendKeys(date);
		// dateXpath.sendKeys(Keys.ENTER);
	}

	// Select Class - SL , 3E ,3A ,2A ,1A
	private static void selectClass(String classOption) {
		waitClickableClick(classXpath, 5);
		waitClickableClick(getDynamicXpath(classDropdownXpath, classOption), 5);
	}

	// Select Quoto - GENERAL , TATKAL , PREMIUM TATKAL , LOWER BERTH/SR.CITIZEN ,
	// LADIES
	private static void selectQuoto(String quotoOption) {
		waitClickableClick(quotoXpath, 5);
		waitClickableClick(getDynamicXpath(quotoDropdownXpath, quotoOption), 5);
	}

	// Click on Search Button
	private static void clickOnSearchBtn() {
		waitClickableClick(searchBtnXpath, 5);
	}

	// Click on Search Button
	private static void closeAskDishaPopUp() {
		waitClickableClick(askDishPopUpXpath, 5);
	}

	// Click on Seat Availability Refresh Button
	// trainNo = 11055
	// className = SL , 3E , 3A , 2A , 1A
	private static void clickOnSeatAvailabilityRefreshBtn(String trainNo, String className) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		String seatformattedXpath = getDynamicXpath(seatAvailabilityRefreshBtnXpath, trainNo, className);
		WebElement seatElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(seatformattedXpath)));
		Thread.sleep(2000);
		scrollPageToElement(seatElement);
		 waitClickableClick(seatElement, 10);
	}

	// Returns dynamic XPath by formatting XPath template with given value
	private static String getDynamicXpath(String xpath, String... values) {
		String formattedXpath = String.format(xpath, (Object[]) values);
		return formattedXpath;
	}

	// Wait until Clickable then SendKeys - WebElement
	private static void waitClickableSendKeys(WebElement element, String value, int noOfSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(noOfSeconds));
		wait.until(ExpectedConditions.elementToBeClickable(element)).sendKeys(value);
	}

	// Wait until Clickable then SendKeys - String
	private static void waitClickableSendKeys(String Xpath, String value, int noOfSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(noOfSeconds));
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(Xpath))).sendKeys(value);
	}

	// Wait until Clickable then Click - WebElement
	private static void waitClickableClick(WebElement element, int noOfSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(noOfSeconds));
		wait.until(ExpectedConditions.elementToBeClickable(element)).click();
	}

	// Wait until Clickable then Click - String
	private static void waitClickableClick(String Xpath, int noOfSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(noOfSeconds));
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(Xpath))).click();
	}

	// Scroll Method
	public static void scrollPageToElement(WebElement scrollToElement) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView({ block: 'center'});", scrollToElement);
	}

}