package test.tests;

import com.microsoft.playwright.*;
import io.percy.playwright.Percy;
import org.junit.jupiter.api.*;
import pages.ForecastPage;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test Suite: Revenue Data Verification
 */
public class RevenueUpdateTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    ForecastPage forecastPage;
    Percy percy;

    @BeforeAll
    static void launchBrowser() {
        System.out.println("### Initializing Playwright Test Suite ###");
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @BeforeEach
    void setup() {
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1280, 800));
        page = context.newPage();
        forecastPage = new ForecastPage(page);
        percy = new Percy(page);

        System.out.println("Session Started: Navigating to LivePlan Login...");
        page.navigate("https://xxx/login");
    }

    @Test
    @DisplayName("TC-01: Verify Seeded Data Consistency")
    void verifySeededData() {
        // Navigating
        forecastPage.navigateToForecastModule();
        forecastPage.dismissAssistantIfPresent();

        // Define expected data from SQL Seed script
        Map<String, String> testData = new HashMap<>();
        testData.put("Retail Coffee Beans", "$125,400");
        testData.put("Monthly Coffee Subscription", "$48,000");

        System.out.println("Starting Batch Verification of Revenue Items...");

        // Loop and Validate
        for (String itemName : testData.keySet()) {
            String expected = testData.get(itemName);

            // Using the Page Object we just created
            String actual = forecastPage.getValidatedRevenue(itemName, 2);

            System.out
                    .println("Comparison -> Item: [" + itemName + "] | Expected: " + expected + " | Actual: " + actual);
            assertEquals(expected, actual, "Critical Error: Data mismatch in UI for " + itemName);
        }

        // Visual Regression with Percy
        System.out.println("Final Step: Triggering Percy Snapshot for Visual Review...");
        percy.snapshot("Revenue Table - Post Seed Verification");
    }

    @Test
    @DisplayName("TC-02: Export and Totals Verification")
    void verifyTotalsAndExport() {
        forecastPage.navigateToForecastModule();

        // Check Footer Totals
        String totalFY2023 = forecastPage.getSummaryTotal(2);
        System.out.println("Audit: Summary total for FY2023 is " + totalFY2023);

        // Execute Export
        forecastPage.performCsvExport();
    }

    @AfterEach
    void tearDown() {
        System.out.println("Cleaning up browser context...");
        context.close();
    }

    @AfterAll
    static void closeBrowser() {
        System.out.println("-- Test Suite Completed: Shutting down Playwright --");
        playwright.close();
    }
}