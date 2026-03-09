package Utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

public class ReusableFunctions {

    WebDriver driver;

    public ReusableFunctions(WebDriver driver) {
        this.driver = driver;
    }

    public void enterTextInTheTextBox(By by, String text) {
        driver.findElement(by).sendKeys(text);
    }

    public void clickButton(By by) {
        driver.findElement(by).click();
    }

    // Scroll an element into view using JavaScript Executor
    public static void scrollElementIntoView(WebDriver driver, WebElement element) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].scrollIntoView({ behavior: 'auto', block: 'center', inline: 'center' });", element);
    }

    // Scroll an element into view and click it using Actions
    public static void scrollElementIntoViewClick(WebDriver driver, WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().perform();
    }
    public static Boolean objectExists(By by,WebDriver driver) {

        if (driver.findElements(by).isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public static void clearFieldWithKeyboardKeys(WebElement element){
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.BACK_SPACE);
        element.clear();
    }
    public static void clearInputField(WebElement element){
        String value = element.getAttribute("value");
        for (int i = 0; i < value.length(); i++) {
            element.sendKeys(Keys.BACK_SPACE);
        }

    }
}
