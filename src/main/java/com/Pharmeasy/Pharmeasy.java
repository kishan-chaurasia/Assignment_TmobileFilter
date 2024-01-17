package com.Pharmeasy;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Pharmeasy {
	static WebDriver driver;

	public static void main(String[] args) throws InterruptedException {
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://pharmeasy.in/health-care/protein-supplements-620");
		getProductsNames();
		driver.quit();
	}

	/*
	 * PharmEasy has lot of products to load when we scroll down. Everytime new
	 * product starts loading when we reach little bit above from bottom of page.
	 * and problem here is that if we go to bottom of page then the prodcut will not
	 * load so we have have implemented logic in a way where we minus the footer
	 * height from initial height then we scroll so we can reach that loading part
	 * of page not the bottom of page..... so do the infinte scrolling and print all
	 * products names
	 */
	private static void getProductsNames() throws InterruptedException {

		// Perform scrolling to load all elements
		JavascriptExecutor js = (JavascriptExecutor) driver;
		long initialHeight;
		long finalHeight;

		// Replace "your_footer_height" with the height of your footer
		int footerHeight = 1800;
		do {
			// Store the initial scroll height
			initialHeight = (Long) js.executeScript("return document.body.scrollHeight");

			// Scroll to a position slightly above the actual bottom, adjusted for the
			// footer height
			js.executeScript("window.scrollTo(0, arguments[0] - arguments[1]);", initialHeight, footerHeight);

			// Wait for a brief moment to allow new elements to load
			Thread.sleep(2000);

			// Store the final scroll height
			finalHeight = (Long) js.executeScript("return document.body.scrollHeight");

		} while (finalHeight > initialHeight); // Continue scrolling until the scroll height no longer increases

		List<WebElement> elementXpath = driver
				.findElements(By.xpath("//a[contains(@class , 'ProductCard_displayBlock')]"));
		int counter = 1;
		for (WebElement webElement : elementXpath) {
			String prodName = webElement.getAttribute("title");
			System.out.println(counter + ") " + prodName);
			counter++;
		}
	}
}