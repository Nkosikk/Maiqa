package Pages.Admin;

import Utils.ReusableFunctions;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CourseManagementPage {

    private static final Pattern COURSE_COUNT_PATTERN = Pattern.compile("(\\d+)");

    private final WebDriver driver;
    private final ReusableFunctions reusableFunctions;

    private final By topNavigationLoginButton = By.xpath("//button[.//text()[normalize-space()='Login']]");
    private final By emailInput = By.xpath("//input[@placeholder='Email']");
    private final By passwordInput = By.xpath("//input[@placeholder='Password']");
    private final By submitLoginButton = By.xpath("//main//button[normalize-space()='Login']");
    private final By authenticatedUserMenuButton = By.xpath("//button[contains(normalize-space(),'admin') and contains(normalize-space(),'▼')]");
    private final By adminPanelButton = By.xpath("//button[contains(normalize-space(),'admin') and contains(normalize-space(),'▼')]/following-sibling::*//button[.//text()[normalize-space()='Admin Panel']]");
    private final By coursesSidebarButton = By.xpath("//nav//button[.//text()[normalize-space()='Courses']]");
    private final By courseSummaryText = By.xpath("//p[contains(normalize-space(),'courses available')]");
    private final By addCourseButton = By.xpath("//button[contains(normalize-space(),'Add Course')]");
    private final By titleInput = By.xpath("//*[normalize-space()='Create New Course']/following::input[1]");
    private final By descriptionTextArea = By.xpath("//*[normalize-space()='Description *']/following::textarea[1]");
    private final By durationInput = By.xpath("//input[@placeholder='e.g., 6 weeks']");
    private final By levelSelect = By.xpath("//select");
    private final By priceInput = By.xpath("//input[@type='number']");
    private final By thumbnailUrlInput = By.xpath("//input[@placeholder='https://...']");
    private final By meetingUrlInput = By.xpath("//input[@placeholder='https://teams.microsoft.com/l/meetup-join/...']");
    private final By publishedCheckbox = By.xpath("//input[@type='checkbox']");
    private final By createCourseButton = By.xpath("//button[normalize-space()='Create Course']");

    public CourseManagementPage(WebDriver driver) {
        this.driver = driver;
        this.reusableFunctions = new ReusableFunctions(driver);
    }

    public void navigateTo(String url) {
        driver.get(url);
        reusableFunctions.waitForVisibility(topNavigationLoginButton);
    }

    public void clickTopNavigationLoginButton() {
        reusableFunctions.click(topNavigationLoginButton);
    }

    public void enterEmail(String email) {
        reusableFunctions.type(emailInput, email);
    }

    public void enterPassword(String password) {
        reusableFunctions.type(passwordInput, password);
    }

    public void submitLogin() {
        reusableFunctions.click(submitLoginButton);
    }

    public void openAuthenticatedUserMenu() {
        reusableFunctions.waitForVisibility(authenticatedUserMenuButton);
        reusableFunctions.click(authenticatedUserMenuButton);
    }

    public void clickAdminPanelFromUserMenu() {
        reusableFunctions.click(adminPanelButton);
    }

    public void openCoursesFromSidebar() {
        reusableFunctions.click(coursesSidebarButton);
        reusableFunctions.waitForVisibility(courseSummaryText);
    }

    public int getCourseCountFromSummary() {
        String summary = reusableFunctions.getText(courseSummaryText);
        Matcher matcher = COURSE_COUNT_PATTERN.matcher(summary);
        if (!matcher.find()) {
            throw new IllegalStateException("Unable to parse course count from summary text: " + summary);
        }
        return Integer.parseInt(matcher.group(1));
    }

    public void clickAddCourse() {
        reusableFunctions.click(addCourseButton);
        reusableFunctions.waitForVisibility(titleInput);
    }

    public void fillCourseTitle(String title) {
        reusableFunctions.type(titleInput, title);
    }

    public void fillCourseDescription(String description) {
        reusableFunctions.type(descriptionTextArea, description);
    }

    public void fillCourseDuration(String duration) {
        reusableFunctions.type(durationInput, duration);
    }

    public void selectCourseLevel(String level) {
        String normalizedLevel = level.trim().toLowerCase();
        try {
            reusableFunctions.selectByValue(levelSelect, normalizedLevel);
        } catch (NoSuchElementException missingValueOption) {
            reusableFunctions.selectByVisibleText(levelSelect, level);
        }
    }

    public void fillCoursePrice(String price) {
        reusableFunctions.type(priceInput, price);
    }

    public void fillThumbnailUrl(String thumbnailUrl) {
        reusableFunctions.type(thumbnailUrlInput, thumbnailUrl);
    }

    public void fillMeetingUrl(String meetingUrl) {
        reusableFunctions.type(meetingUrlInput, meetingUrl);
    }

    public boolean isPublishedCheckedByDefault() {
        return reusableFunctions.isSelected(publishedCheckbox);
    }

    public String submitCourseCreationFormAndAcceptAlert() {
        reusableFunctions.click(createCourseButton);
        Alert alert = reusableFunctions.getWait().until(ExpectedConditions.alertIsPresent());
        String alertText = alert.getText();
        alert.accept();
        return alertText;
    }

    public int waitForSummaryCount(int expectedCount) {
        reusableFunctions.getWait().until(driver -> getCourseCountFromSummary() == expectedCount);
        return getCourseCountFromSummary();
    }

    public boolean isCourseVisibleInAdminList(String title) {
        By courseCardTitle = By.xpath("//h3[normalize-space()=" + toXpathLiteral(title) + "]");
        return reusableFunctions.isVisible(courseCardTitle);
    }

    private String toXpathLiteral(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }

        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }

        StringBuilder literalBuilder = new StringBuilder("concat(");
        for (int index = 0; index < value.length(); index++) {
            String character = String.valueOf(value.charAt(index));
            if (index > 0) {
                literalBuilder.append(",");
            }
            if ("'".equals(character)) {
                literalBuilder.append("\"'\"");
            } else if ("\"".equals(character)) {
                literalBuilder.append("'\"'");
            } else {
                literalBuilder.append("'").append(character).append("'");
            }
        }
        literalBuilder.append(")");
        return literalBuilder.toString();
    }
}
