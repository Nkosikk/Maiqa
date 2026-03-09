package StepDefinations;

import Helpers.BrowserSetup;
import Pages.Admin.CourseManagementPage;
import Utils.RuntimeConfig;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;

public class Base {

    protected static WebDriver driver;
    protected static CourseManagementPage courseManagementPage;

    @Before
    public void setupDriver() {
        String browser = RuntimeConfig.get("browser", "chrome");
        boolean headless = Boolean.parseBoolean(RuntimeConfig.get("headless", "true"));

        driver = BrowserSetup.createDriver(browser, headless);
        courseManagementPage = new CourseManagementPage(driver);
    }

    @After
    public void tearDownDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
            courseManagementPage = null;
        }
    }
}
