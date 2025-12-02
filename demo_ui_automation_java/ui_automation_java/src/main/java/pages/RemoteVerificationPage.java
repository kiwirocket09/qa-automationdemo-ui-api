import javafx.scene.input.KeyEvent;

public class RemoteVerificationPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;
    private JavascriptExecutor jse;

    private By iCertifyCheckbox = By.xpath("//input[@id='certifycheckbox']");
    private By saveButton = By.id("saveButton");
    private By firstRecord = By.xpath("(//div[contains(@class, 'firstrecord')])");

    public RemoteVerificationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 30);
        this.actions = new Actions(driver);
        this.jse = (JavascriptExecutor) driver;
    }

    public void clickSaveButton() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        actions.moveToElement(element).click().perform();
    }
 
    public void scrollAndFillDetails(String details) {
        WebElement detailsField = driver.findElement(By.id("detailsField"));
        jse.executeScript("arguments[0].scrollIntoView(true);", detailsField);
        detailsField.sendKeys(details);
    }

    public boolean isReportVisible(String title) {
        String xpath = String.format("//a[contains(text(), '%s')]", title);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))).isDisplayed();
    }

    public void uploadFileUsingRobot(String filePath) throws Exception {
        //click upload button 
        wait.until(ExpectedConditions.elementToBeClickable(By.id("btn-choose-file"))).click();
        
        //Use Robot Class to handle keyboard events
        Robot robot = new Robot();
        StringSelection selection = new StringSelection(filePath);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

        robot.delay(1000);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }
}