package Helpers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

public class BrowserSetup {

    public static WebDriver driver = null;

    public static WebDriver startBrowser(String browserChoice, String url) {
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));

        if ("chrome".equalsIgnoreCase(browserChoice)) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--remote-allow-origins=*");
            chromeOptions.addArguments("--disable-notifications");
            chromeOptions.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
            chromeOptions.addArguments("--no-sandbox"); // Bypass OS security model
            chromeOptions.addArguments("--disable-extensions"); // disabling extensions
            chromeOptions.addArguments("--disable-gpu"); // applicable to windows os only
            chromeOptions.addArguments("--start-maximized");
            if (headless) {
                chromeOptions.addArguments("--headless=new");
            }
            chromeOptions.addArguments("window-size=1920,1080");
            driver = new ChromeDriver(chromeOptions);
        } else if ("firefox".equalsIgnoreCase(browserChoice)) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else {
            WebDriverManager.edgedriver().setup();
            EdgeOptions edgeOptions = new EdgeOptions();
            edgeOptions.addArguments("--remote-allow-origins=*");
            if (headless) {
                edgeOptions.addArguments("--headless=new");
            }
            driver = new EdgeDriver(edgeOptions);
        }
        driver.manage().window().maximize();
        driver.get(url);
        return driver;
    }



}
