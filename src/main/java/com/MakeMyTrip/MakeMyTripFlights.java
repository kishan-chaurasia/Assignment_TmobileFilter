package com.MakeMyTrip;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class MakeMyTripFlights {
	static WebDriver driver;
	static WebDriverWait wait;
	static JavascriptExecutor js;

	public static void main(String[] args) throws InterruptedException {
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://www.makemytrip.com/flights/");
		Thread.sleep(2000);
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		js = (JavascriptExecutor) driver;

		// Click on Round Trip checkbox
		driver.findElement(By.xpath("//*[text() = 'Round Trip']/span")).click();

		selectFromAndTo("Mumbai", "Delhi");

		// Departure date
		// selectDate("02", "Mar", "2024");
		// Return date
		// selectDate("25", "Oct", "2024");

		selectDate();

		// Click on search button
		driver.findElement(By.xpath("//a[text() = 'Search']")).click();

		// wait for Lock Prices Now popup and click on 'OKAY, GOT IT!' button
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("// button[text() = 'OKAY, GOT IT!']"))).click();

		scrollTillBottomOfPage();
		clickOnEveryViewAllOptions();
		getTotalDepartAndReturnFlights();
		scrollToTopOfPage();

		selectNonStopFilter();
		scrollTillBottomOfPage();
		System.out.println("\nAfter selecting Non Stop Filter Checkbox");
		getTotalDepartAndReturnFlights();
		scrollToTopOfPage();
		selectNonStopFilter();

		select1StopFilter();
		scrollTillBottomOfPage();
		System.out.println("\nAfter selecting 1 Stop Filter Checkbox");
		getTotalDepartAndReturnFlights();
		scrollToTopOfPage();

		// select Depart and Return Flight with their Row numbers
		selectFlight(2, 3);

		int departFlightAmount = getDptFlightAmt(2);
		int returnFlightAmount = getRtnFlightAmt(3);
		int departFlightAmountFromStickBox = getDptFlightAmtFromStickBox();
		int returnFlightAmtFromStickBox = getRtnFlightAmtFromStickBox();
		int totalAmountFromStickyBox = getTotalFlightAmtFromStickBox();

		System.out.println("\ngetDptFlightAmt :- " + departFlightAmount);
		System.out.println("getRtnFlightAmt :- " + returnFlightAmount);
		System.out.println("getDptFlightAmtFromStickBox :- " + departFlightAmountFromStickBox);
		System.out.println("getRtnFlightAmtFromStickBox :- " + returnFlightAmtFromStickBox);
		System.out.println("\nTotal Amount From Sticky Box :- " + totalAmountFromStickyBox);

		Assert.assertEquals(departFlightAmount, departFlightAmountFromStickBox, "assert failed hogaya");
		Assert.assertEquals(returnFlightAmount, returnFlightAmtFromStickBox, "assert failed hogaya");
		Assert.assertEquals((departFlightAmount + returnFlightAmount), totalAmountFromStickyBox,
				"assert failed hogaya");
		Assert.assertEquals((departFlightAmountFromStickBox + returnFlightAmtFromStickBox), totalAmountFromStickyBox,
				"assert failed hogaya");

		driver.quit();
	}

	private static void selectFromAndTo(String From, String To) throws InterruptedException {
		// From
		driver.findElement(By.xpath("//span[text() = 'From']")).click();
		driver.findElement(By.xpath("//input[@placeholder = 'From']")).sendKeys(From);
		driver.findElement(By.xpath("//li[@role = 'option'][1]")).click();

		// To
		driver.findElement(By.xpath("//span[text() = 'To']")).click();
		driver.findElement(By.xpath("//input[@placeholder = 'To']")).sendKeys(To);
		Thread.sleep(2000);
		driver.findElement(By.xpath("//li[@role = 'option'][1]")).click();
	}

	// Select any specific date you want
	private static void selectDate(String date, String monthOnly3Letters, String year) {
		String dateLocator = "//div[contains(@aria-label, '" + monthOnly3Letters + " " + date + " " + year
				+ "') and @aria-disabled='false']";

		boolean dateVisible = false;

		while (!dateVisible) {
			try {
				driver.findElement(By.xpath(dateLocator)).click();
				dateVisible = true; // Exit the loop if the date is successfully clicked
			} catch (Exception e) {
				// Date element not found, click on the next button
				driver.findElement(By.xpath("//span[@aria-label = 'Next Month']")).click();
			}
		}
	}

	// select the departure date as current date and return date after 7 days
	private static void selectDate() {
		// Get the current date
		LocalDate currentDate = LocalDate.now();
		// Calculate the date after 7 days
		LocalDate dateAfter7Days = currentDate.plusDays(7);

		// Define a date format for the desired output pattern
		DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("MMM dd yyyy");

		// Format the date as a string
		String formattedCurrentDate = currentDate.format(datePattern);
		String formattedDateAfter7Days = dateAfter7Days.format(datePattern);

		String departDateLocator = "//div[contains(@aria-label, '" + formattedCurrentDate
				+ "') and @aria-disabled='false']";
		String returnDateLocator = "//div[contains(@aria-label, '" + formattedDateAfter7Days
				+ "') and @aria-disabled='false']";

		boolean departDateVisible = false;
		boolean returnDateVisible = false;

		while (!departDateVisible) {
			try {
				driver.findElement(By.xpath(departDateLocator)).click();
				departDateVisible = true; // Exit the loop if the date is successfully clicked
			} catch (Exception e) {
				// Date element not found, click on the next button
				driver.findElement(By.xpath("//span[@aria-label = 'Next Month']")).click();
			}
		}
		while (!returnDateVisible) {
			try {
				driver.findElement(By.xpath(returnDateLocator)).click();
				returnDateVisible = true; // Exit the loop if the date is successfully clicked
			} catch (Exception e) {
				// Date element not found, click on the next button
				driver.findElement(By.xpath("//span[@aria-label = 'Next Month']")).click();
			}
		}
	}

	// Perform scrolling Till Bottom of Page to load all elements
	private static void scrollTillBottomOfPage() throws InterruptedException {
		long initialHeight;
		long finalHeight;
		do {
			// Store the initial scroll height
			initialHeight = (Long) js.executeScript("return document.body.scrollHeight");

			// Scroll to the bottom of the page
			js.executeScript("window.scrollTo(0, document.body.scrollHeight)");

			// Wait for a brief moment to allow new elements to load
			Thread.sleep(2000);

			// Store the final scroll height
			finalHeight = (Long) js.executeScript("return document.body.scrollHeight");

		} while (finalHeight > initialHeight); // Continue scrolling until the scroll height no longer increases
	}

	// Scroll to Top of page
	private static void scrollToTopOfPage() {
		js.executeScript("window.scrollTo(0, 0)");
	}

	// Click on Every ViewAll Dropdown Option and then wait for that dropdown
	// animation to complete by waiting for that class attribute value to change
	private static void clickOnEveryViewAllOptions() {
		List<WebElement> viewAllDropDown = driver.findElements(By.xpath("//button[text() = 'VIEW ALL']"));
		for (int i = 0; i < viewAllDropDown.size(); i++) {
			js.executeScript("arguments[0].scrollIntoView({ block: 'center' });", viewAllDropDown.get(i));
			viewAllDropDown.get(i).click();

			wait.until(ExpectedConditions.attributeContains(By.xpath(
					"(//span[text() = 'Some more options with stopovers']/following :: label[1])[" + (i + 1) + "]"),
					"class", "splitViewListing"));
		}
	}

	private static void getTotalDepartAndReturnFlights() {
		int departFlightCount = driver.findElements(By.xpath("//div[@class = 'paneView'][1]//label")).size();
		int returnFlightCount = driver.findElements(By.xpath("//div[@class = 'paneView'][2]//label")).size();
		System.out.println("Total no of Departure Flights are :- " + departFlightCount
				+ "\nTotal no of Return Flights are :- " + returnFlightCount);
	}

	private static void selectFlight(int departFlightNo, int returnFlightNo) {
		// select flight from Departure Row number
		WebElement departFlight = wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("(//div[@class = 'paneView'][1]//label)[" + departFlightNo + "]")));
		js.executeScript("arguments[0].scrollIntoView({ block: 'center' });", departFlight);
		departFlight.click();
		wait.until(ExpectedConditions.attributeContains(
				By.xpath("(//div[@class = 'paneView'][1]//label)[" + departFlightNo + "]"), "class",
				"splitViewListing checked"));

		// select flight from Return Row number
		WebElement returnFlight = wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("(//div[@class = 'paneView'][2]//label)[" + returnFlightNo + "]")));
		js.executeScript("arguments[0].scrollIntoView({ block: 'center' });", returnFlight);
		returnFlight.click();
		wait.until(ExpectedConditions.attributeContains(
				By.xpath("(//div[@class = 'paneView'][2]//label)[" + returnFlightNo + "]"), "class",
				"splitViewListing checked"));
	}

	private static void selectNonStopFilter() {
		driver.findElement(By.xpath("(//p[normalize-space() = 'Non Stop'])[1]/../..//input")).click();
	}

	private static void select1StopFilter() {
		driver.findElement(By.xpath("(//p[normalize-space() = '1 Stop'])[1]/../..//input")).click();
	}

	private static int getDptFlightAmt(int departFlightNo) {
		String departFlightAmount = driver
				.findElement(By.xpath(
						"(//div[@class = 'paneView'][1]//label)[ " + departFlightNo + " ]//p[contains(text() , '₹')]"))
				.getText();
		return Integer.parseInt(departFlightAmount.replaceAll("[^\\d]", ""));
	}

	private static int getRtnFlightAmt(int returnFlightNo) {
		String returnFlightAmount = driver
				.findElement(By.xpath(
						"(//div[@class = 'paneView'][2]//label)[ " + returnFlightNo + " ]//p[contains(text() , '₹')]"))
				.getText();
		return Integer.parseInt(returnFlightAmount.replaceAll("[^\\d]", ""));
	}

	private static int getDptFlightAmtFromStickBox() {
		String departFlightAmountFromStickBox = driver
				.findElement(By.xpath("(//div[@class= 'splitviewStickyOuter ']//p[contains(text(), '₹')])[1]"))
				.getText();
		return Integer.parseInt(departFlightAmountFromStickBox.replaceAll("[^\\d]", ""));
	}

	private static int getRtnFlightAmtFromStickBox() {
		String returnFlightAmtFromStickBox = driver
				.findElement(By.xpath("(//div[@class= 'splitviewStickyOuter ']//p[contains(text(), '₹')])[2]"))
				.getText();
		return Integer.parseInt(returnFlightAmtFromStickBox.replaceAll("[^\\d]", ""));
	}

	private static int getTotalFlightAmtFromStickBox() {
		String totalFlightAmtFromStickyBox = driver
				.findElement(By.xpath("//div[@class = 'textRight appendRight10']//span")).getText();
		return Integer.parseInt(totalFlightAmtFromStickyBox.replaceAll("[^\\d]", ""));
	}

}