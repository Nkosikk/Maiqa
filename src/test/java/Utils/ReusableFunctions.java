package Utils;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ReusableFunctions {
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(20);

    private final WebDriver driver;
    private final WebDriverWait wait;

    public ReusableFunctions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
    }

    public void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public void type(By locator, String value) {
        WebElement element = waitForVisible(locator);
        element.clear();
        element.sendKeys(value);
    }

    public void selectByValueOrText(By locator, String optionValue) {
        Select select = new Select(waitForVisible(locator));
        try {
            select.selectByValue(optionValue);
        } catch (NoSuchElementException ignored) {
            select.selectByVisibleText(optionValue);
        }
    }

    public void selectByValue(By locator, String optionValue) {
        Select select = new Select(waitForVisible(locator));
        select.selectByValue(optionValue);
    }

    public void selectByVisibleText(By locator, String visibleText) {
        Select select = new Select(waitForVisible(locator));
        select.selectByVisibleText(visibleText);
    }

    public WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForVisibility(By locator) {
        return waitForVisible(locator);
    }

    public String getText(By locator) {
        return waitForVisible(locator).getText().trim();
    }

    public boolean isVisible(By locator) {
        try {
            return waitForVisible(locator).isDisplayed();
        } catch (TimeoutException ex) {
            return false;
        }
    }

    public boolean isSelected(By locator) {
        return waitForVisible(locator).isSelected();
    }

    public String waitForAlertAndAccept(Duration timeout) {
        Alert alert = new WebDriverWait(driver, timeout).until(ExpectedConditions.alertIsPresent());
        String text = alert.getText();
        alert.accept();
        return text;
    }

    public boolean waitForPresence(By locator, Duration timeout) {
        try {
            new WebDriverWait(driver, timeout).until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (TimeoutException ex) {
            return false;
        }
    }

    public WebDriverWait getWait() {
        return wait;
    }
}
