package com.Ajio;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Ajio {
	static WebDriver driver;

	public static void main(String[] args) throws InterruptedException {
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://www.ajio.com/s/table-napkins-4720-51871");
		getProductsNames();
		driver.quit();
	}

	/*
	 * Ajio has lot of products to load when we scroll down. Everytime new product
	 * starts loading when we reach bottom of page. so do the infinte scrolling and
	 * print all products names
	 */
	private static void getProductsNames() throws InterruptedException {

		// Perform scrolling to load all elements
		JavascriptExecutor js = (JavascriptExecutor) driver;
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

		List<WebElement> elementXpath = driver.findElements(By.xpath("//div[@class = 'nameCls']"));
		int counter = 1;
		for (WebElement webElement : elementXpath) {
			String prodName = webElement.getText();
			System.out.println(counter + ") " + prodName);
			counter++;
		}
	}
}