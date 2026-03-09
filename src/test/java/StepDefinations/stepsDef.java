package StepDefinations;

import Pages.SwagLabs.SwagLabsPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class stepsDef extends Base {

    private SwagLabsPage swagLabsPage;
    private String selectedItemName;

    @Before
    public void setUp() {
        startSession();
        swagLabsPage = new SwagLabsPage(driver);
    }

    @Given("the framework session is started")
    public void frameworkSessionIsStarted() {
        Assert.assertNotNull(driver, "WebDriver session was not started");
    }

    @When("the user reaches the default landing page")
    public void userReachesTheDefaultLandingPage() {
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotNull(currentUrl, "Current URL is null");
        Assert.assertFalse(currentUrl.isBlank(), "Current URL is blank");
    }

    @Then("the framework should be ready for new scripts")
    public void frameworkShouldBeReadyForNewScripts() {
        Assert.assertNotNull(driver.getTitle(), "Page title should be available");
    }

    @Given("^navigate to (.+)$")
    public void navigateTo(String url) {
        swagLabsPage.navigateTo(url.trim());
    }

    @And("type username {string}")
    public void typeUsername(String username) {
        swagLabsPage.typeUsername(username);
    }

    @And("type password {string}")
    public void typePassword(String password) {
        swagLabsPage.typePassword(password);
    }

    @And("click Login")
    public void clickLogin() {
        swagLabsPage.clickLogin();
    }

    @When("^click Add to cart for (.+)$")
    public void clickAddToCartForItem(String itemName) {
        selectedItemName = itemName.trim();
        swagLabsPage.clickAddToCartForItem(selectedItemName);
    }

    @Then("verify cart badge shows 1")
    public void verifyCartBadgeShowsOne() {
        Assert.assertEquals(swagLabsPage.getCartBadgeText(), "1", "Cart badge count is not 1");
        Assert.assertEquals(swagLabsPage.getActionButtonTextForItem(selectedItemName), "Remove",
                "Selected item is not in Remove state");
    }

    @After
    public void tearDown() {
        stopSession();
    }
}
