package com.seleniumpractice;

import java.time.Duration;

// Java - 11 & Selenium - 4.11.0
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TmobileAutomation {
	static WebDriver driver;

	public static void main(String[] args) {
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://www.t-mobile.com/tablets");

		// Example usage:
		selectFilter("Deals", "Special offer");
		selectFilter("Brands", "Alcatel", "Samsung", "T-Mobile");
		selectFilter("Operating System", "Other");

		// Additional examples:
//		selectFilter("Deals", "New", "Special offer");
//		selectFilter("Brands", "Alcatel", "Apple", "Samsung", "T-Mobile", "TCL");
//		selectFilter("Operating System", "Android", "iPadOS", "Other");
//		selectFilter("Deals", "all");
//		selectFilter("Brands", "all");
//		selectFilter("Operating System", "all");
	}

	public static void selectFilter(String menu, String... subMenu) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		WebElement menuElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//legend[normalize-space()='" + menu + "']")));
		menuElement.click();

		if (subMenu[0].equals("all")) {
			String[] allFilters = {};
			switch (menu) {
			case "Deals":
				allFilters = new String[] { "New", "Special offer" };
				break;
			case "Brands":
				allFilters = new String[] { "Alcatel", "Apple", "Samsung", "T-Mobile", "TCL" };
				break;
			case "Operating System":
				allFilters = new String[] { "Android", "iPadOS", "Other" };
				break;
			}

			for (String filter : allFilters) {
				WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(), '" + filter + "')]/ancestor::label/span[1]")));
				element.click();
			}

		} else {
			for (String name : subMenu) {
				WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(), '" + name + "')]/ancestor::label/span[1]")));
				element.click();
			}
		}
	}
}