package Pages;

import Utils.ReusableFunctions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SwagLabsPage {
    private final WebDriver driver;
    private final ReusableFunctions reusableFunctions;
    private final WebDriverWait wait;

    private static final By USERNAME_INPUT = By.id("user-name");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By CART_BADGE = By.cssSelector("span.shopping_cart_badge");

    public SwagLabsPage(WebDriver driver) {
        this.driver = driver;
        this.reusableFunctions = new ReusableFunctions(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateTo(String url) {
        driver.navigate().to(url);
        wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME_INPUT));
    }

    public void typeUsername(String username) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME_INPUT));
        reusableFunctions.enterTextInTheTextBox(USERNAME_INPUT, username);
    }

    public void typePassword(String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(PASSWORD_INPUT));
        reusableFunctions.enterTextInTheTextBox(PASSWORD_INPUT, password);
    }

    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(LOGIN_BUTTON));
        reusableFunctions.clickButton(LOGIN_BUTTON);
        wait.until(ExpectedConditions.urlContains("inventory.html"));
    }

    public void clickAddToCartForItem(String itemName) {
        By addToCartButton = byAddToCartButtonForItem(itemName);
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
        reusableFunctions.clickButton(addToCartButton);
    }

    public String getCartBadgeText() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(CART_BADGE));
        return driver.findElement(CART_BADGE).getText().trim();
    }

    public String getActionButtonTextForItem(String itemName) {
        By actionButton = byActionButtonForItem(itemName);
        wait.until(ExpectedConditions.visibilityOfElementLocated(actionButton));
        return driver.findElement(actionButton).getText().trim();
    }

    private By byAddToCartButtonForItem(String itemName) {
        return By.xpath("//div[@class='inventory_item' and .//div[@class='inventory_item_name' and normalize-space()='"
                + itemName
                + "']]//button[starts-with(@id,'add-to-cart')]");
    }

    private By byActionButtonForItem(String itemName) {
        return By.xpath("//div[@class='inventory_item' and .//div[@class='inventory_item_name' and normalize-space()='"
                + itemName
                + "']]//button");
    }
}
