package code;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

//import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class NavigationCommands {
	
	static WebDriver driver;
	
	public void sendInputToGoogleSearch() {
		WebElement gsearchbox = driver.findElement(By.id("APjFqb"));
		gsearchbox.sendKeys("Orange HRM demo");
		gsearchbox.sendKeys(Keys.ENTER);
	}
	
	public static void main(String[] args) throws InterruptedException {
		NavigationCommands e = new NavigationCommands();
		
		// Driver Setup
		DriverSetup driverSetup = new DriverSetup();
		driver = driverSetup.setUpWebDriver(1);
		
		// Launch Browser and maximize windows
		driver.get("https://google.com");
		driver.manage().window().maximize();
		
		// Implement the implicit wait
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		// Send input to Google Search Input box and click on the Search Button
		e.sendInputToGoogleSearch();
		Thread.sleep(3000);
		
		// Click on the back button and verify if previous page is appearing
		driver.navigate().back();
		Thread.sleep(3000);
		if (driver.getTitle().equals("Google")) {
			System.out.println("Back Navigation Verified");
		} else {
			System.out.println("Back Navigation Failed");
		}
		
		// Click on the forward button and verify if it is redirected to the results page
		driver.navigate().forward();
		Thread.sleep(3000);
		if (driver.getTitle().equals("Orange HRM demo - Google Search")) {
			System.out.println("Forward Navigation Verified");
		} else {
			System.out.println("Forward Navigation Failed");
		}
		
		// Visit the destination page
		driver.get("https://opensource-demo.orangehrmlive.com");
		String previousPageTitle = driver.getTitle();
		Thread.sleep(3000);
		
		// To visit the page where CONTACT SALES is present
		driver.findElement(By.xpath("//a[@href='http://www.orangehrm.com']")).click();
		Thread.sleep(3000);
		
		// Fetch all window id for multiple browser handling
		List<Object> window_ids = driver.getWindowHandles().stream().collect(Collectors.toList());
		
		// To switch the driver to the new window
		driver.switchTo().window((String) window_ids.get(1));
		Thread.sleep(2000);
		
		driver.findElement(By.xpath("//*[@id=\"navbarSupportedContent\"]/div[2]/ul/li[2]/a/button")).click();
		
		// Send Input to the form
		driver.findElement(By.id("Form_getForm_FullName")).sendKeys("India");
		driver.findElement(By.id("Form_getForm_JobTitle")).sendKeys("ABC");
		driver.findElement(By.id("Form_getForm_Email")).sendKeys("test@test.com");
		driver.findElement(By.id("Form_getForm_Contact")).sendKeys("1234567890");
		
		//scrolling down
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("window.scrollBy(0,250)");
		Thread.sleep(1000);
		
		// Send Input to the drop down inputs
		WebElement countrySelect = driver.findElement(By.name("Country"));
		Select selectCountryOption = new Select(countrySelect);
		selectCountryOption.selectByValue("India");
		WebElement numOfEmpDropDown= driver.findElement(By.id("Form_getForm_NoOfEmployees"));
		Select selectOption= new Select(numOfEmpDropDown);
		selectOption.selectByValue("11 - 15");
		
		// Click on the captcha checkbox
		try {
			driver.findElement(By.xpath("//*[@id=\"recaptcha-anchor\"]/div[1]")).click();
		} catch (Exception err) {
			System.out.println("Error: The captcha checkbox cannot be handled using automation.");
		}
		
		// Click on the Submit button
		driver.findElement(By.name("action_submitForm")).click();
		
		Thread.sleep(2000);
		
		driver.close();
		System.out.println("The previous tab is visible as the previous browser is not terminated.");	
		try {
			if(driver.getTitle().equals(previousPageTitle)) {
				System.out.println("Requirement Verified: The driver navigates the previous page.");				
			}
		} catch (Exception err) {
			System.out.println("Requirement Not Verified: The driver doesn't navigate to the previous page.");
			System.out.println(err.getMessage());
		}
		Thread.sleep(3000);
		driver.quit();	
	}
}
