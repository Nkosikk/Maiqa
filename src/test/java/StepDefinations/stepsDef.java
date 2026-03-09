package StepDefinations;

import Utils.RuntimeConfig;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Locale;
import java.util.UUID;
import org.testng.Assert;

public class stepsDef extends Base {

    private static final String RUN_TAG = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

    private int initialCourseCount;

    private final String courseTitle = RuntimeConfig.get("courseTitle", "E2E Course " + RUN_TAG);
    private final String courseDescription = RuntimeConfig.get("courseDescription", "Automated course creation validation for run " + RUN_TAG + ".");
    private final String courseDuration = RuntimeConfig.get("courseDuration", "4 weeks");
    private final String courseLevel = RuntimeConfig.get("courseLevel", "advanced");
    private final String coursePrice = RuntimeConfig.get("coursePrice", "1500");
    private final String thumbnailUrl = RuntimeConfig.get("thumbnailUrl", "https://example.com/e2e-course-" + RUN_TAG + ".jpg");
    private final String meetingUrl = RuntimeConfig.get("meetingUrl", "https://teams.microsoft.com/l/meetup-join/19%3ameeting_" + RUN_TAG + "%40thread.v2/0?context=%7b%22Tid%22%3a%22default%22%2c%22Oid%22%3a%22default%22%7d");

    @Given("^navigate to (.+) on desktop viewport$")
    public void navigateToUrlOnDesktopViewport(String urlToken) {
        String targetUrl = resolveRuntimeToken(urlToken, "baseUrl", "https://example.com");
        courseManagementPage.navigateTo(targetUrl);
    }

    @And("click the top navigation Login button")
    public void clickTheTopNavigationLoginButton() {
        courseManagementPage.clickTopNavigationLoginButton();
    }

    @And("enter admin email")
    public void enterAdminEmail() {
        String adminEmail = resolveRequiredToken("adminEmail");
        courseManagementPage.enterEmail(adminEmail);
    }

    @And("enter admin password")
    public void enterAdminPassword() {
        String adminPassword = resolveRequiredToken("adminPassword");
        courseManagementPage.enterPassword(adminPassword);
    }

    @And("submit login")
    public void submitLogin() {
        courseManagementPage.submitLogin();
    }

    @When("open the authenticated user menu")
    public void openTheAuthenticatedUserMenu() {
        courseManagementPage.openAuthenticatedUserMenu();
    }

    @And("click Admin Panel from the user menu")
    public void clickAdminPanelFromTheUserMenu() {
        courseManagementPage.clickAdminPanelFromUserMenu();
    }

    @And("open Courses from the admin sidebar")
    public void openCoursesFromTheAdminSidebar() {
        courseManagementPage.openCoursesFromSidebar();
    }

    @Then("verify initial course summary before creation")
    public void verifyInitialCourseSummaryBeforeCreation() {
        initialCourseCount = courseManagementPage.getCourseCountFromSummary();
        Assert.assertTrue(initialCourseCount >= 0, "Initial course count must be zero or more.");
    }

    @And("click Add Course")
    public void clickAddCourse() {
        courseManagementPage.clickAddCourse();
    }

    @And("fill course title")
    public void fillCourseTitle() {
        courseManagementPage.fillCourseTitle(courseTitle);
    }

    @And("fill course description")
    public void fillCourseDescription() {
        courseManagementPage.fillCourseDescription(courseDescription);
    }

    @And("fill course duration")
    public void fillCourseDuration() {
        courseManagementPage.fillCourseDuration(courseDuration);
    }

    @And("select course level")
    public void selectCourseLevel() {
        courseManagementPage.selectCourseLevel(courseLevel);
    }

    @And("fill course price")
    public void fillCoursePrice() {
        courseManagementPage.fillCoursePrice(coursePrice);
    }

    @And("fill thumbnail URL")
    public void fillThumbnailUrl() {
        courseManagementPage.fillThumbnailUrl(thumbnailUrl);
    }

    @And("fill meeting URL")
    public void fillMeetingUrl() {
        courseManagementPage.fillMeetingUrl(meetingUrl);
    }

    @And("verify published is checked by default before submit")
    public void verifyPublishedIsCheckedByDefaultBeforeSubmit() {
        Assert.assertTrue(
                courseManagementPage.isPublishedCheckedByDefault(),
                "Published checkbox should be checked by default before submit."
        );
    }

    @And("submit the course creation form")
    public void submitTheCourseCreationForm() {
        String successAlertText = courseManagementPage.submitCourseCreationFormAndAcceptAlert();
        Assert.assertTrue(
                successAlertText.toLowerCase(Locale.ROOT).contains("course created successfully"),
                "Unexpected success alert message: " + successAlertText
        );
    }

    @And("verify total course count incremented after creation")
    public void verifyTotalCourseCountIncrementedAfterCreation() {
        int expectedCourseCount = initialCourseCount + 1;
        int currentCourseCount = courseManagementPage.waitForSummaryCount(expectedCourseCount);
        Assert.assertEquals(currentCourseCount, expectedCourseCount, "Course count did not increment as expected.");
    }

    @And("verify the new course appears in the admin course list")
    public void verifyTheNewCourseAppearsInTheAdminCourseList() {
        Assert.assertTrue(
                courseManagementPage.isCourseVisibleInAdminList(courseTitle),
                "Could not find created course in admin list. Expected title: " + courseTitle
        );
    }

    private String resolveRuntimeToken(String token, String fallbackPropertyKey, String fallbackDefault) {
        String trimmedToken = token == null ? "" : token.trim();

        if (trimmedToken.isEmpty() || trimmedToken.startsWith("<") || trimmedToken.endsWith(">")) {
            return RuntimeConfig.get(fallbackPropertyKey, fallbackDefault);
        }

        String resolvedPropertyValue = RuntimeConfig.get(trimmedToken, null);
        if (resolvedPropertyValue != null && !resolvedPropertyValue.isBlank()) {
            return resolvedPropertyValue;
        }

        return trimmedToken;
    }

    private String resolveRequiredToken(String key) {
        String value = RuntimeConfig.get(key, "");
        if (value.isBlank()) {
            throw new IllegalStateException("Required runtime value is missing for key: " + key);
        }
        return value;
    }
}
