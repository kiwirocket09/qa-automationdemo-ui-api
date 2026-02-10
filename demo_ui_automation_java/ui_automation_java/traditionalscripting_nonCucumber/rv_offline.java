package automation.demo.traditional;
/**
 * Test Scenario: Verify that reports created in Offline Mode are 
 * successfully synchronized to the server once the connection is restored.
 */
@Test
public void testOfflineReportSynchronization() throws Exception {
    JavascriptExecutor jse = (JavascriptExecutor) driver;
    WebDriverWait wait = new WebDriverWait(driver, 30);
    
    // 1. Navigate to the Target Module
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menu-analysis-btn"))).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menu-rv-module-link"))).click();

    // 2. Disconnect from WiFi 
    Runtime.getRuntime().exec("netsh wlan disconnect");
    
    // Handle the "Offline Mode" warning notification
    try {
        WebElement offlineSticky = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("warning-sticky-note")));
        System.out.println("System Notification: " + offlineSticky.getText());
        driver.findElement(By.className("sticky-close-btn")).click();
    } catch (TimeoutException e) {
        System.out.println("Offline sticky note did not appear, proceeding...");
    }

    // 3. Create Randomized Report Title
    Random random = new Random();
    String reportTitle = "Offline_Test_Report_" + random.nextInt(999999);
    
    // Input Report Title with basic retry logic for stability
    WebElement titleField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("report-title-input")));
    titleField.sendKeys(reportTitle);

    // 4. Multiple Image Uploads
    String uploadFilePath = "C:/automation/test-data/sample-image.jpg";
    for (int i = 0; i < 3; i++) {
        // Fill metadata for each file
        driver.findElement(By.id("file-title-" + i)).sendKeys("Evidence_0" + (i + 1));
        driver.findElement(By.id("file-desc-" + i)).sendKeys("Automated upload description");

        // Use sendKeys on the hidden <input type='file'> for direct upload
        WebElement fileInput = driver.findElement(By.id("file-upload-input-" + i));
        fileInput.sendKeys(uploadFilePath);
        
        // Wait for thumbnail to confirm successful upload
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("uploaded-img-preview-" + i)));

        if (i < 2) { // Add another file if not the last iteration
            driver.findElement(By.id("add-more-files-btn")).click();
        }
    }

    // 5.  details section to ensure visibility
    WebElement detailsSection = driver.findElement(By.id("report-details-textarea"));
    jse.executeScript("arguments[0].scrollIntoView(true);", detailsSection);
    detailsSection.sendKeys("Testing data integrity during network outage.");

    // Handle Checkboxes via JS to avoid 'ElementClickInterceptedException'
    WebElement certifyCheckbox = driver.findElement(By.id("certify-agreement-checkbox"));
    jse.executeScript("arguments[0].click();", certifyCheckbox);

    // 6. Save Report in Offline Mode
    driver.findElement(By.id("btn-save-report")).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modal-confirm-save"))).click();
    System.out.println("Report saved locally in Offline Mode.");

    // 7. Restore Connectivity & Verify Sync (Highlight: Network restoration)
    try {
        // Reconnect to the office/test WiFi
        Runtime.getRuntime().exec("netsh wlan connect name=\"Office_Testing_WiFi\"");
        Thread.sleep(10000); //FIX nextitme
    } catch (Exception e) {
        System.out.println("Network restoration failed.");
    }

    // 8. Final Assertions (Highlight: Verification of sync status)
    // Check if the record appears in the 'Online' listing after sync
    wait.until(ExpectedConditions.elementToBeClickable(By.id("btn-refresh-list"))).click();
    
    String firstRecordTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[1]/td[@class='title-cell']"))).getText();
    
    // AssertJ or JUnit assertion to verify sync success
    Assert.assertTrue("The offline report should be visible in the online list", firstRecordTitle.contains(reportTitle));
    System.out.println("Verification Successful: Report '" + reportTitle + "' synced correctly.");
    
    // Clean up
    driver.quit();
}