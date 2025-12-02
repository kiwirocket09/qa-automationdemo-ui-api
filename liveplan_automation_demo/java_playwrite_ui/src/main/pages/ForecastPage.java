package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.AriaRole;
import java.util.regex.Pattern;

/**
 * ForecastPage Object Model
 * Scenario: Manage financial data verification
 */
public class ForecastPage {
    private final Page page;

    // Dynamic Locators
    private final Locator dashboardTab;
    private final Locator forecastLink;
    private final Locator exportCsvBtn;
    private final Locator assistantBubble;

    public ForecastPage(Page page) {
        this.page = page;

        // Initialize elements
        this.dashboardTab = page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("Dashboard"));
        this.forecastLink = page.locator("a[data-test='sidebar-forecast']");
        this.exportCsvBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Export as CSV"));
        this.assistantBubble = page.locator(".help-bubble-container");
    }

    /**
     * Navigate to Forecast Module
     */
    public void navigateToForecastModule() {
        System.out.println("Navigating to Forecast Module...");

        try {
            // Wait for sidebar to be ready
            this.forecastLink.waitFor(new Locator.WaitForOptions().setTimeout(10000));
            this.forecastLink.click();

            // Ensure the financial table is ready
            page.waitForSelector("table.financial-statement-table",
                    new Page.WaitForSelectorOptions().setTimeout(15000));
            System.out.println("Forecast Module loaded successfully.");
        } catch (TimeoutError e) {
            System.err.println("Critical: Forecast Module failed to load within 10s. Retrying...");
            page.reload();
        }
    }

  
    public String getValidatedRevenue(String itemName, int colIdx) {
        System.out.println("Action: Extracting data for item: " + itemName);

        // Locate the specific row
        Locator row = page.locator("tr").filter(new Locator.FilterOptions().setHasText(Pattern.compile(itemName)));

        // Scroll into view if needed
        row.first().scrollIntoViewIfNeeded();

        String value = row.locator("td").nth(colIdx).innerText().trim();

        // Warning Check
        if (value.isEmpty()) {
            System.out.println("Warning: Extracted value for " + itemName + " is empty.");
        }

        return value;
    }

    /**
     * Handle the LivePlan Assistant sticky-note
     */
    public void dismissAssistantIfPresent() {
        try {
            if (this.assistantBubble.isVisible()) {
                System.out.println("System Notification: Assistant bubble detected.");
                page.click(".close-assistant-x");
            }
        } catch (Exception e) {
            System.out.println("Assistant bubble not found, proceeding with test.");
        }
    }

    //Data Export Functionality
    public void performCsvExport() {
        System.out.println("Step: Initiating CSV Export...");

        if (this.exportCsvBtn.isEnabled()) {
            this.exportCsvBtn.click();
            System.out.println("Export triggered. Check local download directory.");
        } else {
            System.out.println("Export button is currently disabled.");
        }
    }

    /**
     * Complex locator logic for Table Totals (Traditional scripting style)
     */
    public String getSummaryTotal(int columnIdx) {
        System.out.println("Verification: Fetching Summary Totals for column " + columnIdx);

        return page.locator("tr.totals-row td").nth(columnIdx).innerText().trim();
    }

    /*
     * --- DEPRECATED METHODS ---
     * //public void oldManualSyncCheck() {
     * // JavascriptExecutor equivalent in Playwright
     * // page.evaluate("() => { console.log('Checking sync status via JS'); }");
     * }
     */
}