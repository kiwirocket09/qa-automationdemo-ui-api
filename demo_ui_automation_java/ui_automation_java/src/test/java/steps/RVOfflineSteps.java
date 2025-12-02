package stepdefs;

import io.cucumber.java.en.*;
import pages.RemoteVerificationPage;
import org.openqa.selenium.WebDriver;

public class RVOfflineSteps {
    private WebDriver driver = Hooks.driver; 
    private RemoteVerificationPage rvPage = new RemoteVerificationPage(driver);
    private String testImagePath = "C:/automation/test-data/report-image.jpg";

    @Given("the user is on the \"Remote Verification\" module")
    public void navigateToAnalysis() {
        rvPage.switchToMainFrame();
        rvPage.navigateToAnalysis();
    }

    @Then("the user opens the Remote Verification module")
    public void openRVModule() {
        rvPage.clickRVModule();
    }

    @And("the user enters {string} as the report title")
    public void enterTitle(String title) {
        rvPage.enterReportTitle(title);
    }

    @Then("the user uploads a supporting photo")
    public void uploadPhoto() throws Exception {
        rvPage.uploadFileUsingRobot(testImagePath);
    }

    @When("the user enters {string} in the description field")
    public void enterDescription(String detail) {
        rvPage.enterDescription(detail);
    }

    @And("the user selects the In-Person verification method")
    public void selectInPerson() {
        rvPage.clickInPersonTab();
    }

    @Given("the user confirms the certification checkbox")
    public void confirmCertification() {
        rvPage.checkICertify();
    }
}