package StepDefinations;

import Helpers.BrowserSetup;
import org.openqa.selenium.WebDriver;

public class Base {
    protected WebDriver driver;

    protected void startSession() {
        if (driver != null) {
            return;
        }

        String browser = System.getProperty("browser", "chrome");
        String baseUrl = System.getProperty("baseUrl", "https://example.com");
        driver = BrowserSetup.startBrowser(browser, baseUrl);
    }

    protected void stopSession() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
