package com.Spotify;

//Java - 11 & Selenium - 4.11.0
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SpotifySongsCollection {
	static WebDriver driver;

	public static void main(String[] args) throws InterruptedException {
		driver = new ChromeDriver();
		driver.manage().window().maximize();

		/*
		 * Spotify has list of songs to load when we scroll down. Everytime we scroll
		 * after 20 to 22 songs the above songs gets disappear from HTML DOM which means
		 * the songs which are on screen will only be there in DOM so this makes it a
		 * little tricky. We are Printing all songs names present in that List.
		 */

		// Hot Hits Hindi
		driver.get("https://open.spotify.com/playlist/37i9dQZF1DX0XUfTFmNBRM");
		System.out.println("Hot Hits Hindi");
		getSongNames();

		// Top Hindi Tracks of 2023
		driver.get("https://open.spotify.com/playlist/37i9dQZF1DXdf5QlS7TLuX");
		System.out.println("\nTop Hindi Tracks of 2023");
		getSongNames();

		// Gym playlist we never used in 2023
		driver.get("https://open.spotify.com/playlist/37i9dQZF1DX3wwp27Epwn5");
		System.out.println("\nGym playlist we never used in 2023");
		getSongNames();

		// Bollywood Dance Music
		driver.get("https://open.spotify.com/playlist/37i9dQZF1DX8xfQRRX1PDm");
		System.out.println("\nBollywood Dance Music");
		getSongNames();

		// All Out 90s Hindi
		driver.get("https://open.spotify.com/playlist/37i9dQZF1DXa2huSXaKVkW");
		System.out.println("\nAll Out 90s Hindi");
		getSongNames();

		driver.quit();
	}

	private static void getSongNames() {

		// Initialize WebDriverWait
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
		int counter = 0;
		// Iterate in steps of until reaching the maximum number of songs
		for (int i = 2; i <= 150; i++) {
			WebElement elementXpath = null;
			try {
				elementXpath = wait.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath("//div[@aria-rowindex='" + i + "']//a[@data-testid='internal-track-link']/div")));
			} catch (Exception e) {
				System.out.println("Total no of Songs present are :- " + (i - 2));
				break;
			}
			String songName = elementXpath.getText();
			System.out.println((i - 1) + ") " + ": " + songName);
			counter++;
			if (counter == 20) {
				JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
				jsExecutor.executeScript("arguments[0].scrollIntoView({ behavior: 'smooth'});", elementXpath);
				counter = 0;
			}
		}
	}
}