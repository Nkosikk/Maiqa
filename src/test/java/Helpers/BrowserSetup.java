package Helpers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Locale;

public final class BrowserSetup {

    private BrowserSetup() {
    }

    public static WebDriver createDriver(String browser, boolean headless) {
        String requestedBrowser = browser == null ? "chrome" : browser.trim().toLowerCase(Locale.ROOT);

        switch (requestedBrowser) {
            case "firefox":
                return buildFirefox(headless);
            case "edge":
                return buildEdge(headless);
            default:
                return buildChrome(headless);
        }
    }

    private static WebDriver buildChrome(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        if (headless) {
            options.addArguments("--headless=new");
        }
        return new ChromeDriver(options);
    }

    private static WebDriver buildFirefox(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless");
        }
        return new FirefoxDriver(options);
    }

    private static WebDriver buildEdge(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        return new EdgeDriver(options);
    }
}
