package code;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class DriverSetup {
	public WebDriver setUpWebDriver(int ch) {
		WebDriver driver;
		if(ch == 2) {
			driver = new EdgeDriver();
		}
		else {
			driver = new ChromeDriver();
		}
		return driver;
	}
}
