package com.worldometers;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/*
 * This Java program automate the retrieval and printing of real-time population metrics from the Worldometer website.
 * The metrics include Current "World Population," "Births Today," "Deaths Today," "Population Growth Today," "Births This Year," 
 * "Deaths This Year," and "Population Growth This Year."
 */
public class WorldPopulationClock {
	private static WebDriver driver;

	public static void main(String[] args) throws InterruptedException {
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://www.worldometers.info/world-population/");

		getProductsNames();
	}

	private static void getProductsNames() throws InterruptedException {
		// Delay to allow the webpage to load before initiating live monitoring
		Thread.sleep(3000);

		// Array to store the last printed values for different population metrics
		long[] lastPrintedValues = new long[7];

		// Continuously monitor and print live updates
		while (true) {
			// Locate population metrics elements on the webpage
			List<WebElement> elements = driver
					.findElements(By.xpath("//div[@class = 'sec-counter' or span[@class = 'rts-counter']]"));

			// Iterate through each population metric
			for (int i = 0; i < elements.size(); i++) {
				try {
					// Extract and format the current value of the population metric
					String liveValue = elements.get(i).getText();
					String formattedValue = liveValue.replace(",", "");
					long currentValue = Long.parseLong(formattedValue);

					// Print the updated value if it is greater than the last printed value
					if (currentValue > lastPrintedValues[i]) {
						printResult(i, liveValue);
						lastPrintedValues[i] = currentValue;
					}
				} catch (StaleElementReferenceException e) {

				}
			}
			// Add a newline after
			System.out.println();
		}
	}

	private static void printResult(int index, String value) {
		// Labels for different population metrics
		String[] labels = { "Current World Population", "Births Today", "Deaths Today", "Population Growth Today",
				"Births This Year", "Deaths This Year", "Population Growth This Year" };

		// Print the label and the corresponding value for the population metric
		System.out.println(labels[index] + " = " + value);
	}
}