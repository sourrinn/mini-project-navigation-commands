package code;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class NavigationCommands {
	
	static WebDriver driver;
	
	public List<String> excelData(String excelName, int rowNum) throws IOException {
		List<String> data = new ArrayList<String>();
		String cd = System.getProperty("user.dir");
		excelName = cd + "\\src\\test\\resources\\" + excelName;
		File file = new File(excelName);
		file.getAbsolutePath();
 
		FileInputStream is = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(is);
		XSSFSheet sheet = workbook.getSheetAt(0);
 
		XSSFRow row = sheet.getRow(rowNum);
		int totalCells = row.getLastCellNum();
 
		XSSFCell cell;
 
		for (int i = 0; i < totalCells; i++) {
			cell = row.getCell(i);
			data.add(cell.toString());
		}
 
		workbook.close();
		return data;
	}
	
	public void sendInputToGoogleSearch(String searchInput) {
		WebElement gsearchbox = driver.findElement(By.id("APjFqb"));
		gsearchbox.sendKeys(searchInput);
		gsearchbox.sendKeys(Keys.ENTER);
	}
	
	public static void main(String[] args) throws InterruptedException {
		NavigationCommands e = new NavigationCommands();
		
		System.out.println("Which browser do you want to automate? (Chrome is set to be the default browser.) \n1. Google Chrome \n2. Microsoft Edge");
		Scanner sc = new Scanner(System.in);
		int browser = sc.nextInt();
		sc.close();
		
		// Driver Setup
		DriverSetup driverSetup = new DriverSetup();
		driver = driverSetup.setUpWebDriver(browser);
		
		// Fetching the data from excel sheet
		List<String> input_data = null;
		
		try {
			input_data = e.excelData("data.xlsx", 0);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Launch Browser and maximize windows
		driver.get("https://google.com");
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		
		// Implement the implicit wait
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		
		// Send input to Google Search Input box and click on the Search Button
		e.sendInputToGoogleSearch(input_data.get(0));
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
		driver.findElement(By.id("Form_getForm_FullName")).sendKeys(input_data.get(1));
		driver.findElement(By.id("Form_getForm_JobTitle")).sendKeys(input_data.get(2));
		driver.findElement(By.id("Form_getForm_Email")).sendKeys(input_data.get(3));
		driver.findElement(By.id("Form_getForm_Contact")).sendKeys(input_data.get(4));
		
		//scrolling down
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("window.scrollBy(0,250)");
		Thread.sleep(1000);
		
		// Send Input to the drop down inputs
		WebElement countrySelect = driver.findElement(By.name("Country"));
		Select selectCountryOption = new Select(countrySelect);
		selectCountryOption.selectByValue(input_data.get(5));
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
		}
		Thread.sleep(3000);
		driver.quit();
	}
}
